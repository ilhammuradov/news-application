package az.abb.news.controller;

import az.abb.news.model.request.PostRequest;
import az.abb.news.model.view.PostView;
import az.abb.news.service.PostService;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @GetMapping
    public ResponseEntity<List<PostView>> getAll() {
        return postService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<PostView> getById(@PathVariable @NonNull Long id) {
        return postService.getById(id);
    }

    @PostMapping
    public ResponseEntity<PostView> create(@RequestBody @Validated PostRequest postRequest) {
        return postService.create(postRequest);
    }

    @PutMapping("{id}")
    public ResponseEntity<PostView> update(@PathVariable @NonNull Long id, @RequestBody @Validated PostRequest postRequest) {
        return postService.update(id, postRequest);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable @NonNull Long id) {
        return postService.delete(id);
    }
}
