package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;
import site.hixview.support.spring.util.EconomyArticleTestUtils;
import site.hixview.support.spring.util.dto.EconomyArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class EconomyArticleMapperTest implements EconomyArticleTestUtils, EconomyArticleDtoTestUtils {

    private final EconomyArticleMapper mapperImpl = new EconomyArticleMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 EconomyArticle 일관성 보장")
    @Test
    void economyArticleMappingWithDomainMapper() {
        assertThat(mapperImpl.toEconomyArticle(mapperImpl.toEconomyArticleDto(economyArticle))).isEqualTo(economyArticle);
        assertThat(mapperImpl.toEconomyArticle(mapperImpl.toEconomyArticleDtoNoNumber(economyArticle)))
                .usingRecursiveComparison().ignoringFields(NUMBER).isEqualTo(economyArticle);
    }

    @DisplayName("도메인 매퍼 사용 후 EconomyArticleDto 일관성 보장")
    @Test
    void economyArticleDtoMappingWithDomainMapper() {
        EconomyArticleDto EconomyArticleDto = createEconomyArticleDto();
        assertThat(mapperImpl.toEconomyArticleDto(mapperImpl.toEconomyArticle(EconomyArticleDto))).usingRecursiveComparison().isEqualTo(EconomyArticleDto);
    }

    @DisplayName("도메인 매퍼 사용 후 EconomyArticleDtoNoNumber 일관성 보장")
    @Test
    void economyArticleDtoNoNumberMappingWithDomainMapper() {
        EconomyArticleDtoNoNumber EconomyArticleDto = createEconomyArticleDtoNoNumber();
        assertThat(mapperImpl.toEconomyArticleDtoNoNumber(mapperImpl.toEconomyArticle(EconomyArticleDto))).usingRecursiveComparison().isEqualTo(EconomyArticleDto);
    }
}