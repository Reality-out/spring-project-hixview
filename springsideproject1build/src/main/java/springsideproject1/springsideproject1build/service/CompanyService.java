package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.repository.CompanyRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * SELECT One Company
     */
    public Optional<Company> SearchOneCompanyByCode(Long code) {
        return companyRepository.searchCompanyByCode(code);
    }

    public Optional<Company> SearchOneCompanyByName(String name) {
        return companyRepository.searchCompanyByName(name);
    }

    /**
     * INSERT One Member
     */
    @Transactional
    public void joinCompany(Company company) {
        DuplicateCodeCheck(company);
        companyRepository.saveCompany(company);
    }

    /**
     * REMOVE One Member
     */
    @Transactional
    public void removeCompany(Long companyCode) {
        companyRepository.searchCompanyByCode(companyCode).orElseThrow(
                () -> new IllegalStateException("해당 ID와 일치하는 멤버가 없습니다."));

        companyRepository.removeCompanyByCode(companyCode);
    }

    private void DuplicateCodeCheck(Company company) {
        companyRepository.searchCompanyByCode(company.getCode()).ifPresent(
                v -> {throw new IllegalStateException("이미 존재하는 코드 번호입니다.");}
        );
    }
}
