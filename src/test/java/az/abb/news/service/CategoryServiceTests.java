package az.abb.news.service;

import az.abb.news.entity.Category;
import az.abb.news.entity.Post;
import az.abb.news.exceptions.CategoryNotFoundException;
import az.abb.news.mapper.CategoryMapper;
import az.abb.news.model.request.CategoryRequest;
import az.abb.news.model.view.CategoryView;
import az.abb.news.repository.CategoryRepository;
import az.abb.news.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private Post post;
    private CategoryView categoryView;
    private CategoryRequest categoryRequest;

    @BeforeEach
    public void setUp() {
        post = Post.builder().id(1L).title("Test Post").content("Test Content").build();
        category = Category.builder().id(1L).name("TestCategory").posts(List.of(post)).build();
        categoryView = CategoryView.builder().id(1L).name("TestCategory").build();
        categoryRequest = CategoryRequest.builder().name("TestCategory").postsId(List.of(1L)).build();
    }


    @Test
    public void testGetAll() {
        given(categoryRepository.findAll()).willReturn(List.of(category));
        given(categoryMapper.toCategoryViewList(List.of(category))).willReturn(List.of(categoryView));

        ResponseEntity<List<CategoryView>> response = categoryService.getAll();

        assertThat(response.getBody()).isEqualTo(List.of(categoryView));
    }
//given when then
    @Test
    public void testGetById() {
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(categoryMapper.toCategoryView(category)).willReturn(categoryView);

        ResponseEntity<CategoryView> response = categoryService.getById(1L);

        assertThat(response.getBody()).isEqualTo(categoryView);
    }

    @Test
    public void testGetByIdFail() {
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.getById(1L))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    public void testCreate() {
        given(categoryMapper.toCategory(categoryRequest)).willReturn(category);
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(categoryRepository.save(category)).willReturn(category);
        given(categoryMapper.toCategoryView(category)).willReturn(categoryView);

        ResponseEntity<CategoryView> response = categoryService.create(categoryRequest);

        assertThat(response.getBody()).isEqualTo(categoryView);
    }

    @Test
    public void testUpdate() {
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(categoryRepository.save(category)).willReturn(category);
        given(categoryMapper.toCategoryView(category)).willReturn(categoryView);

        ResponseEntity<CategoryView> response = categoryService.update(1L, categoryRequest);

        assertThat(response.getBody()).isEqualTo(categoryView);
    }

    @Test
    public void testDelete() {
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        ResponseEntity<String> response = categoryService.delete(1L);

        assertThat(response.getBody()).isEqualTo("Category with id " + 1L + " has been deleted.");
    }

    @Test
    public void testDeleteFail() {
        given(categoryRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.delete(1L))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}
