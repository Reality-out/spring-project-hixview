package springsideproject1.springsideproject1build.domain.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.repository.MemberRepository;
import springsideproject1.springsideproject1build.utility.test.MemberTestUtility;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberTest implements MemberTestUtility {

    @Autowired
    MemberRepository memberRepository;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
    }

    @DisplayName("대시가 있는 버전과 없는 버전의 전화번호를 사용하는 회원 저장")
    @Test
    public void saveMemberWithVariousPhoneNumber() {
        // given
        Member member1 = createTestMember();
        Member member2 = Member.builder().member(createTestNewMember()).phoneNumber("01023456789").build();
        
        // when
        member1 = Member.builder().member(member1).identifier(memberRepository.saveMember(member1)).build();
        member2 = Member.builder().member(member2).identifier(memberRepository.saveMember(member2)).build();

        // then
        assertThat(memberRepository.getMemberByID(member1.getId()).orElseThrow()).usingRecursiveComparison().isEqualTo(member1);
        assertThat(memberRepository.getMemberByID(member2.getId()).orElseThrow()).usingRecursiveComparison().isEqualTo(member2);
    }
}