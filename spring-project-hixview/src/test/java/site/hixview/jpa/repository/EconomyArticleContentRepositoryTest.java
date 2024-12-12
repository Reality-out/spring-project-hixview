package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.executor.SqlExecutor;
import site.hixview.support.jpa.util.EconomyArticleContentMapperTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class EconomyArticleContentRepositoryTest implements EconomyArticleContentMapperTestUtils {

    @Autowired
    private EconomyArticleContentRepository economyArticleMapperRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(EconomyArticleContentRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentMapper();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.findByNumber(mapper.getNumber()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("경제 기사로 경제 기사와 컨텐츠 간 매퍼 찾기")
    @Test
    void findByCompanyArticleTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentMapper();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.findByEconomyArticle(mapper.getEconomyArticle())).isEqualTo(List.of(mapper));
    }

    @DisplayName("경제 컨텐츠로 경제 기사와 컨텐츠 간 매퍼 찾기")
    @Test
    void findByCompanyTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentMapper();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.findByEconomyContent(mapper.getEconomyContent())).isEqualTo(List.of(mapper));
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        EconomyArticleContentEntity mapper = economyArticleMapperRepository.save(createEconomyArticleContentMapper());

        // when
        economyArticleMapperRepository.deleteByNumber(mapper.getNumber());

        // then
        assertThat(economyArticleMapperRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 확인")
    @Test
    void existsByNumberTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentMapper();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}