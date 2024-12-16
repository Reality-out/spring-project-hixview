package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.EconomyContentDto;
import site.hixview.support.spring.util.EconomyContentTestUtils;
import site.hixview.support.spring.util.dto.EconomyContentDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class EconomyContentMapperTest implements EconomyContentTestUtils, EconomyContentDtoTestUtils {

    private final EconomyContentMapperImpl mapperImpl = new EconomyContentMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 EconomyContent 일관성 보장")
    @Test
    void economyContentMappingWithDomainMapper() {
        assertThat(mapperImpl.toEconomyContent(mapperImpl.toEconomyContentDto(economyContent))).isEqualTo(economyContent);
    }

    @DisplayName("도메인 매퍼 사용 후 EconomyContentDto 일관성 보장")
    @Test
    void economyContentDtoMappingWithDomainMapper() {
        EconomyContentDto EconomyContentDto = createEconomyContentDto();
        assertThat(mapperImpl.toEconomyContentDto(mapperImpl.toEconomyContent(EconomyContentDto))).usingRecursiveComparison().isEqualTo(EconomyContentDto);
    }
}