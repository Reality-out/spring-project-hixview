package springsideproject1.springsideproject1build;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springsideproject1.springsideproject1build.repository.*;

import javax.sql.DataSource;

@Configuration
public class Project1Config {

    private final DataSource dataSource;

    public Project1Config(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Article Bean
     */
    @Bean
    public ArticleRepository articleRepository() {
        return new ArticleRepositoryJdbc(dataSource);
    }

    /**
     * Company Bean
     */
    @Bean
    public CompanyRepository companyRepository() {
        return new CompanyRepositoryJdbc(dataSource);
    }

    /**
     * Member Bean
     */
    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepositoryJdbc(dataSource);
    }
}
