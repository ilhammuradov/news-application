package az.abb.news.repository;

import az.abb.news.entity.File;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Override
    @Cacheable(value = "fileCache")
    @EntityGraph(attributePaths = {"post"})
    List<File> findAll();

    @Override
    @Cacheable(value = "fileCache")
    @EntityGraph(attributePaths = {"post"})
    Optional<File> findById(Long id);

    @Override
    @CachePut(value = "fileCache")
    File save(File file);

    @Override
    @CacheEvict(value = "fileCache")
    void deleteById(Long id);
}
