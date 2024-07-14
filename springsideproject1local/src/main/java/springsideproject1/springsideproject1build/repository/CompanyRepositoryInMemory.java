package springsideproject1.springsideproject1build.repository;

import org.springframework.stereotype.Repository;
import springsideproject1.springsideproject1build.domain.Company;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static springsideproject1.springsideproject1build.database.CompanyDatabase.getCompanyHashMap;

@Repository
public class CompanyRepositoryInMemory implements CompanyRepository {

    /**
     * SELECT Data
     */
    @Override
    public Optional<Company> searchCompanyByCode(Long code) {
        return Optional.ofNullable(getCompanyHashMap().values().stream()
                .filter(company -> company.getCode().equals(code))
                .findAny().get());
    }

    @Override
    public Optional<Company> searchCompanyByName(String name) {
        return Optional.ofNullable(getCompanyHashMap().values().stream()
                .filter(company -> company.getCode().equals(name))
                .findAny().get());
    }

    /**
     * INSERT Data
     */
    @Override
    public void saveCompany(Company company) {
        getCompanyHashMap().put(new AtomicLong(company.getCode()), company);
    }

    /**
     * REMOVE Data
     */
    @Override
    public void removeCompanyByCode(Long code) {
        getCompanyHashMap().remove(getCompanyHashMap().keySet().stream()
                .filter(companyCode -> companyCode.equals(code))
                .findAny().get());
    }
}
