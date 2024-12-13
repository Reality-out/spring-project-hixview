package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.support.spring.util.CompanyArticleTestUtils;
import site.hixview.support.spring.util.dto.CompanyArticleDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class CompanyArticleMapperTest implements CompanyArticleTestUtils, CompanyArticleDtoTestUtils {

    private final CompanyArticleMapperImpl mapperImpl = new CompanyArticleMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(CompanyArticleMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 CompanyArticle 일관성 보장")
    @Test
    void companyArticleMappingWithDomainMapper() {
        assertThat(mapperImpl.toCompanyArticle(mapperImpl.toCompanyArticleDto(companyArticle))).isEqualTo(companyArticle);
    }

    @DisplayName("도메인 매퍼 사용 후 CompanyArticleDto 일관성 보장")
    @Test
    void companyArticleDtoMappingWithDomainMapper() {
        CompanyArticleDto CompanyArticleDto = createCompanyArticleDto();
        assertThat(mapperImpl.toCompanyArticleDto(mapperImpl.toCompanyArticle(CompanyArticleDto))).usingRecursiveComparison().isEqualTo(CompanyArticleDto);
    }
}