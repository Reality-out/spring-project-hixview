package springsideproject1.springsideproject1production;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springsideproject1.springsideproject1production.repository.*;
import springsideproject1.springsideproject1production.service.CompanyService;
import springsideproject1.springsideproject1production.service.MemberService;

import javax.sql.DataSource;

@Configuration
public class Project1Config {

    private final DataSource dataSource;

    public Project1Config(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Member Bean
     */
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
//        return new MemberRepositoryInMemory();
        return new MemberRepositoryJdbc(dataSource);
    }

    /**
     * Company Bean
     */
    @Bean
    public CompanyService companyService() {
        return new CompanyService(companyRepository());
    }

    @Bean
    public CompanyRepository companyRepository() {
        return new CompanyRepositoryJdbc(dataSource);
    }
}
