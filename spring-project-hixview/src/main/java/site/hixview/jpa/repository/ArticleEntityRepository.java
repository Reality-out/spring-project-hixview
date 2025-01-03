package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.ArticleEntity;

import java.util.Optional;

@Repository
public interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {
    /**
     * SELECT Article
     */
    Optional<ArticleEntity> findByNumber(Long number);

    /**
     * DELETE Article
     */
    void deleteByNumber(Long number);

    /**
     * CHECK Article
     */
    boolean existsByNumber(Long number);
}
