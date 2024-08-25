package az.abb.news.service;

import az.abb.news.entity.Author;
import az.abb.news.entity.Post;
import az.abb.news.exceptions.AuthorNotFoundException;
import az.abb.news.mapper.AuthorMapper;
import az.abb.news.model.request.AuthorRequest;
import az.abb.news.model.view.AuthorView;
import az.abb.news.repository.AuthorRepository;
import az.abb.news.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTests {
    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    private Author author;
    private AuthorRequest authorRequest;
    private AuthorView authorView;
    private Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder().id(1L).title("Test Post").content("Test Content").build();
        author = Author.builder().id(1L).name("Ilham Muradov").posts(List.of(post)).build();
        authorRequest = new AuthorRequest("Ilham Muradov", List.of(1L));
        authorView = new AuthorView(1L, "Ilham Muradov", List.of(1L));
    }


    @Test
    public void testGetAll() {
        given(authorRepository.findAll()).willReturn(List.of(author));
        given(authorMapper.toAuthorViewList(List.of(author))).willReturn(List.of(authorView));

        ResponseEntity<List<AuthorView>> response = authorService.getAll();

        assertThat(response.getBody()).isEqualTo(List.of(authorView));
        verify(authorRepository).findAll();
        verify(authorMapper).toAuthorViewList(List.of(author));
    }

    @Test
    public void testCreate() {
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(authorMapper.toAuthor(authorRequest)).willReturn(author);
        given(authorRepository.save(author)).willReturn(author);
        given(authorMapper.toAuthorView(author)).willReturn(authorView);

        ResponseEntity<AuthorView> response = authorService.create(authorRequest);

        assertThat(response.getBody()).isEqualTo(authorView);
        verify(postRepository).findById(1L);
        verify(authorMapper).toAuthor(authorRequest);
        verify(authorRepository).save(author);
        verify(authorMapper).toAuthorView(author);
    }

    @Test
    public void testGetById() {
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(authorMapper.toAuthorView(author)).willReturn(authorView);

        ResponseEntity<AuthorView> response = authorService.getById(1L);

        assertThat(response.getBody()).isEqualTo(authorView);
    }

    @Test
    public void testGetByIdFail() {
        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.getById(1L))
                .isInstanceOf(AuthorNotFoundException.class);
    }

    @Test
    public void testUpdate() {
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));
        given(authorMapper.toAuthorView(author)).willReturn(authorView);
        given(postRepository.findById(1L)).willReturn(Optional.of(post));
        given(authorRepository.save(author)).willReturn(author);

        ResponseEntity<AuthorView> response = authorService.update(1L, authorRequest);

        assertThat(response.getBody()).isEqualTo(authorView);
    }

    @Test
    public void testDelete() {
        given(authorRepository.findById(1L)).willReturn(Optional.of(author));

        ResponseEntity<String> response = authorService.delete(1L);

        assertThat(response.getBody()).isEqualTo("Author with id " + 1L + " has been deleted.");
    }

    @Test
    public void testDeleteFail() {
        given(authorRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> authorService.delete(1L))
                .isInstanceOf(AuthorNotFoundException.class);
    }
}
