package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;
import site.hixview.support.spring.util.EconomyArticleTestUtils;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE;

public interface EconomyArticleDtoTestUtils extends EconomyArticleTestUtils {
    String economyArticleEconomyContentNumbers = "{\"" + MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE + "\":[1,2]}";
    String anotherEconomyArticleEconomyContentNumbers = "{\"" + MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE + "\":[3,4]}";

    /**
     * Create
     */
    default EconomyArticleDto createEconomyArticleDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setNumber(economyArticle.getNumber());
        economyArticleDto.setName(economyArticle.getName());
        economyArticleDto.setLink(economyArticle.getLink());
        economyArticleDto.setDate(String.valueOf(economyArticle.getDate()));
        economyArticleDto.setSubjectCountry(economyArticle.getSubjectCountry().name());
        economyArticleDto.setImportance(economyArticle.getImportance().name());
        economyArticleDto.setSummary(economyArticle.getSummary());
        economyArticleDto.setPressNumber(economyArticle.getPressNumber());
        economyArticleDto.setMappedEconomyContentNumbers(economyArticleEconomyContentNumbers);
        return economyArticleDto;
    }

    default EconomyArticleDto createAnotherEconomyArticleDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setNumber(anotherEconomyArticle.getNumber());
        economyArticleDto.setName(anotherEconomyArticle.getName());
        economyArticleDto.setLink(anotherEconomyArticle.getLink());
        economyArticleDto.setDate(String.valueOf(anotherEconomyArticle.getDate()));
        economyArticleDto.setSubjectCountry(anotherEconomyArticle.getSubjectCountry().name());
        economyArticleDto.setImportance(anotherEconomyArticle.getImportance().name());
        economyArticleDto.setSummary(anotherEconomyArticle.getSummary());
        economyArticleDto.setPressNumber(anotherEconomyArticle.getPressNumber());
        economyArticleDto.setMappedEconomyContentNumbers(anotherEconomyArticleEconomyContentNumbers);
        return economyArticleDto;
    }

    default EconomyArticleDtoNoNumber createEconomyArticleDtoNoNumber() {
        EconomyArticleDtoNoNumber economyArticleDto = new EconomyArticleDtoNoNumber();
        economyArticleDto.setName(economyArticle.getName());
        economyArticleDto.setLink(economyArticle.getLink());
        economyArticleDto.setDate(String.valueOf(economyArticle.getDate()));
        economyArticleDto.setSubjectCountry(economyArticle.getSubjectCountry().name());
        economyArticleDto.setImportance(economyArticle.getImportance().name());
        economyArticleDto.setSummary(economyArticle.getSummary());
        economyArticleDto.setPressNumber(economyArticle.getPressNumber());
        economyArticleDto.setMappedEconomyContentNumbers(economyArticleEconomyContentNumbers);
        return economyArticleDto;
    }

    default EconomyArticleDtoNoNumber createAnotherEconomyArticleDtoNoNumber() {
        EconomyArticleDtoNoNumber economyArticleDto = new EconomyArticleDtoNoNumber();
        economyArticleDto.setName(anotherEconomyArticle.getName());
        economyArticleDto.setLink(anotherEconomyArticle.getLink());
        economyArticleDto.setDate(String.valueOf(anotherEconomyArticle.getDate()));
        economyArticleDto.setSubjectCountry(anotherEconomyArticle.getSubjectCountry().name());
        economyArticleDto.setImportance(anotherEconomyArticle.getImportance().name());
        economyArticleDto.setSummary(anotherEconomyArticle.getSummary());
        economyArticleDto.setPressNumber(anotherEconomyArticle.getPressNumber());
        economyArticleDto.setMappedEconomyContentNumbers(anotherEconomyArticleEconomyContentNumbers);
        return economyArticleDto;
    }
}
