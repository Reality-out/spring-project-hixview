package springsideproject1.springsideproject1build;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springsideproject1.springsideproject1build.repository.*;
import springsideproject1.springsideproject1build.service.CompanyService;
import springsideproject1.springsideproject1build.service.MemberService;

import javax.sql.DataSource;

@Configuration
public class Project1Config {

    /**
     * Member Bean
     */
    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepositoryInMemory();
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
        return new CompanyRepositoryInMemory();
    }
}
