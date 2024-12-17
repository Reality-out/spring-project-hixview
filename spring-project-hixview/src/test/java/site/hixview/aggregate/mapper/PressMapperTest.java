package site.hixview.aggregate.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import site.hixview.aggregate.dto.PressDto;
import site.hixview.support.spring.util.PressTestUtils;
import site.hixview.support.spring.util.dto.PressDtoTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Execution(value = ExecutionMode.CONCURRENT)
@Slf4j
class PressMapperTest implements PressTestUtils, PressDtoTestUtils {

    private final PressMapper mapperImpl = new PressMapperImpl();

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