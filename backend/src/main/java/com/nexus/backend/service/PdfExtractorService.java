package com.nexus.backend.service;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class PdfExtractorService {

    public String extractPdf(String filePath) {
        StringBuilder builder = new StringBuilder();

        try {
            PdfReader pdfReader = new PdfReader(filePath);

            int pages = pdfReader.getNumberOfPages();
            for (int i = 1; i <= pages; i++) {
                String text = PdfTextExtractor.getTextFromPage(pdfReader, i);
                builder.append("Page " + i + ":\n" + text);
            }

            pdfReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //saveTextToFile(builder.toString());
        System.out.println(builder.toString());
        return builder.toString();
    }

    public void saveTextToFile(String text) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("A:\\Downloads\\extracted_text.txt"))) {
            writer.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
