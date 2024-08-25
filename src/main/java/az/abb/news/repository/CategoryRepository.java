package az.abb.news.repository;

import az.abb.news.entity.Author;
import az.abb.news.entity.Category;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Override
    @Cacheable(value = "categoryCache")
    @EntityGraph(attributePaths = {"posts"})
    List<Category> findAll();

    @Override
    @Cacheable(value = "categoryCache")
    @EntityGraph(attributePaths = {"posts"})
    Optional<Category> findById(Long id);

    @Override
    @CachePut(value = "categoryCache")
    Category save(Category category);

    @Override
    @CacheEvict(value = "categoryCache")
    void deleteById(Long id);
}
