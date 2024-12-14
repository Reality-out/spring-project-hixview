package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.PressDto;
import site.hixview.aggregate.dto.PressDtoNoNumber;
import site.hixview.support.spring.util.PressTestUtils;

public interface PressDtoTestUtils extends PressTestUtils {
    /**
     * Create
     */
    default PressDto createPressDto() {
        PressDto pressDto = new PressDto();
        pressDto.setNumber(press.getNumber());
        pressDto.setKoreanName(press.getKoreanName());
        pressDto.setEnglishName(press.getEnglishName());
        return pressDto;
    }

    default PressDto createAnotherPressDto() {
        PressDto pressDto = new PressDto();
        pressDto.setNumber(anotherPress.getNumber());
        pressDto.setKoreanName(anotherPress.getKoreanName());
        pressDto.setEnglishName(anotherPress.getEnglishName());
        return pressDto;
    }

    default PressDtoNoNumber createPressDtoNoNumber() {
        PressDtoNoNumber pressDto = new PressDtoNoNumber();
        pressDto.setKoreanName(press.getKoreanName());
        pressDto.setEnglishName(press.getEnglishName());
        return pressDto;
    }

    default PressDtoNoNumber createAnotherPressDtoNoNumber() {
        PressDtoNoNumber pressDto = new PressDtoNoNumber();
        pressDto.setKoreanName(anotherPress.getKoreanName());
        pressDto.setEnglishName(anotherPress.getEnglishName());
        return pressDto;
    }
}
