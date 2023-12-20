package com.nexus.backend.controller;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.nexus.backend.entity.Tender;
import com.nexus.backend.entity.User;
import com.nexus.backend.entity.preferences.Category;
import com.nexus.backend.entity.preferences.Industry;
import com.nexus.backend.entity.preferences.Ministry;
import com.nexus.backend.entity.preferences.State;
import com.nexus.backend.repository.TenderRepository;
import com.nexus.backend.service.AiService;
import com.nexus.backend.service.PdfExtractorService;
import com.nexus.backend.service.TenderService;
import com.nexus.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tender")
public class TenderController {

    @Autowired
    private TenderService actsService;

    @Autowired
    private TenderRepository tenderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PdfExtractorService pdfExtractorService;

    @Autowired
    private AiService aiService;

    @PostMapping("/create")
    public ResponseEntity<Tender> createAct(@RequestBody Tender newAct, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfile(jwt);
        if (user.getIsAdmin() == false){
            return new ResponseEntity<>(newAct, HttpStatus.BAD_REQUEST);
        }
        Tender createdAct = actsService.createAct(newAct, user.getId());
        return new ResponseEntity<>(createdAct, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{tenderId}")
    public ResponseEntity<String> deleteAct(@PathVariable Integer actId) {
        actsService.deleteAct(actId);
        return new ResponseEntity<>("Act deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Tender>> searchActs(@PathVariable String searchString) {
        List<Tender> result = actsService.searchActs(searchString);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/searchByPreferences/{searchString}")
    public ResponseEntity<List<Tender>> searchTenderByPreferences(@PathVariable String searchString, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfile(jwt);

        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        // Extract user preferences
        State userState = user.getState();
        Industry userIndustry = user.getIndustry();
        Ministry userMinistry = user.getMinistry();
        Category userCategory = user.getCategory();

        // Use user preferences to filter tenders by default
        List<Tender> result = actsService.searchTendersByPreferences(userState, userIndustry, userMinistry, userCategory);

        // If a search string is provided, further filter the results
        if (searchString != null && !searchString.isEmpty()) {
            result = result.stream()
                    .filter(tender -> tender.getTitle().toLowerCase().contains(searchString.toLowerCase()) ||
                            tender.getDescription().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/{actId}")
    public ResponseEntity<Tender> getActById(@PathVariable Integer actId) {
        Tender result = tenderRepository.findById(actId).get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Tender> getAllActs() {
        List<Tender> allActs = tenderRepository.findAllByOrderByDateDesc();
        return allActs;
    }

    @GetMapping("/by")
    public ResponseEntity<List<Tender>> getAllActsByUserId(@RequestHeader("Authorization") String jwt) throws Exception {

        Integer userId = userService.findUserProfile(jwt).getId();
        List<Tender> allActs = tenderRepository.findAllByUploaderId(userId);

        if(allActs == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(allActs, HttpStatus.OK);
    }

    @GetMapping("/docList/{actId}")
    public ResponseEntity<String> getDocumentList(@PathVariable Integer actId){

        Tender act = tenderRepository.findById(actId).get();
        if (act == null)
            return new ResponseEntity<>("Tender not found", HttpStatus.NOT_FOUND);


        String response = aiService.getDocList(act);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/checkFromPlan/{actId}")
    public ResponseEntity<String> checkIfCompliantByPlan(@PathVariable Integer actId, @RequestBody String userPlan){
        Tender act = tenderRepository.findById(actId).get();
        if (act == null)
            return new ResponseEntity<>("Tender not found", HttpStatus.NOT_FOUND);

        String response = aiService.checkIfTenderCompliant(act, userPlan);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @PostMapping("/checkPdf")
//    public ResponseEntity<String> checkIfTenderIsCompliantWithPdf(
//            @PathVariable Integer actId,
//            @RequestParam MultipartFile userTender) {
//
//        Tender act = tenderRepository.findById(actId).orElse(null);
//        if (act == null)
//            return new ResponseEntity<>("Tender not found", HttpStatus.NOT_FOUND);
//
//        // Extract text from the uploaded PDF file
//        String extractedText = extractPdf(userTender);
//
//        // Call the AI service with the extracted text
//        String response = aiService.checkIfTenderIsCompliant(act, extractedText);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }

    private String extractPdf(MultipartFile file) {
        StringBuilder builder = new StringBuilder();

        try {
            // Use PdfReader with InputStream obtained from MultipartFile
            PdfReader pdfReader = new PdfReader(file.getInputStream());

            int pages = pdfReader.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                String text = PdfTextExtractor.getTextFromPage(pdfReader, i);
                builder.append("Page " + i + ":\n" + text);
            }

            pdfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
