package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.company.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository {
    /**
     * SELECT Company
     */
    List<Company> selectCompanies();

    Optional<Company> selectCompanyByCode(String code);

    Optional<Company> selectCompanyByName(String name);

    /**
     * INSERT Company
     */
    void insertCompany(Company company);

    /**
     * UPDATE Company
     */
    void updateCompany(Company company);

    /**
     * REMOVE Company
     */
    void deleteCompanyByCode(String code);
}
