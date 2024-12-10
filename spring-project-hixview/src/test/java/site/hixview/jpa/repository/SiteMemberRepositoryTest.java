package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.support.context.OnlyRealRepositoryContext;
import site.hixview.support.executor.SqlExecutor;
import site.hixview.support.jpa.util.SiteMemberTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@OnlyRealRepositoryContext
class SiteMemberRepositoryTest implements SiteMemberTestUtils {

    @Autowired
    private SiteMemberRepository siteMemberRepository;

    private static final Logger log = LoggerFactory.getLogger(SiteMemberRepositoryTest.class);

    @BeforeAll
    public static void beforeAll(@Autowired ApplicationContext applicationContext) {
        new SqlExecutor().resetAutoIncrement(applicationContext);
    }

    @DisplayName("이름으로 사이트 회원 찾기")
    @Test
    void findByNameTest() {
        // given
        SiteMemberEntity member = createSiteMember();

        // when
        siteMemberRepository.save(member);

        // then
        assertThat(siteMemberRepository.findByName(member.getName())).isEqualTo(List.of(member));
    }

    @DisplayName("번호로 사이트 회원 찾기")
    @Test
    void findByNumberTest() {
        // given
        SiteMemberEntity member = createSiteMember();

        // when
        siteMemberRepository.save(member);

        // then
        assertThat(siteMemberRepository.findByNumber(member.getNumber()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("ID로 사이트 회원 찾기")
    @Test
    void findByIdTest() {
        // given
        SiteMemberEntity member = createSiteMember();

        // when
        siteMemberRepository.save(member);

        // then
        assertThat(siteMemberRepository.findById(member.getId()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("ID와 PW로 사이트 회원 찾기")
    @Test
    void findByIdAndPwTest() {
        // given
        SiteMemberEntity member = createSiteMember();

        // when
        siteMemberRepository.save(member);

        // then
        assertThat(siteMemberRepository.findByIdAndPw(member.getId(), member.getPw()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("이메일로 사이트 회원 찾기")
    @Test
    void findByEmailTest() {
        // given
        SiteMemberEntity member = createSiteMember();

        // when
        siteMemberRepository.save(member);

        // then
        assertThat(siteMemberRepository.findByEmail(member.getEmail()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("번호로 사이트 회원 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        SiteMemberEntity member = siteMemberRepository.save(createSiteMember());

        // when
        siteMemberRepository.deleteByNumber(member.getNumber());

        // then
        assertThat(siteMemberRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 사이트 회원 확인")
    @Test
    void existsByNumberTest() {
        // given
        SiteMemberEntity member = createSiteMember();

        // when
        siteMemberRepository.save(member);

        // then
        assertThat(siteMemberRepository.existsByNumber(member.getNumber())).isEqualTo(true);
    }
}