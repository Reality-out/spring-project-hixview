package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.CompanyArticleEntity;

@Repository
public interface CompanyArticleRepository extends JpaRepository<CompanyArticleEntity, Long> {
}
