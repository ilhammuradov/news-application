package az.abb.news.controller;

import az.abb.news.model.view.FileView;
import az.abb.news.service.FileService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//..feignclient
//       .. fallback

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/files")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileView>> getAll() {
        return fileService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileView> getById(@PathVariable @NonNull Long id) {
        return fileService.getById(id);
    }
    @PostMapping
    public ResponseEntity<FileView> uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable @NonNull Long id) {
        return fileService.downloadFile(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable @NonNull Long id) {
        return fileService.deleteFile(id);
    }
}
