package site.hixview.jpa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.executor.SqlExecutor;
import site.hixview.support.jpa.util.EconomyArticleContentEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
@Slf4j
class EconomyArticleContentRepositoryTest implements EconomyArticleContentEntityTestUtils {

    private final EconomyArticleContentEntityRepository economyArticleMapperRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    EconomyArticleContentRepositoryTest(EconomyArticleContentEntityRepository economyArticleMapperRepository, JdbcTemplate jdbcTemplate) {
        this.economyArticleMapperRepository = economyArticleMapperRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        new SqlExecutor().deleteOnlyWithGeneratedId(jdbcTemplate);
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 찾기")
    @Test
    void findByNumberTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentEntity();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.findByNumber(mapper.getNumber()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("경제 기사로 경제 기사와 컨텐츠 간 매퍼 찾기")
    @Test
    void findByCompanyArticleTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentEntity();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.findByEconomyArticle(mapper.getEconomyArticle())).isEqualTo(List.of(mapper));
    }

    @DisplayName("경제 컨텐츠로 경제 기사와 컨텐츠 간 매퍼 찾기")
    @Test
    void findByCompanyTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentEntity();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.findByEconomyContent(mapper.getEconomyContent())).isEqualTo(List.of(mapper));
    }

    @DisplayName("경제 기사와 컨텐츠로 경제 기사와 컨텐츠 간 매퍼 찾기")
    @Test
    void findByEconomyArticleAndEconomyContentTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentEntity();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.findByEconomyArticleAndEconomyContent(
                mapper.getEconomyArticle(), mapper.getEconomyContent()).orElseThrow()).isEqualTo(mapper);
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        EconomyArticleContentEntity mapper = economyArticleMapperRepository.save(createEconomyArticleContentEntity());

        // when
        economyArticleMapperRepository.deleteByNumber(mapper.getNumber());

        // then
        assertThat(economyArticleMapperRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 경제 기사와 컨텐츠 간 매퍼 확인")
    @Test
    void existsByNumberTest() {
        // given
        EconomyArticleContentEntity mapper = createEconomyArticleContentEntity();

        // when
        economyArticleMapperRepository.save(mapper);

        // then
        assertThat(economyArticleMapperRepository.existsByNumber(mapper.getNumber())).isEqualTo(true);
    }
}