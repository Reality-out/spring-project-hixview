package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;
import site.hixview.support.spring.util.CompanyArticleTestUtils;
import site.hixview.support.spring.util.dto.CompanyArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class CompanyArticleMapperTest implements CompanyArticleTestUtils, CompanyArticleDtoTestUtils {

    private final CompanyArticleMapper mapperImpl = new CompanyArticleMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 CompanyArticle 일관성 보장")
    @Test
    void companyArticleMappingWithDomainMapper() {
        assertThat(mapperImpl.toCompanyArticle(mapperImpl.toCompanyArticleDto(companyArticle))).isEqualTo(companyArticle);
        assertThat(mapperImpl.toCompanyArticle(mapperImpl.toCompanyArticleDtoNoNumber(companyArticle)))
                .usingRecursiveComparison().ignoringFields(NUMBER).isEqualTo(companyArticle);
    }

    @DisplayName("도메인 매퍼 사용 후 CompanyArticleDto 일관성 보장")
    @Test
    void companyArticleDtoMappingWithDomainMapper() {
        CompanyArticleDto CompanyArticleDto = createCompanyArticleDto();
        assertThat(mapperImpl.toCompanyArticleDto(mapperImpl.toCompanyArticle(CompanyArticleDto))).usingRecursiveComparison().isEqualTo(CompanyArticleDto);
    }

    @DisplayName("도메인 매퍼 사용 후 CompanyArticleDtoNoNumber 일관성 보장")
    @Test
    void companyArticleDtoNoNumberMappingWithDomainMapper() {
        CompanyArticleDtoNoNumber CompanyArticleDto = createCompanyArticleDtoNoNumber();
        assertThat(mapperImpl.toCompanyArticleDtoNoNumber(mapperImpl.toCompanyArticle(CompanyArticleDto))).usingRecursiveComparison().isEqualTo(CompanyArticleDto);
    }
}