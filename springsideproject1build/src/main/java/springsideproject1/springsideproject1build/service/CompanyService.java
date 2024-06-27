package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * SELECT One Company
     */
    public List<Company> findCompanies() {
        return companyRepository.findAllCompanies();
    }

    public Optional<Company> SearchOneCompanyByCode(String companyCode) {
        return companyRepository.searchCompanyByCode(companyCode);
    }

    public Optional<Company> SearchOneCompanyByName(String companyName) {
        return companyRepository.searchCompanyByName(companyName);
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
    public void removeCompany(String companyCode) {
        companyRepository.searchCompanyByCode(companyCode).orElseThrow(
                () -> new IllegalStateException("해당 코드 번호와 일치하는 기업이 없습니다.")
        );

        companyRepository.removeCompanyByCode(companyCode);
    }

    @Transactional
    private void DuplicateCodeCheck(Company company) {
        companyRepository.searchCompanyByCode(company.getCode()).ifPresent(
                v -> {throw new IllegalStateException("이미 존재하는 코드 번호입니다.");}
        );
    }
}
