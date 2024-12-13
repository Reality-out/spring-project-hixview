package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import static site.hixview.aggregate.vo.WordSnake.MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE;

public interface EconomyArticleDtoTestUtils {
    /**
     * Create
     */
    default EconomyArticleDto createEconomyArticleDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setNumber(1L);
        economyArticleDto.setName("“돈 푼다” 공언에 돈 들어왔다, 中 증시 나흘새 8조원 순유입");
        economyArticleDto.setLink("https://m.edaily.co.kr/News/Read?newsId=03499766639025368&mediaCodeNo=257");
        economyArticleDto.setDate("2024-09-30");
        economyArticleDto.setSubjectCountry(Country.CHINA.name());
        economyArticleDto.setImportance(Importance.MODERATE.name());
        economyArticleDto.setSummary("테스트용 첫 번째 경제 기사");
        economyArticleDto.setPressNumber(1L);
        economyArticleDto.setMappedEconomyContentNumbers("{\"" + MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE + "\":[1,2]}");
        return economyArticleDto;
    }

    default EconomyArticleDto createAnotherEconomyArticleDto() {
        EconomyArticleDto economyArticleDto = new EconomyArticleDto();
        economyArticleDto.setNumber(1L);
        economyArticleDto.setName("韓, WGBI 26번째 편입…9번째 규모 글로벌 투자처");
        economyArticleDto.setLink("https://www.newsis.com/view/NISX20241009_0002913996");
        economyArticleDto.setDate("2024-10-09");
        economyArticleDto.setSubjectCountry(Country.SOUTH_KOREA.name());
        economyArticleDto.setImportance(Importance.MODERATE.name());
        economyArticleDto.setSummary("테스트용 두 번째 경제 기사");
        economyArticleDto.setPressNumber(2L);
        economyArticleDto.setMappedEconomyContentNumbers("{\"" + MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE + "\":[3,4]}");
        return economyArticleDto;
    }

    default EconomyArticleDtoNoNumber createEconomyArticleDtoNoNumber() {
        EconomyArticleDtoNoNumber economyArticleDto = new EconomyArticleDtoNoNumber();
        economyArticleDto.setName("“돈 푼다” 공언에 돈 들어왔다, 中 증시 나흘새 8조원 순유입");
        economyArticleDto.setLink("https://m.edaily.co.kr/News/Read?newsId=03499766639025368&mediaCodeNo=257");
        economyArticleDto.setDate("2024-09-30");
        economyArticleDto.setSubjectCountry(Country.CHINA.name());
        economyArticleDto.setImportance(Importance.MODERATE.name());
        economyArticleDto.setSummary("테스트용 첫 번째 경제 기사");
        economyArticleDto.setPressNumber(1L);
        economyArticleDto.setMappedEconomyContentNumbers("{\"" + MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE + "\":[1,2]}");
        return economyArticleDto;
    }

    default EconomyArticleDtoNoNumber createAnotherEconomyArticleDtoNoNumber() {
        EconomyArticleDtoNoNumber economyArticleDto = new EconomyArticleDtoNoNumber();
        economyArticleDto.setName("韓, WGBI 26번째 편입…9번째 규모 글로벌 투자처");
        economyArticleDto.setLink("https://www.newsis.com/view/NISX20241009_0002913996");
        economyArticleDto.setDate("2024-10-09");
        economyArticleDto.setSubjectCountry(Country.SOUTH_KOREA.name());
        economyArticleDto.setImportance(Importance.MODERATE.name());
        economyArticleDto.setSummary("테스트용 두 번째 경제 기사");
        economyArticleDto.setPressNumber(2L);
        economyArticleDto.setMappedEconomyContentNumbers("{\"" + MAPPED_ECONOMY_CONTENT_NUMBERS_SNAKE + "\":[3,4]}");
        return economyArticleDto;
    }
}
