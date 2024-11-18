package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.method.BasicPostRepositoryFunction;

public interface BlogPostRepository extends BasicPostRepositoryFunction<BlogPostEntity>, JpaRepository<BlogPostEntity, Long> {
}
