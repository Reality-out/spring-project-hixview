package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE;

public interface IndustryArticleDtoTestUtils {
    default IndustryArticleDto createIndustryArticleDto() {
        IndustryArticleDto industryArticleDto = new IndustryArticleDto();
        industryArticleDto.setNumber(1L);
        industryArticleDto.setName("ASML, 중국 내 장비 유지보수 중단… &quot;中, 반도체 산업 타격 불가피&quot;");
        industryArticleDto.setLink("https://biz.chosun.com/it-science/ict/2024/08/30/EFI7ZQXPTZE45D53WKE4DNQ6AU/");
        industryArticleDto.setDate("2024-08-30");
        industryArticleDto.setSubjectCountry(Country.CHINA.name());
        industryArticleDto.setImportance(Importance.IMPORTANT.name());
        industryArticleDto.setSummary("테스트용 첫 번째 산업 기사");
        industryArticleDto.setPressNumber(1L);
        industryArticleDto.setMappedSecondCategoryNumbers("{\"" + MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE + "\":[1,2]}");
        return industryArticleDto;
    }

    default IndustryArticleDto createAnotherIndustryArticleDto() {
        IndustryArticleDto industryArticleDto = new IndustryArticleDto();
        industryArticleDto.setNumber(2L);
        industryArticleDto.setName("흔들리는 반도체 굴기…화웨이 &quot;3·5나노칩 확보 불가&quot;");
        industryArticleDto.setLink("https://www.sedaily.com/NewsView/2DAECLJTW4");
        industryArticleDto.setDate("2024-06-09");
        industryArticleDto.setSubjectCountry(Country.CHINA.name());
        industryArticleDto.setImportance(Importance.MODERATE.name());
        industryArticleDto.setSummary("테스트용 두 번째 산업 기사");
        industryArticleDto.setPressNumber(2L);
        industryArticleDto.setMappedSecondCategoryNumbers("{\"" + MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE + "\":[3,4]}");
        return industryArticleDto;
    }

    default IndustryArticleDtoNoNumber createIndustryArticleDtoNoNumber() {
        IndustryArticleDtoNoNumber industryArticleDto = new IndustryArticleDtoNoNumber();
        industryArticleDto.setName("ASML, 중국 내 장비 유지보수 중단… &quot;中, 반도체 산업 타격 불가피&quot;");
        industryArticleDto.setLink("https://biz.chosun.com/it-science/ict/2024/08/30/EFI7ZQXPTZE45D53WKE4DNQ6AU/");
        industryArticleDto.setDate("2024-08-30");
        industryArticleDto.setSubjectCountry(Country.CHINA.name());
        industryArticleDto.setImportance(Importance.IMPORTANT.name());
        industryArticleDto.setSummary("테스트용 첫 번째 산업 기사");
        industryArticleDto.setPressNumber(1L);
        industryArticleDto.setMappedSecondCategoryNumbers("{\"" + MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE + "\":[1,2]}");
        return industryArticleDto;
    }

    default IndustryArticleDtoNoNumber createAnotherIndustryArticleDtoNoNumber() {
        IndustryArticleDtoNoNumber industryArticleDto = new IndustryArticleDtoNoNumber();
        industryArticleDto.setName("흔들리는 반도체 굴기…화웨이 &quot;3·5나노칩 확보 불가&quot;");
        industryArticleDto.setLink("https://www.sedaily.com/NewsView/2DAECLJTW4");
        industryArticleDto.setDate("2024-06-09");
        industryArticleDto.setSubjectCountry(Country.CHINA.name());
        industryArticleDto.setImportance(Importance.MODERATE.name());
        industryArticleDto.setSummary("테스트용 두 번째 산업 기사");
        industryArticleDto.setPressNumber(2L);
        industryArticleDto.setMappedSecondCategoryNumbers("{\"" + MAPPED_SECOND_CATEGORY_NUMBERS_SNAKE + "\":[3,4]}");
        return industryArticleDto;
    }
}
