package site.hixview.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.domain.entity.company.Company;
import site.hixview.domain.repository.CompanyRepository;
import site.hixview.support.context.OnlyRealServiceContext;
import site.hixview.support.util.CompanyArticleTestUtils;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static site.hixview.domain.vo.ExceptionMessage.ALREADY_EXIST_COMPANY_CODE;
import static site.hixview.domain.vo.ExceptionMessage.NO_COMPANY_WITH_THAT_CODE;
import static site.hixview.support.util.CompanyTestUtils.samsungElectronics;
import static site.hixview.support.util.CompanyTestUtils.skHynix;

@OnlyRealServiceContext
class CompanyServiceTest implements CompanyArticleTestUtils {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @DisplayName("기업 코드와 이름으로 기업 찾기")
    @Test
    void findCompanyWithCodeAndNameTest() {
        // given
        Company company = samsungElectronics;
        when(companyRepository.getCompanyByCode(company.getCode()))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(company));
        when(companyRepository.getCompanyByName(company.getName())).thenReturn(Optional.of(company));
        doNothing().when(companyRepository).saveCompany(company);

        // when
        companyService.registerCompany(company);

        // then
        for (String str : List.of(company.getCode(), company.getName())) {
            assertThat(companyService.findCompanyByCodeOrName(str).orElseThrow())
                    .usingRecursiveComparison()
                    .isEqualTo(company);
        }
    }

    @DisplayName("기업 등록")
    @Test
    void registerCompanyTest() {
        // given
        Company company = samsungElectronics;
        when(companyRepository.getCompanies()).thenReturn(List.of(company));
        when(companyRepository.getCompanyByCode(company.getCode())).thenReturn(Optional.empty());
        doNothing().when(companyRepository).saveCompany(company);

        // when
        companyService.registerCompany(company);

        // then
        assertThat(companyService.findCompanies()).usingRecursiveComparison().isEqualTo(List.of(company));
    }

    @DisplayName("기업들 등록")
    @Test
    void registerCompaniesTest() {
        // given
        Company firstCompany = samsungElectronics;
        Company secondCompany = skHynix;
        when(companyRepository.getCompanies()).thenReturn(List.of(firstCompany, secondCompany));
        when(companyRepository.getCompanyByCode(firstCompany.getCode())).thenReturn(Optional.empty());
        when(companyRepository.getCompanyByCode(secondCompany.getCode())).thenReturn(Optional.empty());
        doNothing().when(companyRepository).saveCompany(firstCompany);
        doNothing().when(companyRepository).saveCompany(secondCompany);

        // when
        companyService.registerCompanies(firstCompany, secondCompany);

        // then
        assertThat(companyService.findCompanies())
                .usingRecursiveComparison().isEqualTo(List.of(firstCompany, secondCompany));
    }

    @DisplayName("기업 중복 코드로 등록")
    @Test
    void registerDuplicatedCompanyWithSameCodeTest() {
        // given
        Company company = samsungElectronics;
        String duplicatedCode = company.getCode();
        when(companyRepository.getCompanyByCode(duplicatedCode))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(company));
        doNothing().when(companyRepository).saveCompany(company);

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.registerCompanies(company,
                        Company.builder().company(skHynix).code(duplicatedCode).build()));

        // then
        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_COMPANY_CODE);
    }

    @DisplayName("기업 존재하지 않는 코드로 수정")
    @Test
    void correctCompanyByFaultCodeTest() {
        // given
        when(companyRepository.getCompanyByCode(samsungElectronics.getCode())).thenReturn(Optional.empty());

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.correctCompany(samsungElectronics));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_COMPANY_WITH_THAT_CODE);
    }

    @DisplayName("기업 제거")
    @Test
    void removeCompanyTest() {
        // given
        Company company = samsungElectronics;
        String code = company.getCode();
        when(companyRepository.getCompanies()).thenReturn(emptyList());
        when(companyRepository.getCompanyByCode(code))
                .thenReturn(Optional.empty()).thenReturn(Optional.of(company));
        doNothing().when(companyRepository).saveCompany(company);
        doNothing().when(companyRepository).deleteCompanyByCode(code);

        // when
        companyService.registerCompany(company);
        companyService.removeCompanyByCode(code);

        // then
        assertThat(companyService.findCompanies()).isEmpty();
    }

    @DisplayName("기업 존재하지 않는 코드로 제거")
    @Test
    void removeCompanyByFaultCodeTest() {
        // given
        when(companyRepository.getCompanyByCode(any())).thenReturn(Optional.empty());

        // when
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> companyService.removeCompanyByCode(INVALID_VALUE));

        // then
        assertThat(e.getMessage()).isEqualTo(NO_COMPANY_WITH_THAT_CODE);
    }
}