package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleEntityRepositoryFunction;

@Repository
public interface CompanyArticleEntityRepository extends BasicArticleEntityRepositoryFunction<CompanyArticleEntity>, JpaRepository<CompanyArticleEntity, Long> {
}
