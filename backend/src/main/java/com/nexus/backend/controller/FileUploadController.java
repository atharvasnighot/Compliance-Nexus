package com.nexus.backend.controller;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.nexus.backend.entity.Tender;
import com.nexus.backend.repository.TenderRepository;
import com.nexus.backend.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private AiService aiService;

    @Autowired
    private TenderRepository tenderRepository;

    @PostMapping("/pdfs/{actId}")
    public ResponseEntity<String> handleFileUpload(@RequestParam("files") MultipartFile[] files, @PathVariable Integer actId) {
        List<String> extractedTexts = new ArrayList<>();

        // Process each uploaded file
        for (MultipartFile file : files) {
            try {
                String extractedText = extractTextFromPDF(file);
                extractedTexts.add(extractedText);
            } catch (IOException e) {
                System.out.println("Error at PDF extraction");
                e.printStackTrace();
            }
        }

        List<String> userDocList = new ArrayList<>();
        for (String text: extractedTexts) {
//            try {
//                TimeUnit.SECONDS.sleep(21);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            userDocList.add(aiService.getDocType(text));
        }

        Tender tender = tenderRepository.findById(actId).get();
        if (tender == null)
            return new ResponseEntity<>("Tender not found", HttpStatus.NOT_FOUND);
        String response = aiService.checkMissingDocuments(userDocList, tender);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private String extractTextFromPDF(MultipartFile pdfFile) throws IOException {
        try (InputStream inputStream = pdfFile.getInputStream()) {
            PdfReader pdfReader = null;
            try {
                pdfReader = new PdfReader(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int pages = pdfReader.getNumberOfPages();
            StringBuilder extractedText = new StringBuilder();

            for (int i = 1; i <= pages; i++) {
                String text = PdfTextExtractor.getTextFromPage(pdfReader, i);
                extractedText.append("Page ").append(i).append(":\n").append(text);
            }

            return extractedText.toString();
        }
    }
}

