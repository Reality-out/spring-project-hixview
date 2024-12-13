package site.hixview.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.SiteMemberEntityTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.SITE_MEMBER_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class SiteMemberEntityRepositoryTest implements SiteMemberEntityTestUtils {

    @Autowired
    private SiteMemberEntityRepository siteMemberEntityRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + SITE_MEMBER_SNAKE};

    private static final Logger log = LoggerFactory.getLogger(SiteMemberEntityRepositoryTest.class);

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("이름으로 사이트 회원 찾기")
    @Test
    void findByNameTest() {
        // given
        SiteMemberEntity member = createSiteMemberEntity();

        // when
        siteMemberEntityRepository.save(member);

        // then
        assertThat(siteMemberEntityRepository.findByName(member.getName())).isEqualTo(List.of(member));
    }

    @DisplayName("번호로 사이트 회원 찾기")
    @Test
    void findByNumberTest() {
        // given
        SiteMemberEntity member = createSiteMemberEntity();

        // when
        siteMemberEntityRepository.save(member);

        // then
        assertThat(siteMemberEntityRepository.findByNumber(member.getNumber()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("ID로 사이트 회원 찾기")
    @Test
    void findByIdTest() {
        // given
        SiteMemberEntity member = createSiteMemberEntity();

        // when
        siteMemberEntityRepository.save(member);

        // then
        assertThat(siteMemberEntityRepository.findById(member.getId()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("ID와 PW로 사이트 회원 찾기")
    @Test
    void findByIdAndPwTest() {
        // given
        SiteMemberEntity member = createSiteMemberEntity();

        // when
        siteMemberEntityRepository.save(member);

        // then
        assertThat(siteMemberEntityRepository.findByIdAndPw(member.getId(), member.getPw()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("이메일로 사이트 회원 찾기")
    @Test
    void findByEmailTest() {
        // given
        SiteMemberEntity member = createSiteMemberEntity();

        // when
        siteMemberEntityRepository.save(member);

        // then
        assertThat(siteMemberEntityRepository.findByEmail(member.getEmail()).orElseThrow()).isEqualTo(member);
    }

    @DisplayName("번호로 사이트 회원 삭제")
    @Test
    void deleteByNumberTest() {
        // given
        SiteMemberEntity member = siteMemberEntityRepository.save(createSiteMemberEntity());

        // when
        siteMemberEntityRepository.deleteByNumber(member.getNumber());

        // then
        assertThat(siteMemberEntityRepository.findAll()).isEmpty();
    }

    @DisplayName("번호로 사이트 회원 확인")
    @Test
    void existsByNumberTest() {
        // given
        SiteMemberEntity member = createSiteMemberEntity();

        // when
        siteMemberEntityRepository.save(member);

        // then
        assertThat(siteMemberEntityRepository.existsByNumber(member.getNumber())).isEqualTo(true);
    }
}