package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;
import site.hixview.support.spring.util.IndustryArticleSecondCategoryTestUtils;
import site.hixview.support.spring.util.dto.IndustryArticleSecondCategoryDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class IndustryArticleSecondCategoryMapperTest implements IndustryArticleSecondCategoryTestUtils, IndustryArticleSecondCategoryDtoTestUtils {

    private final IndustryArticleSecondCategoryMapperImpl mapperImpl = new IndustryArticleSecondCategoryMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 IndustryArticleSecondCategory 일관성 보장")
    @Test
    void industryArticleSecondCategoryMappingWithDomainMapper() {
        assertThat(mapperImpl.toIndustryArticleSecondCategory(mapperImpl.toIndustryArticleSecondCategoryDto(industryArticleSecondCategory))).isEqualTo(industryArticleSecondCategory);
    }

    @DisplayName("도메인 매퍼 사용 후 IndustryArticleSecondCategoryDto 일관성 보장")
    @Test
    void industryArticleSecondCategoryDtoMappingWithDomainMapper() {
        IndustryArticleSecondCategoryDto IndustryArticleSecondCategoryDto = createIndustryArticleSecondCategoryDto();
        assertThat(mapperImpl.toIndustryArticleSecondCategoryDto(mapperImpl.toIndustryArticleSecondCategory(IndustryArticleSecondCategoryDto))).usingRecursiveComparison().isEqualTo(IndustryArticleSecondCategoryDto);
    }
}