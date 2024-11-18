package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleRepositoryFunction;

public interface CompanyArticleRepository extends BasicArticleRepositoryFunction<CompanyArticleEntity>, JpaRepository<CompanyArticleEntity, Long> {
}
