package springsideproject1.springsideproject1build.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springsideproject1.springsideproject1build.repository.*;

import javax.sql.DataSource;

@Configuration
public class BeanConfig {

    private final DataSource dataSource;

    public BeanConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * CompanyArticle Bean
     */
    @Bean
    public CompanyArticleRepository articleRepository() {
        return new CompanyArticleRepositoryJdbc(dataSource);
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
