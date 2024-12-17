package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;
import site.hixview.support.spring.util.FirstCategoryTestUtils;
import site.hixview.support.spring.util.dto.FirstCategoryDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class FirstCategoryMapperTest implements FirstCategoryTestUtils, FirstCategoryDtoTestUtils {

    private final FirstCategoryMapper mapperImpl = new FirstCategoryMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 FirstCategory 일관성 보장")
    @Test
    void firstCategoryMappingWithDomainMapper() {
        assertThat(mapperImpl.toFirstCategory(mapperImpl.toFirstCategoryDto(firstCategory))).isEqualTo(firstCategory);
        assertThat(mapperImpl.toFirstCategory(mapperImpl.toFirstCategoryDtoNoNumber(firstCategory)))
                .usingRecursiveComparison().ignoringFields(NUMBER).isEqualTo(firstCategory);
    }

    @DisplayName("도메인 매퍼 사용 후 FirstCategoryDto 일관성 보장")
    @Test
    void firstCategoryDtoMappingWithDomainMapper() {
        FirstCategoryDto FirstCategoryDto = createFirstCategoryDto();
        assertThat(mapperImpl.toFirstCategoryDto(mapperImpl.toFirstCategory(FirstCategoryDto))).usingRecursiveComparison().isEqualTo(FirstCategoryDto);
    }

    @DisplayName("도메인 매퍼 사용 후 FirstCategoryDtoNoNumber 일관성 보장")
    @Test
    void firstCategoryDtoNoNumberMappingWithDomainMapper() {
        FirstCategoryDtoNoNumber FirstCategoryDto = createFirstCategoryDtoNoNumber();
        assertThat(mapperImpl.toFirstCategoryDtoNoNumber(mapperImpl.toFirstCategory(FirstCategoryDto))).usingRecursiveComparison().isEqualTo(FirstCategoryDto);
    }
}