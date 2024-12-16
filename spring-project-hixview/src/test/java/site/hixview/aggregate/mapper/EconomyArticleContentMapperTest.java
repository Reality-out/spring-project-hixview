package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.EconomyArticleContentDto;
import site.hixview.support.spring.util.EconomyArticleContentTestUtils;
import site.hixview.support.spring.util.dto.EconomyArticleContentDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class EconomyArticleContentMapperTest implements EconomyArticleContentTestUtils, EconomyArticleContentDtoTestUtils {

    private final EconomyArticleContentMapperImpl mapperImpl = new EconomyArticleContentMapperImpl();

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
