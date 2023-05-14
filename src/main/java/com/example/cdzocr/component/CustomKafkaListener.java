package com.example.cdzocr.component;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@Component
public class CustomKafkaListener {

    @Value("tesseract.dataPath")
    private String tesseractDataPath;

    @Autowired
    private KafkaTemplate<String, Map> kafkaTemplate;
   private String extractData(byte[] byteArray) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tesseractDataPath);
        tesseract.setLanguage("eng");
        tesseract.setPageSegMode(1);
        tesseract.setOcrEngineMode(1);
        File file = new File("filename.pdf");
       try (FileOutputStream fileOuputStream = new FileOutputStream(file)){
           fileOuputStream.write(byteArray);
           return tesseract.doOCR(file);
       } catch (IOException | TesseractException e) {
           throw new RuntimeException(e);
       }
    }

    private Map<String, ?> convertExtractedData(String extractedText) {
       String[] lines = extractedText.split("\n");
       Map result = new HashMap();
       String line = lines[0];

       result.put("type", line.trim());

        Map<String, String> content = new HashMap<>();
       for(int i = 1; i < lines.length; i++) {
           if (!lines[i].trim().isEmpty()) {
               String[] lineData = Arrays.stream(lines[i].split(":"))
                       .filter(x -> !x.equals(" ") && !x.isEmpty())
                       .toArray(String[]::new);

               content.put(lineData[0].trim(), lineData[1].trim());
           }
       }
        result.put("content", content);
       return result;
    }

    @KafkaListener(topics = "input", groupId = "ocr")
    public void listenGroupOCR(Map message) {
       String topicName = "response";
       byte[] data = (byte[]) message.get("file");
       String extractedText = this.extractData(data);
       System.out.println(extractedText);
       Map extractedData = this.convertExtractedData(extractedText);
       kafkaTemplate.send(topicName, extractedData);
    }
}
