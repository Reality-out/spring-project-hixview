package springsideproject1.springsideproject1build.database;

import springsideproject1.springsideproject1build.domain.Company;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


public class CompanyDatabase {

    private static Map<AtomicLong, Company> companyHashMap = new ConcurrentHashMap<>();

    public static Map<AtomicLong, Company> getCompanyHashMap() {
        return companyHashMap;
    }
}
