package az.abb.news.service;

import az.abb.news.entity.File;
import az.abb.news.exceptions.FileStorageException;
import az.abb.news.mapper.FileMapper;
import az.abb.news.model.view.FileView;
import az.abb.news.repository.FileRepository;
import az.abb.news.exceptions.FileNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileService {
   final FileRepository fileRepository;
   final FileMapper fileMapper;

    @Value("${upload.dir}")
    String uploadDir;

    public ResponseEntity<List<FileView>> getAll() {
        List<File> files = fileRepository.findAll();
        List<FileView> fileViews = fileMapper.toFileViewList(files);
        return ResponseEntity.ok(fileViews);
    }

    public ResponseEntity<FileView> getById(Long id) {
        return fileRepository.findById(id)
                .map(file -> ResponseEntity.ok(fileMapper.toFileView(file)))
                .orElseThrow(az.abb.news.exceptions.FileNotFoundException::new);
    }

    public ResponseEntity<FileView> uploadFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Path path = Paths.get(uploadDir + java.io.File.separator + fileName);

        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
            fos.write(multipartFile.getBytes());

            File fileEntity = az.abb.news.entity.File.builder()
                    .type(multipartFile.getContentType())
                    .url(path.toString())
                    //folder
                    .build();

            fileRepository.save(fileEntity);

            FileView fileView = fileMapper.toFileView(fileEntity);
            return ResponseEntity.ok(fileView);
        } catch (java.io.FileNotFoundException e) {
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + fileName, e);
        }
    }

    public ResponseEntity<Resource> downloadFile(Long id) {
        File fileEntity = fileRepository.findById(id)
                .orElseThrow(FileNotFoundException::new);
        Path filePath = Paths.get(fileEntity.getUrl());
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new FileStorageException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new FileStorageException("Error: " + e.getMessage());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getType() + "\"")
                .body(resource);
    }

    public ResponseEntity<String> deleteFile(Long id) {
        return fileRepository.findById(id).map(file -> {
            fileRepository.deleteById(id);
            return ResponseEntity.ok("File with id" + id + "has been deleted.");
        }).orElseThrow(FileNotFoundException::new);
    }
}

//BDDMockito