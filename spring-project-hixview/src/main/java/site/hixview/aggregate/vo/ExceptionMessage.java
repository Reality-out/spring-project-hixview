package site.hixview.aggregate.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    // AccessException
    public static final String NOT_REPOSITORY_TEST = "해당 테스트는 리포지토리를 대상으로 하는 테스트가 아닙니다: ";

    // ConfigurationException
    public static final String NOT_REGISTERED_LISTENER = "해당 키로 등록된 TestExecutionListener가 없습니다: ";

    // ConversionFailedException
    public static final String CANNOT_CONVERT_TO_LOCAL_DATE = "해당 값을 LocalDate 타입으로 변환할 수 없습니다: ";

    // RuntimeJsonMappingException
    public static final String CANNOT_MAP_TO_JSON = "해당 값을 JSON으로 매핑할 수 없습니다: ";

    // RuntimeJsonParsingException
    public static final String CANNOT_PARSE_TO_LIST = "해당 값을 List로 파싱할 수 없습니다: ";

    // SQLException
    public static final String FAILED_BATCH_PROCESSING = "해당 SQL문을 사용한 배치 처리에 실패했습니다: ";

    // UnexpectedClassTypeException
    public static final String NOT_ENTITY_CLASS = "해당 클래스가 엔터티 클래스가 아닙니다: ";
}