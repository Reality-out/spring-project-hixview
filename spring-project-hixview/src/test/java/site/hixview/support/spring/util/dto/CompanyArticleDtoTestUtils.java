package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;
import site.hixview.support.spring.util.CompanyArticleTestUtils;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_COMPANY_CODES_SNAKE;

public interface CompanyArticleDtoTestUtils extends CompanyArticleTestUtils {
    String companyArticleCompanyCodes = "{\"" + MAPPED_COMPANY_CODES_SNAKE + "\":[\"000270\",\"000660\"]}";
    String anotherCompanyArticleCompanyCodes = "{\"" + MAPPED_COMPANY_CODES_SNAKE + "\":[\"000660\",\"005380\"]}";

    /**
     * Create
     */
    default CompanyArticleDto createCompanyArticleDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(companyArticle.getNumber());
        companyArticleDto.setName(companyArticle.getName());
        companyArticleDto.setLink(companyArticle.getLink());
        companyArticleDto.setDate(String.valueOf(companyArticle.getDate()));
        companyArticleDto.setSubjectCountry(companyArticle.getSubjectCountry().name());
        companyArticleDto.setImportance(companyArticle.getImportance().name());
        companyArticleDto.setSummary(companyArticle.getSummary());
        companyArticleDto.setPressNumber(companyArticle.getPressNumber());
        companyArticleDto.setMappedCompanyCodes(companyArticleCompanyCodes);
        return companyArticleDto;
    }

    default CompanyArticleDto createAnotherCompanyArticleDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(anotherCompanyArticle.getNumber());
        companyArticleDto.setName(anotherCompanyArticle.getName());
        companyArticleDto.setLink(anotherCompanyArticle.getLink());
        companyArticleDto.setDate(String.valueOf(anotherCompanyArticle.getDate()));
        companyArticleDto.setSubjectCountry(anotherCompanyArticle.getSubjectCountry().name());
        companyArticleDto.setImportance(anotherCompanyArticle.getImportance().name());
        companyArticleDto.setSummary(anotherCompanyArticle.getSummary());
        companyArticleDto.setPressNumber(anotherCompanyArticle.getPressNumber());
        companyArticleDto.setMappedCompanyCodes(anotherCompanyArticleCompanyCodes);
        return companyArticleDto;
    }

    default CompanyArticleDtoNoNumber createCompanyArticleDtoNoNumber() {
        CompanyArticleDtoNoNumber companyArticleDto = new CompanyArticleDtoNoNumber();
        companyArticleDto.setName(companyArticle.getName());
        companyArticleDto.setLink(companyArticle.getLink());
        companyArticleDto.setDate(String.valueOf(companyArticle.getDate()));
        companyArticleDto.setSubjectCountry(companyArticle.getSubjectCountry().name());
        companyArticleDto.setImportance(companyArticle.getImportance().name());
        companyArticleDto.setSummary(companyArticle.getSummary());
        companyArticleDto.setPressNumber(companyArticle.getPressNumber());
        companyArticleDto.setMappedCompanyCodes(companyArticleCompanyCodes);
        return companyArticleDto;
    }

    default CompanyArticleDtoNoNumber createAnotherCompanyArticleDtoNoNumber() {
        CompanyArticleDtoNoNumber companyArticleDto = new CompanyArticleDtoNoNumber();
        companyArticleDto.setName(anotherCompanyArticle.getName());
        companyArticleDto.setLink(anotherCompanyArticle.getLink());
        companyArticleDto.setDate(String.valueOf(anotherCompanyArticle.getDate()));
        companyArticleDto.setSubjectCountry(anotherCompanyArticle.getSubjectCountry().name());
        companyArticleDto.setImportance(anotherCompanyArticle.getImportance().name());
        companyArticleDto.setSummary(anotherCompanyArticle.getSummary());
        companyArticleDto.setPressNumber(anotherCompanyArticle.getPressNumber());
        companyArticleDto.setMappedCompanyCodes(anotherCompanyArticleCompanyCodes);
        return companyArticleDto;
    }
}
