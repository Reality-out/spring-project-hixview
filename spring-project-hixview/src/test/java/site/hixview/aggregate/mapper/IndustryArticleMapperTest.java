package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
import site.hixview.support.spring.util.IndustryArticleTestUtils;
import site.hixview.support.spring.util.dto.IndustryArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class IndustryArticleMapperTest implements IndustryArticleTestUtils, IndustryArticleDtoTestUtils {

    private final IndustryArticleMapper mapperImpl = new IndustryArticleMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 IndustryArticle 일관성 보장")
    @Test
    void industryArticleMappingWithDomainMapper() {
        assertThat(mapperImpl.toIndustryArticle(mapperImpl.toIndustryArticleDto(industryArticle))).isEqualTo(industryArticle);
        assertThat(mapperImpl.toIndustryArticle(mapperImpl.toIndustryArticleDtoNoNumber(industryArticle)))
                .usingRecursiveComparison().ignoringFields(NUMBER).isEqualTo(industryArticle);
    }

    @DisplayName("도메인 매퍼 사용 후 IndustryArticleDto 일관성 보장")
    @Test
    void industryArticleDtoMappingWithDomainMapper() {
        IndustryArticleDto IndustryArticleDto = createIndustryArticleDto();
        assertThat(mapperImpl.toIndustryArticleDto(mapperImpl.toIndustryArticle(IndustryArticleDto))).usingRecursiveComparison().isEqualTo(IndustryArticleDto);
    }

    @DisplayName("도메인 매퍼 사용 후 IndustryArticleDtoNoNumber 일관성 보장")
    @Test
    void industryArticleDtoNoNumberMappingWithDomainMapper() {
        IndustryArticleDtoNoNumber IndustryArticleDto = createIndustryArticleDtoNoNumber();
        assertThat(mapperImpl.toIndustryArticleDtoNoNumber(mapperImpl.toIndustryArticle(IndustryArticleDto))).usingRecursiveComparison().isEqualTo(IndustryArticleDto);
    }
}