package com.java.profile.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.java.profile.service.StudentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/v1")
public class StudentController {

    @Autowired
    StudentService studentService;

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageURL = studentService.uploadImage(file);
            logger.info("Uploaded Succesfully: {}", imageURL);

            // Extract file extension
            String fileExtension = getFileExtension(file.getOriginalFilename());

            // Customize return message based on file extension
            String message = "";
            switch (fileExtension.toLowerCase()) {
                case ".pdf":
                    message = "PDF file uploaded successfully. File URL: ";
                    break;
                case ".xlsx":
                    message = "Excel file uploaded successfully. File URL: ";
                    break;
                case ".jpg":
                case ".jpeg":
                case ".png":
                    message = "Image uploaded successfully. File URL: ";
                    break;
                case ".txt":
                    message = "Text file uploaded successfully. File URL: ";
                    break;
                default:
                    message = "File uploaded successfully. File URL: ";
                    break;
            }

            return message + imageURL;
        } catch (Exception e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    private String getFileExtension(String filename) {
        if (filename != null && !filename.isEmpty()) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        logger.info("Attempting to download file: {}", filename); 
        return studentService.downloadFile(filename);
    }
}
