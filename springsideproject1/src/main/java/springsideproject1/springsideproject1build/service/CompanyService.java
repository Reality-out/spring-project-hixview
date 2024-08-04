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
import static springsideproject1.springsideproject1build.utility.MainUtility.isNumeric;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    /**
     * SELECT Company
     */
    public List<Company> findCompanies() {
        return companyRepository.getCompanies();
    }

    public Optional<Company> findCompanyByCode(String code) {
        return companyRepository.getCompanyByCode(code);
    }

    public Optional<Company> findCompanyByName(String name) {
        return companyRepository.getCompanyByName(name);
    }

    public Optional<Company> findCompanyByCodeOrName(String nameOrCode) {
        return isNumeric(nameOrCode) ? findCompanyByCode(nameOrCode) : findCompanyByName(nameOrCode);
    }

    /**
     * INSERT Company
     */
    @Transactional
    public void joinCompanies(Company... articles) {
        for (Company article : articles) {
            joinCompany(article);
        }
    }

    @Transactional
    public void joinCompany(Company company) {
        duplicateCheck(company);
        companyRepository.saveCompany(company);
    }

    /**
     * UPDATE Company
     */
    @Transactional
    public void renewCompany(Company company) {
        existentCheck(company.getCode());
        companyRepository.updateCompany(company);
    }

    /**
     * REMOVE Company
     */
    @Transactional
    public void removeCompany(String code) {
        existentCheck(code);
        companyRepository.deleteCompanyByCode(code);
    }

    /**
     * Other private methods
     */
    @Transactional
    private void duplicateCheck(Company company) {
        companyRepository.getCompanyByCode(company.getCode()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_COMPANY_CODE);}
        );
    }
    @Transactional
    private void existentCheck(String code) {
        companyRepository.getCompanyByCode(code).orElseThrow(
                () -> new IllegalStateException(NO_COMPANY_WITH_THAT_CODE)
        );
    }
}
