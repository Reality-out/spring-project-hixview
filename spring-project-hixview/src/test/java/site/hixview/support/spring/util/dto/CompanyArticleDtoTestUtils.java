package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_COMPANY_CODES_SNAKE;

public interface CompanyArticleDtoTestUtils {
    /**
     * Create
     */
    default CompanyArticleDto createCompanyArticleDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(1L);
        companyArticleDto.setName("[단독] 파나소닉 &quot;현대차와 언제든 전장협업 가능&quot");
        companyArticleDto.setLink("https://www.sedaily.com/NewsView/2D413KUMZY");
        companyArticleDto.setDate("2024-01-10");
        companyArticleDto.setSubjectCountry(Country.SOUTH_KOREA.name());
        companyArticleDto.setImportance(Importance.MODERATE.name());
        companyArticleDto.setSummary("테스트용 첫 번째 기업 기사");
        companyArticleDto.setPressNumber(1L);
        companyArticleDto.setMappedCompanyCodes("{\"" + MAPPED_COMPANY_CODES_SNAKE + "\":[\"000270\",\"000660\"]}");
        return companyArticleDto;
    }

    default CompanyArticleDto createAnotherCompanyArticleDto() {
        CompanyArticleDto companyArticleDto = new CompanyArticleDto();
        companyArticleDto.setNumber(1L);
        companyArticleDto.setName("원료부터 재활용 기술 확보까지…현대차그룹, 배터리 내재화 이유는%3F");
        companyArticleDto.setLink("https://www.newswatch.kr/news/articleView.html?idxno=66894");
        companyArticleDto.setDate("2024-01-23");
        companyArticleDto.setSubjectCountry(Country.SOUTH_KOREA.name());
        companyArticleDto.setImportance(Importance.MODERATE.name());
        companyArticleDto.setSummary("테스트용 두 번째 기업 기사");
        companyArticleDto.setPressNumber(2L);
        companyArticleDto.setMappedCompanyCodes("{\"" + MAPPED_COMPANY_CODES_SNAKE + "\":[\"000660\",\"005380\"]}");
        return companyArticleDto;
    }

    default CompanyArticleDtoNoNumber createCompanyArticleDtoNoNumber() {
        CompanyArticleDtoNoNumber companyArticleDto = new CompanyArticleDtoNoNumber();
        companyArticleDto.setName("[단독] 파나소닉 &quot;현대차와 언제든 전장협업 가능&quot");
        companyArticleDto.setLink("https://www.sedaily.com/NewsView/2D413KUMZY");
        companyArticleDto.setDate("2024-01-10");
        companyArticleDto.setSubjectCountry(Country.SOUTH_KOREA.name());
        companyArticleDto.setImportance(Importance.MODERATE.name());
        companyArticleDto.setSummary("테스트용 첫 번째 기업 기사");
        companyArticleDto.setPressNumber(1L);
        companyArticleDto.setMappedCompanyCodes("{\"" + MAPPED_COMPANY_CODES_SNAKE + "\":[\"000270\",\"000660\"]}");
        return companyArticleDto;
    }

    default CompanyArticleDtoNoNumber createAnotherCompanyArticleDtoNoNumber() {
        CompanyArticleDtoNoNumber companyArticleDto = new CompanyArticleDtoNoNumber();
        companyArticleDto.setName("원료부터 재활용 기술 확보까지…현대차그룹, 배터리 내재화 이유는%3F");
        companyArticleDto.setLink("https://www.newswatch.kr/news/articleView.html?idxno=66894");
        companyArticleDto.setDate("2024-01-23");
        companyArticleDto.setSubjectCountry(Country.SOUTH_KOREA.name());
        companyArticleDto.setImportance(Importance.MODERATE.name());
        companyArticleDto.setSummary("테스트용 두 번째 기업 기사");
        companyArticleDto.setPressNumber(2L);
        companyArticleDto.setMappedCompanyCodes("{\"" + MAPPED_COMPANY_CODES_SNAKE + "\":[\"000660\",\"005380\"]}");
        return companyArticleDto;
    }
}
