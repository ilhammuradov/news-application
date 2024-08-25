package az.abb.news.service;

import az.abb.news.entity.Author;
import az.abb.news.entity.Category;
import az.abb.news.entity.File;
import az.abb.news.entity.Post;
import az.abb.news.exceptions.AuthorNotFoundException;
import az.abb.news.exceptions.CategoryNotFoundException;
import az.abb.news.exceptions.FileNotFoundException;
import az.abb.news.exceptions.PostNotFoundException;
import az.abb.news.mapper.PostMapper;
import az.abb.news.model.request.PostRequest;
import az.abb.news.model.view.PostView;
import az.abb.news.repository.AuthorRepository;
import az.abb.news.repository.CategoryRepository;
import az.abb.news.repository.FileRepository;
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
public class PostService {
    PostRepository postRepository;
    FileRepository fileRepository;
    AuthorRepository authorRepository;
    CategoryRepository categoryRepository;
    PostMapper postMapper;

    public ResponseEntity<List<PostView>> getAll() {
        List<Post> posts = postRepository.findAll();
        List<PostView> postViews = postMapper.toPostViewList(posts);
        return ResponseEntity.ok(postViews);
    }

    public ResponseEntity<PostView> getById(Long id) {
        return postRepository.findById(id)
                .map(post -> ResponseEntity.ok(postMapper.toPostView(post)))
                .orElseThrow(PostNotFoundException::new);
    }

    public ResponseEntity<PostView> create(PostRequest postRequest) {
        Post post = postMapper.toPost(postRequest);

        setPostFields(postRequest, post);
        postRepository.save(post);

        PostView postView = postMapper.toPostView(post);
        return ResponseEntity.ok(postView);
    }

    public ResponseEntity<String> delete(Long id) {
        return postRepository.findById(id).map(post -> {
            postRepository.deleteById(id);
            return ResponseEntity.ok("Post with id " + id + " has been deleted.");
        }).orElseThrow(PostNotFoundException::new);
    }

    public ResponseEntity<PostView> update(Long id, PostRequest postRequest) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(postRequest.title());
                    post.setContent(postRequest.content());
                    setPostFields(postRequest, post);

                    postRepository.save(post);
                    return ResponseEntity.ok(postMapper.toPostView(post));
                })
                .orElseThrow(PostNotFoundException::new);
    }

    private void setPostFields(PostRequest postRequest, Post post) {
        List<Author> authors = postRequest.authorsId().stream()
                .map(a_id -> authorRepository.findById(a_id)
                        .orElseThrow(AuthorNotFoundException::new)).toList();
        post.setAuthors(authors);

        List<Category> categories = postRequest.categoriesId().stream()
                .map(c_id -> categoryRepository.findById(c_id)
                        .orElseThrow(CategoryNotFoundException::new)).toList();
        post.setCategories(categories);

        List<File> files = postRequest.filesId().stream()
                .map(f_id -> fileRepository.findById(f_id)
                        .orElseThrow(FileNotFoundException::new)).toList();
        post.setFiles(files);
    }
}
