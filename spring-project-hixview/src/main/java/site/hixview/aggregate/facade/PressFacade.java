package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.domain.Press.PressBuilder;
import site.hixview.aggregate.dto.PressDto;
import site.hixview.aggregate.dto.PressDtoNoNumber;

public class PressFacade {
    public PressBuilder createBuilder(Press press) {
        return Press.builder()
                .number(press.getNumber())
                .koreanName(press.getKoreanName())
                .englishName(press.getEnglishName());
    }

    public PressBuilder createBuilder(PressDto pressDto) {
        return Press.builder()
                .number(pressDto.getNumber())
                .koreanName(pressDto.getKoreanName())
                .englishName(pressDto.getEnglishName());
    }

    public PressBuilder createBuilder(PressDtoNoNumber pressDto) {
        return Press.builder()
                .koreanName(pressDto.getKoreanName())
                .englishName(pressDto.getEnglishName());
    }
}
