package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.PressDto;
import site.hixview.aggregate.dto.PressDtoNoNumber;

public interface PressDtoTestUtils {
    /**
     * Create
     */
    default PressDto createPressDto() {
        PressDto pressDto = new PressDto();
        pressDto.setNumber(1L);
        pressDto.setKoreanName("아주경제");
        pressDto.setEnglishName("AJU_ECONOMY");
        return pressDto;
    }

    default PressDto createAnotherPressDto() {
        PressDto pressDto = new PressDto();
        pressDto.setNumber(2L);
        pressDto.setKoreanName("아시아경제");
        pressDto.setEnglishName("ASIA_ECONOMY");
        return pressDto;
    }

    default PressDtoNoNumber createPressDtoNoNumber() {
        PressDtoNoNumber pressDto = new PressDtoNoNumber();
        pressDto.setKoreanName("아주경제");
        pressDto.setEnglishName("AJU_ECONOMY");
        return pressDto;
    }

    default PressDtoNoNumber createAnotherPressDtoNoNumber() {
        PressDtoNoNumber pressDto = new PressDtoNoNumber();
        pressDto.setKoreanName("아시아경제");
        pressDto.setEnglishName("ASIA_ECONOMY");
        return pressDto;
    }
}
