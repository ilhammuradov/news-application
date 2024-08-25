package az.abb.news.service;

import az.abb.news.entity.Author;
import az.abb.news.entity.Category;
import az.abb.news.entity.File;
import az.abb.news.entity.Post;
import az.abb.news.exceptions.PostNotFoundException;
import az.abb.news.mapper.PostMapper;
import az.abb.news.model.request.PostRequest;
import az.abb.news.model.view.PostView;
import az.abb.news.repository.AuthorRepository;
import az.abb.news.repository.CategoryRepository;
import az.abb.news.repository.FileRepository;
import az.abb.news.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {
    @Mock
    private PostRepository postRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;

    private Post post;
    private PostRequest postRequest;
    private PostView postView;
    private File file;
    private Author author;
    private Category category;

    @BeforeEach
    public void setUp() {
        postView = PostView.builder().id(1L).title("title").content("content").build();
        author = Author.builder().id(1L).name("Ilham Muradov").build();
        category = Category.builder().id(1L).name("TestCategory").build();
        file = File.builder().id(1L).type("type").url("url").build();
        post = Post.builder().id(1L).title("title").content("content").authors(List.of(author)).categories(List.of(category)).files(List.of(file)).build();
        postRequest = PostRequest.builder().title("title").content("content").authorsId(List.of(1L)).categoriesId(List.of(1L)).filesId(List.of(1L)).build();
    }

    @Test
    public void testGetAll() {
        given(postRepository.findAll()).willReturn(List.of(post));
        given(postMapper.toPostViewList(List.of(post))).willReturn(List.of(postView));

        ResponseEntity<List<PostView>> response = postService.getAll();

        assertThat(response.getBody()).isEqualTo(List.of(postView));
    }

    @Test
    public void testGetById() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(postMapper.toPostView(post)).willReturn(postView);

        ResponseEntity<PostView> response = postService.getById(1L);

        assertThat(response.getBody()).isEqualTo(postView);
    }

    @Test
    public void testGetByIdFail() {
        given(postRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getById(1L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    public void testCreate() {
        given(postMapper.toPost(postRequest)).willReturn(post);
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(fileRepository.findById(1L)).willReturn(Optional.of(file));
        given(postRepository.save(post)).willReturn(post);
        given(postMapper.toPostView(post)).willReturn(postView);

        ResponseEntity<PostView> response = postService.create(postRequest);

        assertThat(response.getBody()).isEqualTo(postView);

    }

    @Test
    public void testUpdate() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        given(fileRepository.findById(1L)).willReturn(Optional.of(file));
        given(postRepository.save(post)).willReturn(post);
        given(postMapper.toPostView(post)).willReturn(postView);

        ResponseEntity<PostView> response = postService.update(1L, postRequest);

        assertThat(response.getBody()).isEqualTo(postView);
    }

    @Test
    public void testDelete() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        ResponseEntity<String> response = postService.delete(1L);

        assertThat(response.getBody()).isEqualTo("Post with id " + 1L + " has been deleted.");
    }

    @Test
    public void testDeleteFail() {
        given(postRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> postService.delete(1L))
                .isInstanceOf(PostNotFoundException.class);
    }
}
