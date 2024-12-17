package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;
import site.hixview.support.spring.util.SecondCategoryTestUtils;
import site.hixview.support.spring.util.dto.SecondCategoryDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class SecondCategoryMapperTest implements SecondCategoryTestUtils, SecondCategoryDtoTestUtils {

    private final SecondCategoryMapper mapperImpl = new SecondCategoryMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 SecondCategory 일관성 보장")
    @Test
    void secondCategoryMappingWithDomainMapper() {
        assertThat(mapperImpl.toSecondCategory(mapperImpl.toSecondCategoryDto(secondCategory))).isEqualTo(secondCategory);
        assertThat(mapperImpl.toSecondCategory(mapperImpl.toSecondCategoryDtoNoNumber(secondCategory)))
                .usingRecursiveComparison().ignoringFields(NUMBER).isEqualTo(secondCategory);
    }

    @DisplayName("도메인 매퍼 사용 후 SecondCategoryDto 일관성 보장")
    @Test
    void secondCategoryDtoMappingWithDomainMapper() {
        SecondCategoryDto secondCategoryDto = createSecondCategoryDto();
        assertThat(mapperImpl.toSecondCategoryDto(mapperImpl.toSecondCategory(secondCategoryDto))).usingRecursiveComparison().isEqualTo(secondCategoryDto);
    }

    @DisplayName("도메인 매퍼 사용 후 SecondCategoryDtoNoNumber 일관성 보장")
    @Test
    void secondCategoryDtoNoNumberMappingWithDomainMapper() {
        SecondCategoryDtoNoNumber secondCategoryDto = createSecondCategoryDtoNoNumber();
        assertThat(mapperImpl.toSecondCategoryDtoNoNumber(mapperImpl.toSecondCategory(secondCategoryDto))).usingRecursiveComparison().isEqualTo(secondCategoryDto);
    }
}