package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.support.spring.util.IndustryCategoryTestUtils;
import site.hixview.support.spring.util.dto.IndustryCategoryDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class IndustryCategoryMapperTest implements IndustryCategoryTestUtils, IndustryCategoryDtoTestUtils {

    private final IndustryCategoryMapperImpl mapperImpl = new IndustryCategoryMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(IndustryCategoryMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 IndustryCategory 일관성 보장")
    @Test
    void industryCategoryMappingWithDomainMapper() {
        assertThat(mapperImpl.toIndustryCategory(mapperImpl.toIndustryCategoryDto(industryCategory))).isEqualTo(industryCategory);
    }

    @DisplayName("도메인 매퍼 사용 후 IndustryCategoryDto 일관성 보장")
    @Test
    void industryCategoryDtoMappingWithDomainMapper() {
        IndustryCategoryDto IndustryCategoryDto = createIndustryCategoryDto();
        assertThat(mapperImpl.toIndustryCategoryDto(mapperImpl.toIndustryCategory(IndustryCategoryDto))).usingRecursiveComparison().isEqualTo(IndustryCategoryDto);
    }
}