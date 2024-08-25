package az.abb.news.repository;

import az.abb.news.entity.Post;
import az.abb.news.model.view.PostView;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    @Cacheable(value = "postCache")
    @EntityGraph(attributePaths = {"authors","files","categories"})
    List<Post> findAll();

    @Override
    @Cacheable(value = "postCache")
    @EntityGraph(attributePaths = {"authors","files","categories"})
    Optional<Post> findById(Long id);

    @Override
    @CachePut(value = "postCache")
    Post save(Post post);

    @Override
    @CacheEvict(value = "postCache")
    void deleteById(Long id);

    Optional<List<PostView>> findAllByAuthorsId(Long id);
}
