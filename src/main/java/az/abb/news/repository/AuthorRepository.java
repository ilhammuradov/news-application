package az.abb.news.repository;

import az.abb.news.entity.Author;
import az.abb.news.model.view.AuthorView;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Override
    @Cacheable(value = "authorCache")
    @EntityGraph(attributePaths = {"posts"})
        //@Query("SELECT a FROM Author a JOIN FETCH a.posts")
    List<Author> findAll();

    @Override
    @Cacheable(value = "authorCache")
    @EntityGraph(attributePaths = {"posts"})
        //@Query("SELECT a FROM Author a JOIN FETCH a.posts WHERE a.id = :id")
    Optional<Author> findById(Long id);

    @Override
    @CachePut(value = "authorCache")
    Author save(Author author);

    @Override
    @CacheEvict(value = "authorCache")
    void deleteById(Long id);

    AuthorView findByName(String name);
}
