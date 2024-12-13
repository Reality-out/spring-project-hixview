package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.support.spring.util.SecondCategoryTestUtils;
import site.hixview.support.spring.util.dto.SecondCategoryDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class SecondCategoryMapperTest implements SecondCategoryTestUtils, SecondCategoryDtoTestUtils {

    private final SecondCategoryMapperImpl mapperImpl = new SecondCategoryMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(SecondCategoryMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 SecondCategory 일관성 보장")
    @Test
    void secondCategoryMappingWithDomainMapper() {
        assertThat(mapperImpl.toSecondCategory(mapperImpl.toSecondCategoryDto(secondCategory))).isEqualTo(secondCategory);
    }

    @DisplayName("도메인 매퍼 사용 후 SecondCategoryDto 일관성 보장")
    @Test
    void secondCategoryDtoMappingWithDomainMapper() {
        SecondCategoryDto secondCategoryDto = createSecondCategoryDto();
        assertThat(mapperImpl.toSecondCategoryDto(mapperImpl.toSecondCategory(secondCategoryDto))).usingRecursiveComparison().isEqualTo(secondCategoryDto);
    }
}