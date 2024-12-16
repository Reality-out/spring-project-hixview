package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.CompanyDto;
import site.hixview.support.spring.util.CompanyTestUtils;
import site.hixview.support.spring.util.dto.CompanyDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class CompanyMapperTest implements CompanyTestUtils, CompanyDtoTestUtils {

    private final CompanyMapperImpl mapperImpl = new CompanyMapperImpl();

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