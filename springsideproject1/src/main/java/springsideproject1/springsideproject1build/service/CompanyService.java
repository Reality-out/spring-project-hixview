package springsideproject1.springsideproject1build.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;

import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.ALREADY_EXIST_COMPANY_CODE;
import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.NO_COMPANY_WITH_THAT_CODE;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * SELECT Companies
     */
    public List<Company> findCompanies() {
        return companyRepository.findAllCompanies();
    }

    /**
     * SELECT One Company
     */
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
        duplicateCheck(company);
        companyRepository.saveCompany(company);
    }

    /**
     * REMOVE One Member
     */
    @Transactional
    public void removeCompany(String companyCode) {
        existentCheck(companyCode);

        companyRepository.removeCompanyByCode(companyCode);
    }

    /**
     * Other private methods
     */
    @Transactional
    private void duplicateCheck(Company company) {
        companyRepository.searchCompanyByCode(company.getCode()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_COMPANY_CODE);}
        );
    }
    @Transactional
    private void existentCheck(String companyCode) {
        companyRepository.searchCompanyByCode(companyCode).orElseThrow(
                () -> new IllegalStateException(NO_COMPANY_WITH_THAT_CODE)
        );
    }
}
