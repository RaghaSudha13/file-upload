package com.java.profile.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

import com.java.profile.model.Student;
import com.java.profile.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ResourceLoader resourceLoader; 

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private static final String UPLOAD_DIRECTORY = "D:/image uploads/src/main/java/com/java/profile/upload";
    
    @Transactional
    public String uploadImage(MultipartFile file) throws IOException {
        String uuid = UUID.randomUUID().toString();
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String fileName = uuid + fileExtension; // Use the original file extension

        // Save the file to the upload directory within the project
        saveImageToFileSystem(file, fileName);

        // Save file details to the database
        Student student = new Student();
        student.setUuid(uuid); // Set the UUID
        student.setFileName(fileName); // Set the file name
        studentRepository.save(student);

        return "" + fileName;
    }

    private void saveImageToFileSystem(MultipartFile file, String fileName) throws IOException {
        String fullPath = UPLOAD_DIRECTORY + File.separator + fileName;
        File directory = new File(UPLOAD_DIRECTORY);
        if (!directory.exists()) {
            boolean created = directory.mkdirs(); // Create the directory if it doesn't exist
            if (!created) {
                throw new IOException("Failed to create the upload directory");
            }
        }
        File outputFile = new File(fullPath);
        file.transferTo(outputFile);
    }

    private String getFileExtension(String filename) {
        if (filename != null && !filename.isEmpty()) {
            return filename.substring(filename.lastIndexOf("."));
        }
        return "";
    }

   
    public ResponseEntity<Resource> downloadFile(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIRECTORY).resolve(filename).normalize();
            Resource resource = resourceLoader.getResource("file:" + filePath.toString());

            if (resource.exists()) {
                logger.info("File found. Returning file: {}", filename); 
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .body(resource);
            } else {
                logger.warn("File not found: {}", filename); 
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("An error occurred while downloading file: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
