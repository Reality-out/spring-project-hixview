package springsideproject1.springsideproject1build.domain.error.constant;

public interface EXCEPTION_MESSAGE {
    // AlreadyExistException
    String ALREADY_EXIST_COMPANY_ARTICLE_NAME = "이미 존재하는 기업 기사명입니다.";
    String ALREADY_EXIST_INDUSTRY_ARTICLE_NAME = "이미 존재하는 산업 기사명입니다.";
    String ALREADY_EXIST_ARTICLE_MAIN_NAME = "이미 존재하는 기사 메인명입니다.";
    String ALREADY_EXIST_COMPANY_CODE = "이미 존재하는 기업 코드입니다.";
    String ALREADY_EXIST_MEMBER_ID = "이미 존재하는 ID입니다.";

    // ConstraintValidationException
    String CONSTRAINT_VALIDATION_VIOLATED = "제약 조건 위반이 발생하였습니다.";

    // IndexOutOfBoundsException
    String NOT_EQUAL_LIST_SIZE = "리스트의 크기가 일치하지 않습니다.";

    // NotBlankException
    String EMPTY_ARTICLE = "기사가 비어 있습니다.";

    /**
     * NotFoundException
     */
    // Article
    String NO_COMPANY_ARTICLE_WITH_THAT_NAME = "해당 기사명과 일치하는 기업 기사가 없습니다.";
    String NO_COMPANY_ARTICLE_WITH_THAT_NUMBER_OR_NAME = "해당 기사 번호 또는 기사명과 일치하는 기업 기사가 없습니다.";
    String NO_COMPANY_ARTICLE_WITH_THAT_CONDITION = "해당 조건에 부합하는 기업 기사가 없습니다.";
    String NO_INDUSTRY_ARTICLE_WITH_THAT_NAME = "해당 기사명과 일치하는 산업 기사가 없습니다.";
    String NO_INDUSTRY_ARTICLE_WITH_THAT_NUMBER_OR_NAME = "해당 기사 번호 또는 기사명과 일치하는 산업 기사가 없습니다.";
    String NO_INDUSTRY_ARTICLE_WITH_THAT_CONDITION = "해당 조건에 부합하는 산업 기사가 없습니다.";
    String NO_ARTICLE_MAIN_WITH_THAT_NAME = "해당 기사명과 일치하는 기사 메인이 없습니다.";
    String NO_ARTICLE_MAIN_WITH_THAT_NUMBER_OR_NAME = "해당 기사 번호 또는 기사명과 일치하는 기사 메인이 없습니다.";
    String NO_PRESS_WITH_THAT_VALUE = "해당 한글 언론사와 일치하는 언론사가 없습니다.";

    // Company
    String NO_COMPANY_WITH_THAT_CODE = "해당 기업 코드와 일치하는 기업이 없습니다.";
    String NO_COMPANY_WITH_THAT_NAME = "해당 기업명과 일치하는 기업이 없습니다.";
    String NO_COMPANY_WITH_THAT_CODE_OR_NAME = "해당 기업 코드 또는 기업명과 일치하는 기업이 없습니다.";
    String NO_FIRST_CATEGORY_WITH_THAT_VALUE = "해당 1차 업종과 일치하는 1차 업종이 없습니다.";
    String NO_SECOND_CATEGORY_WITH_THAT_VALUE = "해당 2차 업종과 일치하는 2차 업종이 없습니다.";
    String NO_COUNTRY_WITH_THAT_KOREAN_VALUE = "해당 한글 상장된 국가와 일치하는 상장된 국가가 없습니다.";
    String NO_SCALE_WITH_THAT_KOREAN_VALUE = "해당 한글 상장된 국가와 일치하는 상장된 국가가 없습니다.";
    String NO_FIRST_CATEGORY_WITH_THAT_KOREAN_VALUE = "해당 한글 1차 업종과 일치하는 1차 업종이 없습니다.";
    String NO_SECOND_CATEGORY_WITH_THAT_KOREAN_VALUE = "해당 한글 2차 업종과 일치하는 2차 업종이 없습니다.";

    // Member
    String NO_MEMBER_WITH_THAT_ID = "해당 ID와 일치하는 회원이 없습니다.";
}
