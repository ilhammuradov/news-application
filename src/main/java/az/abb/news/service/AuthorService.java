package az.abb.news.service;

import az.abb.news.entity.Author;
import az.abb.news.entity.Post;
import az.abb.news.exceptions.AuthorNotFoundException;
import az.abb.news.exceptions.PostNotFoundException;
import az.abb.news.mapper.AuthorMapper;
import az.abb.news.model.request.AuthorRequest;
import az.abb.news.model.view.AuthorView;
import az.abb.news.repository.AuthorRepository;
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
public class AuthorService {
    AuthorRepository authorRepository;
    PostRepository postRepository;
    AuthorMapper authorMapper;

    public ResponseEntity<List<AuthorView>> getAll() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorView> authorViews = authorMapper.toAuthorViewList(authors);
        return ResponseEntity.ok(authorViews);
    }

    public ResponseEntity<AuthorView> getById(Long id) {
        return authorRepository.findById(id)
                .map(author -> ResponseEntity.ok(authorMapper.toAuthorView(author)))
                .orElseThrow(AuthorNotFoundException::new);
    }

    public ResponseEntity<AuthorView> create(AuthorRequest authorRequest) {
        Author author = authorMapper.toAuthor(authorRequest);

        List<Post> posts=authorRequest.postsId().stream()
                .map(p_id->postRepository.findById(p_id)
                        .orElseThrow(PostNotFoundException::new))
                .toList();
        author.setPosts(posts);

        author = authorRepository.save(author);
        return ResponseEntity.ok(authorMapper.toAuthorView(author));
    }

    public ResponseEntity<AuthorView> update(Long id, AuthorRequest authorRequest) {
        return authorRepository.findById(id)
                .map(author -> {
                    author.setName(authorRequest.name());

                    List<Post> posts = authorRequest.postsId().stream()
                            .map(p_id -> postRepository.findById(p_id)
                                    .orElseThrow(PostNotFoundException::new))
                            .toList();
                    author.setPosts(posts);

                    Author updatedAuthor = authorRepository.save(author);
                    return ResponseEntity.ok(authorMapper.toAuthorView(updatedAuthor));
                })
                .orElseThrow(AuthorNotFoundException::new);
    }

    public ResponseEntity<String> delete(Long id) {
        return authorRepository.findById(id)
                .map(author -> {
                    authorRepository.deleteById(id);
                    return ResponseEntity.ok("Author with id " + id + " has been deleted.");
                })
                .orElseThrow(AuthorNotFoundException::new);
    }
}
