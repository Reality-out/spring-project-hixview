package springsideproject1.springsideproject1build.repository;

import springsideproject1.springsideproject1build.domain.Company;

import java.util.Optional;

public class CompanyRepositoryInMemory implements CompanyRepository {

    @Override
    public Optional<Company> searchCompanyByCode(Long code) {

    }

    @Override
    public Optional<Company> searchCompanyByName(String name) {
        return Optional.empty();
    }

    @Override
    public void saveCompany(Company company) {

    }

    @Override
    public void removeCompanyByCode(Long code) {

    }
}
