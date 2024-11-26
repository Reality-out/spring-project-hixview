package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleRepositoryFunction;

@Repository
public interface CompanyArticleRepository extends BasicArticleRepositoryFunction<CompanyArticleEntity>, JpaRepository<CompanyArticleEntity, Long> {
}
