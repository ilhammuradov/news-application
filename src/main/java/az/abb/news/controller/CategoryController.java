package az.abb.news.controller;

import az.abb.news.model.request.CategoryRequest;
import az.abb.news.model.view.CategoryView;
import az.abb.news.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryView>> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryView> getById(@PathVariable @NonNull Long id) {
        return categoryService.getById(id);
    }

    @PostMapping
    public ResponseEntity<CategoryView> save(@RequestBody @Validated CategoryRequest categoryRequest) {
        return categoryService.create(categoryRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryView> update(@PathVariable @NonNull Long id, @RequestBody @Validated CategoryRequest categoryRequest) {
        return categoryService.update(id, categoryRequest);
    }

   @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable @NonNull Long id) {
        return categoryService.delete(id);
    }
}
