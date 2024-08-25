package springsideproject1.springsideproject1build.domain.error.constant;

public interface EXCEPTION_MESSAGE {
    // AlreadyExistException
    String ALREADY_EXIST_ARTICLE_NAME = "이미 존재하는 기사명입니다.";
    String ALREADY_EXIST_COMPANY_CODE = "이미 존재하는 기업 코드입니다.";
    String ALREADY_EXIST_MEMBER_ID = "이미 존재하는 ID입니다.";

    // ConstraintValidationException
    String CONSTRAINT_VALIDATION_VIOLATED = "제약 조건 위반이 발생하였습니다.";

    // IndexOutOfBoundsException
    String NOT_EQUAL_LIST_SIZE = "리스트의 크기가 일치하지 않습니다.";

    // NotFoundException
    String NO_ARTICLE_WITH_THAT_NAME = "해당 기사명과 일치하는 기사가 없습니다.";
    String NO_ARTICLE_WITH_THAT_NUMBER_OR_NAME = "해당 기사 번호 또는 기사명과 일치하는 기사가 없습니다.";
    String NO_COMPANY_WITH_THAT_CODE = "해당 기업 코드와 일치하는 기업이 없습니다.";
    String NO_COMPANY_WITH_THAT_NAME = "해당 기업명과 일치하는 기업이 없습니다.";
    String NO_MEMBER_WITH_THAT_ID = "해당 ID와 일치하는 회원이 없습니다.";

    // NotMatchException
    String ARTICLE_NOT_MATCHING_PATTERN = "패턴과 매칭되지 않는 기사가 포함되어 있습니다.";
}
