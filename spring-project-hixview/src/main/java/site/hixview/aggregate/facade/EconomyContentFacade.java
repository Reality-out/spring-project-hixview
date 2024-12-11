package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.domain.EconomyContent.EconomyContentBuilder;
import site.hixview.aggregate.dto.EconomyContentDto;

public class EconomyContentFacade {
    public EconomyContentBuilder createBuilder(final EconomyContent economyContent) {
        return EconomyContent.builder()
                .number(economyContent.getNumber())
                .name(economyContent.getName());
    }

    public EconomyContentBuilder createBuilder(final EconomyContentDto economyContent) {
        return EconomyContent.builder()
                .number(economyContent.getNumber())
                .name(economyContent.getName());
    }
}