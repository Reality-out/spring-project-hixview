package springsideproject1.springsideproject1build;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springsideproject1.springsideproject1build.repository.CompanyRepository;
import springsideproject1.springsideproject1build.repository.CompanyRepositoryJdbc;
import springsideproject1.springsideproject1build.repository.MemberRepository;
import springsideproject1.springsideproject1build.repository.MemberRepositoryJdbc;

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
    public MemberRepository memberRepository() {
        return new MemberRepositoryJdbc(dataSource);
    }

    /**
     * Company Bean
     */
    @Bean
    public CompanyRepository companyRepository() {
        return new CompanyRepositoryJdbc(dataSource);
    }
}
