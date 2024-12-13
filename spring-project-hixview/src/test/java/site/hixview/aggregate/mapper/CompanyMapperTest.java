package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.support.spring.util.CompanyTestUtils;
import site.hixview.support.spring.util.dto.CompanyDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class CompanyMapperTest implements CompanyTestUtils, CompanyDtoTestUtils {

    private final CompanyMapperImpl mapperImpl = new CompanyMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(CompanyMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 Company 일관성 보장")
    @Test
    void companyMappingWithDomainMapper() {
        assertThat(mapperImpl.toCompany(mapperImpl.toCompanyDto(company))).isEqualTo(company);
    }

    @DisplayName("도메인 매퍼 사용 후 CompanyDto 일관성 보장")
    @Test
    void companyDtoMappingWithDomainMapper() {
        CompanyDto CompanyDto = createCompanyDto();
        assertThat(mapperImpl.toCompanyDto(mapperImpl.toCompany(CompanyDto))).usingRecursiveComparison().isEqualTo(CompanyDto);
    }
}