package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.IndustryArticleSecondCategoryDto;
import site.hixview.support.spring.util.IndustryArticleSecondCategoryTestUtils;
import site.hixview.support.spring.util.dto.IndustryArticleSecondCategoryDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class IndustryArticleSecondCategoryMapperTest implements IndustryArticleSecondCategoryTestUtils, IndustryArticleSecondCategoryDtoTestUtils {

    private final IndustryArticleSecondCategoryMapperImpl mapperImpl = new IndustryArticleSecondCategoryMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(IndustryArticleSecondCategoryMapperTest.class);

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