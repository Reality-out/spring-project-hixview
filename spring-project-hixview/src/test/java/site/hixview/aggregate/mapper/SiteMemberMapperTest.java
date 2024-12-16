package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;
import site.hixview.support.spring.util.SiteMemberTestUtils;
import site.hixview.support.spring.util.dto.SiteMemberDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class SiteMemberMapperTest implements SiteMemberTestUtils, SiteMemberDtoTestUtils {

    private final SiteMemberMapperImpl mapperImpl = new SiteMemberMapperImpl();

    @DisplayName("도메인 매퍼 사용 후 SiteMember 일관성 보장")
    @Test
    void siteMemberMappingWithDomainMapper() {
        assertThat(mapperImpl.toSiteMember(mapperImpl.toSiteMemberDto(siteMember))).isEqualTo(siteMember);
        assertThat(mapperImpl.toSiteMember(mapperImpl.toSiteMemberDtoNoNumber(siteMember)))
                .usingRecursiveComparison().ignoringFields(NUMBER).isEqualTo(siteMember);
    }

    @DisplayName("도메인 매퍼 사용 후 SiteMemberDto 일관성 보장")
    @Test
    void siteMemberDtoMappingWithDomainMapper() {
        SiteMemberDto siteMemberDto = createSiteMemberDto();
        assertThat(mapperImpl.toSiteMemberDto(mapperImpl.toSiteMember(siteMemberDto))).usingRecursiveComparison().isEqualTo(siteMemberDto);
    }

    @DisplayName("도메인 매퍼 사용 후 SiteMemberDtoNoNumber 일관성 보장")
    @Test
    void siteMemberDtoNoNumberMappingWithDomainMapper() {
        SiteMemberDtoNoNumber siteMemberDto = createSiteMemberDtoNoNumber();
        assertThat(mapperImpl.toSiteMemberDtoNoNumber(mapperImpl.toSiteMember(siteMemberDto))).usingRecursiveComparison().isEqualTo(siteMemberDto);
    }
}