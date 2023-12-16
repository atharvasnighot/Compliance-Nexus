package com.nexus.backend.service;

import com.nexus.backend.entity.Updates;
import com.nexus.backend.entity.User;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import com.nexus.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UpdatesService {

    @Autowired
    private UpdatesRepository updatesRepository;

    @Autowired
    private UserPreferenceService userPreferenceService;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private MinistryRepository ministryRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    public Updates createUpdate(Updates newUpdate, Integer userId, MultipartFile pdfFile) throws Exception {

        newUpdate.setDate(LocalDateTime.now());
        newUpdate.setUploaderId(userId);

        if (newUpdate.getMinistry() != null) {
            Ministry ministry = newUpdate.getMinistry().getId() != null
                    ? ministryRepository.findById(newUpdate.getMinistry().getId()).orElse(null)
                    : null;
            newUpdate.setMinistry(ministry);
        }

        if (newUpdate.getIndustry() != null) {
            Industry industry = newUpdate.getIndustry().getId() != null
                    ? industryRepository.findById(newUpdate.getIndustry().getId()).orElse(null)
                    : null;
            newUpdate.setIndustry(industry);
        }

        if (newUpdate.getCategory() != null) {
            Category category = newUpdate.getCategory().getId() != null
                    ? categoryRepository.findById(newUpdate.getCategory().getId()).orElse(null)
                    : null;
            newUpdate.setCategory(category);
        }

        if (newUpdate.getState() != null) {
            State state = newUpdate.getState().getId() != null
                    ? stateRepository.findById(newUpdate.getState().getId()).orElse(null)
                    : null;
            newUpdate.setState(state);
        }

        if (pdfFile != null) {
            String pdfPath = storePdf(pdfFile);
            newUpdate.setPdfPath(pdfPath);
        }

        sendMailsToUsers(newUpdate);

        return updatesRepository.save(newUpdate);
    }

    public void sendMailsToUsers(Updates newUpdate){

        List<User> usersWithPreferences = userPreferenceService.getUsersWithPreferences(
                (newUpdate.getMinistry() != null) ? newUpdate.getMinistry().getId() : null,
                (newUpdate.getIndustry() != null) ? newUpdate.getIndustry().getId() : null,
                (newUpdate.getCategory() != null) ? newUpdate.getCategory().getId() : null,
                (newUpdate.getState() != null) ? newUpdate.getState().getId() : null
        );

        String mailBody = newUpdate.getTitle() + "\n\n" + newUpdate.getDescription();

        for (User user : usersWithPreferences) {
            System.out.println(user.getEmail().trim());
            if (user.getEmail() != null && !user.getEmail().isEmpty())
                emailSenderService.sendMail(user.getEmail().trim(), "New Update From Compliance Nexus", mailBody);
        }

    }

    private String storePdf(MultipartFile pdfFile) throws IOException {
        if (pdfFile.isEmpty()) {
            return null;
        }
        String fileName = UUID.randomUUID().toString() + ".pdf";

        Path targetDirectory = Path.of(uploadDirectory);

        Files.createDirectories(targetDirectory);

        Path targetPath = targetDirectory.resolve(fileName);

        Files.copy(pdfFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public ResponseEntity<Resource> getUpdatePdf(Integer updateId) throws IOException {
        Optional<Updates> updatesOptional = getUpdateById(updateId);

        if (updatesOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Updates update = updatesOptional.get();
        String pdfPath = update.getPdfPath();

        if (pdfPath == null || pdfPath.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Path pdfFile = Path.of(uploadDirectory, pdfPath);
        Resource resource = new UrlResource(pdfFile.toUri());

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public List<Updates> getAllUpdates() {
        return updatesRepository.findAllByOrderByDateDesc();
    }

    public Optional<Updates> getUpdateById(Integer id) {
        return updatesRepository.findById(id);
    }
}
