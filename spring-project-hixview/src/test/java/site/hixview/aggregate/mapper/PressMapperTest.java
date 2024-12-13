package site.hixview.aggregate.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.hixview.aggregate.dto.PressDto;
import site.hixview.support.spring.util.PressTestUtils;
import site.hixview.support.spring.util.dto.PressDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
class PressMapperTest implements PressTestUtils, PressDtoTestUtils {

    private final PressMapperImpl mapperImpl = new PressMapperImpl();

    private static final Logger log = LoggerFactory.getLogger(PressMapperTest.class);

    @DisplayName("도메인 매퍼 사용 후 Press 일관성 보장")
    @Test
    void pressMappingWithDomainMapper() {
        assertThat(mapperImpl.toPress(mapperImpl.toPressDto(press))).isEqualTo(press);
    }

    @DisplayName("도메인 매퍼 사용 후 PressDto 일관성 보장")
    @Test
    void pressDtoMappingWithDomainMapper() {
        PressDto pressDto = createPressDto();
        assertThat(mapperImpl.toPressDto(mapperImpl.toPress(pressDto))).usingRecursiveComparison().isEqualTo(pressDto);
    }
}