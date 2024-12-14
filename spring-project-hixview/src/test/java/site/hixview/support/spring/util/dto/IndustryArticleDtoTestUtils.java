package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
import site.hixview.support.spring.util.IndustryArticleTestUtils;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE;

public interface IndustryArticleDtoTestUtils extends IndustryArticleTestUtils {
    String industryArticleSecondCategoryNumbers = "{\"" + MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE + "\":[1,2]}";
    String anotherIndustryArticleSecondCategoryNumbers = "{\"" + MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE + "\":[3,4]}";

    /**
     * Create
     */
    default IndustryArticleDto createIndustryArticleDto() {
        IndustryArticleDto industryArticleDto = new IndustryArticleDto();
        industryArticleDto.setNumber(industryArticle.getNumber());
        industryArticleDto.setName(industryArticle.getName());
        industryArticleDto.setLink(industryArticle.getLink());
        industryArticleDto.setDate(String.valueOf(industryArticle.getDate()));
        industryArticleDto.setSubjectCountry(industryArticle.getSubjectCountry().name());
        industryArticleDto.setImportance(industryArticle.getImportance().name());
        industryArticleDto.setSummary(industryArticle.getSummary());
        industryArticleDto.setPressNumber(industryArticle.getPressNumber());
        industryArticleDto.setMappedSecondCategoryNumbers(industryArticleSecondCategoryNumbers);
        return industryArticleDto;
    }

    default IndustryArticleDto createAnotherIndustryArticleDto() {
        IndustryArticleDto industryArticleDto = new IndustryArticleDto();
        industryArticleDto.setNumber(anotherIndustryArticle.getNumber());
        industryArticleDto.setName(anotherIndustryArticle.getName());
        industryArticleDto.setLink(anotherIndustryArticle.getLink());
        industryArticleDto.setDate(String.valueOf(anotherIndustryArticle.getDate()));
        industryArticleDto.setSubjectCountry(anotherIndustryArticle.getSubjectCountry().name());
        industryArticleDto.setImportance(anotherIndustryArticle.getImportance().name());
        industryArticleDto.setSummary(anotherIndustryArticle.getSummary());
        industryArticleDto.setPressNumber(anotherIndustryArticle.getPressNumber());
        industryArticleDto.setMappedSecondCategoryNumbers(anotherIndustryArticleSecondCategoryNumbers);
        return industryArticleDto;
    }

    default IndustryArticleDtoNoNumber createIndustryArticleDtoNoNumber() {
        IndustryArticleDtoNoNumber industryArticleDto = new IndustryArticleDtoNoNumber();
        industryArticleDto.setName(industryArticle.getName());
        industryArticleDto.setLink(industryArticle.getLink());
        industryArticleDto.setDate(String.valueOf(industryArticle.getDate()));
        industryArticleDto.setSubjectCountry(industryArticle.getSubjectCountry().name());
        industryArticleDto.setImportance(industryArticle.getImportance().name());
        industryArticleDto.setSummary(industryArticle.getSummary());
        industryArticleDto.setPressNumber(industryArticle.getPressNumber());
        industryArticleDto.setMappedSecondCategoryNumbers(industryArticleSecondCategoryNumbers);
        return industryArticleDto;
    }

    default IndustryArticleDtoNoNumber createAnotherIndustryArticleDtoNoNumber() {
        IndustryArticleDtoNoNumber industryArticleDto = new IndustryArticleDtoNoNumber();
        industryArticleDto.setName(anotherIndustryArticle.getName());
        industryArticleDto.setLink(anotherIndustryArticle.getLink());
        industryArticleDto.setDate(String.valueOf(anotherIndustryArticle.getDate()));
        industryArticleDto.setSubjectCountry(anotherIndustryArticle.getSubjectCountry().name());
        industryArticleDto.setImportance(anotherIndustryArticle.getImportance().name());
        industryArticleDto.setSummary(anotherIndustryArticle.getSummary());
        industryArticleDto.setPressNumber(anotherIndustryArticle.getPressNumber());
        industryArticleDto.setMappedSecondCategoryNumbers(anotherIndustryArticleSecondCategoryNumbers);
        return industryArticleDto;
    }
}
