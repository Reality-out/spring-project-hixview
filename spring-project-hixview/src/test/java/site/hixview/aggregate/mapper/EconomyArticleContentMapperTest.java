package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.EconomyArticleContentDto;
import site.hixview.support.spring.util.EconomyArticleContentTestUtils;
import site.hixview.support.spring.util.dto.EconomyArticleContentDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class EconomyArticleContentMapperTest implements EconomyArticleContentTestUtils, EconomyArticleContentDtoTestUtils {

    private final EconomyArticleContentMapperImpl mapperImpl = new EconomyArticleContentMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(EconomyArticleContentMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 EconomyArticleContent 일관성 보장")
    @Test
    void economyArticleContentMappingWithDomainMapper() {
        assertThat(mapperImpl.toEconomyArticleContent(mapperImpl.toEconomyArticleContentDto(economyArticleContent))).isEqualTo(economyArticleContent);
    }

    @DisplayName("도메인 매퍼 사용 후 EconomyArticleContentDto 일관성 보장")
    @Test
    void economyArticleContentDtoMappingWithDomainMapper() {
        EconomyArticleContentDto EconomyArticleContentDto = createEconomyArticleContentDto();
        assertThat(mapperImpl.toEconomyArticleContentDto(mapperImpl.toEconomyArticleContent(EconomyArticleContentDto))).usingRecursiveComparison().isEqualTo(EconomyArticleContentDto);
    }
}
