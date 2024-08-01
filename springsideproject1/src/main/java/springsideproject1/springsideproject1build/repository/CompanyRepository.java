package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    /**
     * SELECT Company
     */
    List<Company> getCompanies();

    Optional<Company> getCompanyByCode(String code);

    Optional<Company> getCompanyByName(String name);

    /**
     * INSERT Company
     */
    void saveCompany(Company company);

    /**
     * REMOVE Company
     */
    void deleteCompanyByCode(String code);
}
