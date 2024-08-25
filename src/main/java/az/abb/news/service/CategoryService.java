package az.abb.news.service;

import az.abb.news.entity.Category;
import az.abb.news.entity.Post;
import az.abb.news.exceptions.CategoryNotFoundException;
import az.abb.news.exceptions.PostNotFoundException;
import az.abb.news.mapper.CategoryMapper;
import az.abb.news.model.request.CategoryRequest;
import az.abb.news.model.view.CategoryView;
import az.abb.news.repository.CategoryRepository;
import az.abb.news.repository.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;
    PostRepository postRepository;
    CategoryMapper categoryMapper;

    public ResponseEntity<List<CategoryView>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryView> categoryViews = categoryMapper.toCategoryViewList(categories);
        return ResponseEntity.ok(categoryViews);
    }

    public ResponseEntity<CategoryView> getById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> ResponseEntity.ok(categoryMapper.toCategoryView(category)))
                .orElseThrow(CategoryNotFoundException::new);
    }

    public ResponseEntity<CategoryView> create(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toCategory(categoryRequest);

        List<Post> posts=categoryRequest.postsId().stream()
                .map(p_id->postRepository.findById(p_id)
                        .orElseThrow(PostNotFoundException::new))
                .toList();
        category.setPosts(posts);

        category = categoryRepository.save(category);
        return ResponseEntity.ok(categoryMapper.toCategoryView(category));
    }

    public ResponseEntity<CategoryView> update(Long id, CategoryRequest categoryRequest) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryRequest.name());

                    List<Post> posts=categoryRequest.postsId().stream()
                            .map(p_id->postRepository.findById(p_id)
                                    .orElseThrow(PostNotFoundException::new))
                            .toList();
                    category.setPosts(posts);
                    category = categoryRepository.save(category);
                    return ResponseEntity.ok(categoryMapper.toCategoryView(category));
                })
                .orElseThrow(CategoryNotFoundException::new);
    }

    public ResponseEntity<String> delete(Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.deleteById(id);
                    return ResponseEntity.ok("Category with id " + id + " has been deleted.");
                })
                .orElseThrow(CategoryNotFoundException::new);
    }
}
