package springsideproject1.springsideproject1production;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springsideproject1.springsideproject1production.repository.MemberRepository;
import springsideproject1.springsideproject1production.repository.MemberRepositoryJdbc;
import springsideproject1.springsideproject1production.service.MemberService;

import javax.sql.DataSource;

@Configuration
public class Project1Config {

    private final DataSource dataSource;

    public Project1Config(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
//        return new MemberRepositoryInMemory();
        return new MemberRepositoryJdbc(dataSource);
    }
}
