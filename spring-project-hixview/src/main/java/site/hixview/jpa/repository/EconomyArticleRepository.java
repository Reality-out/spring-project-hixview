package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.EconomyArticleEntity;

@Repository
public interface EconomyArticleRepository extends JpaRepository<EconomyArticleEntity, Long> {
}
