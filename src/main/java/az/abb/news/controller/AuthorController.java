package az.abb.news.controller;

import az.abb.news.model.request.AuthorRequest;
import az.abb.news.model.view.AuthorView;
import az.abb.news.service.AuthorService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthorController {
    AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorView>> getAll() {
        return authorService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorView> getById(@PathVariable @NonNull Long id) {
        return authorService.getById(id);
    }

    @PostMapping
    public ResponseEntity<AuthorView> create(@RequestBody @Validated AuthorRequest authorRequest) {
        return authorService.create(authorRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorView> update(@PathVariable @NonNull Long id, @RequestBody @Validated AuthorRequest authorRequest) {
        return authorService.update(id, authorRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable @NonNull Long id) {
        return authorService.delete(id);
    }
}

