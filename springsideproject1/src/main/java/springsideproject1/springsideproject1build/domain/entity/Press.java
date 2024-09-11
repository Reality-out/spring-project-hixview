package springsideproject1.springsideproject1build.domain.entity;

import lombok.Getter;
import springsideproject1.springsideproject1build.domain.error.NotFoundException;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_MESSAGE.NO_PRESS_WITH_THAT_VALUE;

@Getter
public enum Press {
    AJU_ECONOMY("아주경제"),
    ASIA_ECONOMY("아시아경제"),
    ASIA_TODAY("아시아투데이"),
    BIG_DATA_NEWS("빅데이터뉴스"),
    BLOTER("블로터"),
    BUSINESS_POST("비즈니스포스트"),
    CHUNGCHEONG_SINMUN("충청신문"),
    CHOSUN_BIZ("조선비즈"),
    DAILY_HANKOOK("데일리한국"),
    DEAL_SITE("딜사이트"),
    DIGITAL_DAILY("디지털데일리"),
    ELECTRONIC_TIMES("전자신문"),
    E_DAILY("이데일리"),
    E_TODAY("이투데이"),
    FINANCIAL_NEWS("파이낸셜뉴스"),
    GUKJE_NEWS("국제뉴스"),
    HERALD_ECONOMY("헤럴드경제"),
    INFOSTOCK_DAILY("인포스탁데일리"),
    INVESTING_COM("인베스팅닷컴"),
    IT_CHOSUN("IT조선"),
    JOONGANG_ILBO("중앙일보"),
    KBS("KBS"),
    KI_POST("키포스트"),
    KOREA_ECONOMY("한국경제"),
    KOREAN_ECONOMY("대한경제"),
    K_BENCH("케이벤치"),
    MAEIL_BUSINESS("매일경제"),
    MBC("MBC"),
    MEDIA_PEN("미디어펜"),
    MONEY_TODAY("머니투데이"),
    NEWSIS("뉴시스"),
    NEWS_VISION("뉴스비전e"),
    NEWS_WATCH("뉴스워치"),
    NEWS_WAY("뉴스웨이"),
    OPINION_NEWS("오피니언뉴스"),
    SBS("SBS"),
    SEOUL_ECONOMY("서울경제"),
    THE_BELL("더벨"),
    THE_ELEC("디일렉"),
    THE_GURU("더구루"),
    VOICE_OF_THE_PEOPLE("민중의소리"),
    YONHAP_INFOMAX("연합인포맥스"),
    YONHAP_NEWS("연합뉴스"),
    YONHAP_NEWS_TV("연합뉴스TV"),
    YTN("YTN"),
    ZDNET_KOREA("지디넷코리아");

    private final String pressValue;

    Press(String pressValue) {
        this.pressValue = pressValue;
    }

    public static boolean containedWithPress(String str) {
        for (Press enumValue : Press.values()) {
            if (enumValue.name().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean containedWithPressValue(String str) {
        for (Press enumValue : Press.values()) {
            if (enumValue.pressValue.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static Press convertToPress(String str) {
        for (Press enumValue : Press.values()) {
            if (enumValue.pressValue.equals(str)) {
                return enumValue;
            }
        }
        throw new NotFoundException(NO_PRESS_WITH_THAT_VALUE);
    }
}
