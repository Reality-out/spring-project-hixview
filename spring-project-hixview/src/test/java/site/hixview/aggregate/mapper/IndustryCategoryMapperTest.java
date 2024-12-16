package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;
import site.hixview.support.spring.util.IndustryCategoryTestUtils;
import site.hixview.support.spring.util.dto.IndustryCategoryDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class IndustryCategoryMapperTest implements IndustryCategoryTestUtils, IndustryCategoryDtoTestUtils {

    private final IndustryCategoryMapperImpl mapperImpl = new IndustryCategoryMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 IndustryCategory 일관성 보장")
    @Test
    void industryCategoryMappingWithDomainMapper() {
        assertThat(mapperImpl.toIndustryCategory(mapperImpl.toIndustryCategoryDto(industryCategory))).isEqualTo(industryCategory);
        assertThat(mapperImpl.toIndustryCategory(mapperImpl.toIndustryCategoryDtoNoNumber(industryCategory)))
                .usingRecursiveComparison().ignoringFields(NUMBER).isEqualTo(industryCategory);
    }

    @DisplayName("도메인 매퍼 사용 후 IndustryCategoryDto 일관성 보장")
    @Test
    void industryCategoryDtoMappingWithDomainMapper() {
        IndustryCategoryDto IndustryCategoryDto = createIndustryCategoryDto();
        assertThat(mapperImpl.toIndustryCategoryDto(mapperImpl.toIndustryCategory(IndustryCategoryDto))).usingRecursiveComparison().isEqualTo(IndustryCategoryDto);
    }

    @DisplayName("도메인 매퍼 사용 후 IndustryCategoryDtoNoNumber 일관성 보장")
    @Test
    void industryCategoryDtoNoNumberMappingWithDomainMapper() {
        IndustryCategoryDtoNoNumber IndustryCategoryDto = createIndustryCategoryDtoNoNumber();
        assertThat(mapperImpl.toIndustryCategoryDtoNoNumber(mapperImpl.toIndustryCategory(IndustryCategoryDto))).usingRecursiveComparison().isEqualTo(IndustryCategoryDto);
    }
}