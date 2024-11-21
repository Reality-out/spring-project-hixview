package site.hixview.jpa.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.util.ArticleTestUtils;

@OnlyRealRepositoryContext
class ArticleRepositoryTest implements ArticleTestUtils {

    private EntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    private static final Logger log = LoggerFactory.getLogger(ArticleRepositoryTest.class);

    @DisplayName("초기화")
    @Test
    void init() {
        log.info(articleRepository.findAll().toString());
    }
}