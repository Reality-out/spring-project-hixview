package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.CompanyArticleCompanyDto;
import site.hixview.support.spring.util.CompanyArticleCompanyTestUtils;
import site.hixview.support.spring.util.dto.CompanyArticleCompanyDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class CompanyArticleCompanyMapperTest implements CompanyArticleCompanyTestUtils, CompanyArticleCompanyDtoTestUtils {

    private final CompanyArticleCompanyMapper mapperImpl = new CompanyArticleCompanyMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 CompanyArticleCompany 일관성 보장")
    @Test
    void companyArticleCompanyMappingWithDomainMapper() {
        assertThat(mapperImpl.toCompanyArticleCompany(mapperImpl.toCompanyArticleCompanyDto(companyArticleCompany))).isEqualTo(companyArticleCompany);
    }

    @DisplayName("도메인 매퍼 사용 후 CompanyArticleCompanyDto 일관성 보장")
    @Test
    void companyArticleCompanyDtoMappingWithDomainMapper() {
        CompanyArticleCompanyDto CompanyArticleCompanyDto = createCompanyArticleCompanyDto();
        assertThat(mapperImpl.toCompanyArticleCompanyDto(mapperImpl.toCompanyArticleCompany(CompanyArticleCompanyDto))).usingRecursiveComparison().isEqualTo(CompanyArticleCompanyDto);
    }
}