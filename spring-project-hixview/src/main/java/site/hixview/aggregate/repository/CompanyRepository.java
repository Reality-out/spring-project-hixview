package site.hixview.aggregate.repository;

import site.hixview.aggregate.domain.Company;

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
     * UPDATE Company
     */
    void updateCompany(Company company);

    /**
     * REMOVE Company
     */
    void deleteCompanyByCode(String code);
}