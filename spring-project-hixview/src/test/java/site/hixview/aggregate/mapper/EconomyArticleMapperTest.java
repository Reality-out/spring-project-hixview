package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.support.spring.util.EconomyArticleTestUtils;
import site.hixview.support.spring.util.dto.EconomyArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class EconomyArticleMapperTest implements EconomyArticleTestUtils, EconomyArticleDtoTestUtils {

    private final EconomyArticleMapperImpl mapperImpl = new EconomyArticleMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(EconomyArticleMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 EconomyArticle 일관성 보장")
    @Test
    void economyArticleMappingWithDomainMapper() {
        assertThat(mapperImpl.toEconomyArticle(mapperImpl.toEconomyArticleDto(economyArticle))).isEqualTo(economyArticle);
    }

    @DisplayName("도메인 매퍼 사용 후 EconomyArticleDto 일관성 보장")
    @Test
    void economyArticleDtoMappingWithDomainMapper() {
        EconomyArticleDto EconomyArticleDto = createEconomyArticleDto();
        assertThat(mapperImpl.toEconomyArticleDto(mapperImpl.toEconomyArticle(EconomyArticleDto))).usingRecursiveComparison().isEqualTo(EconomyArticleDto);
    }
}