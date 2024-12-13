package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.method.BasicPostEntityRepositoryFunction;

import java.util.List;

@Repository
public interface BlogPostEntityRepository extends BasicPostEntityRepositoryFunction<BlogPostEntity>, JpaRepository<BlogPostEntity, Long> {
    /**
     * SELECT BlogPost
     */
    List<BlogPostEntity> findByClassification(String classification);
}
