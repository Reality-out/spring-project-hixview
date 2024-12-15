package site.hixview.jpa.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import site.hixview.aggregate.domain.SiteMember;
import site.hixview.jpa.entity.SiteMemberEntity;
import site.hixview.jpa.repository.SiteMemberEntityRepository;
import site.hixview.support.jpa.context.OnlyRealRepositoryContext;
import site.hixview.support.jpa.util.SiteMemberEntityTestUtils;
import site.hixview.support.spring.util.SiteMemberTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordSnake.SITE_MEMBER_SNAKE;
import static site.hixview.support.jpa.util.ObjectEntityTestUtils.TEST_TABLE_PREFIX;

@OnlyRealRepositoryContext
class SiteMemberEntityMapperTest implements SiteMemberTestUtils, SiteMemberEntityTestUtils {

    private final SiteMemberEntityRepository siteMemberEntityRepository;
    private final JdbcTemplate jdbcTemplate;

    private final SiteMemberEntityMapperImpl mapperImpl = new SiteMemberEntityMapperImpl();

    private final String[] relatedSchemas = {TEST_TABLE_PREFIX + SITE_MEMBER_SNAKE};

    @Autowired
    SiteMemberEntityMapperTest(SiteMemberEntityRepository siteMemberEntityRepository, JdbcTemplate jdbcTemplate) {
        this.siteMemberEntityRepository = siteMemberEntityRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    public void beforeEach() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, relatedSchemas);
    }

    @DisplayName("엔터티 매퍼 사용 후 SiteMember 일관성 보장")
    @Test
    void siteMemberMappingWithEntityMapper() {
        // given
        SiteMemberEntity siteMemberEntity = siteMemberEntityRepository.save(createSiteMemberEntity());

        // when
        SiteMember siteMember = SiteMember.builder().number(siteMemberEntity.getNumber()).build();

        // then
        assertThat(mapperImpl.toSiteMember(
                mapperImpl.toSiteMemberEntity(siteMember))).isEqualTo(siteMember);
    }

    @DisplayName("엔터티 매퍼 사용 후 SiteMemberEntity 일관성 보장")
    @Test
    void siteMemberEntityMappingWithEntityMapper() {
        // given & when
        SiteMemberEntity siteMemberEntity = siteMemberEntityRepository.save(createSiteMemberEntity());

        // then
        assertThat(mapperImpl.toSiteMemberEntity(
                mapperImpl.toSiteMember(siteMemberEntity))).isEqualTo(siteMemberEntity);
    }
}