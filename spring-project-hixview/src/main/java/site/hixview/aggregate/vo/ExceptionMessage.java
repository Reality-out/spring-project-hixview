package site.hixview.aggregate.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    // ConversionFailedException
    public static final String CANNOT_CONVERT_TO_LOCAL_DATE = "해당 값을 LocalDate 타입으로 변환할 수 없습니다: ";

    // NotExpectedClassArgumentException
    public static final String NOT_ENTITY_CLASS = "해당 클래스가 엔터티 클래스가 아닙니다: ";

    // RuntimeJsonMappingException
    public static final String CANNOT_PARSE_TO_JSON = "해당 값을 JSON으로 파싱할 수 없습니다: ";

    // RuntimeJsonParsingException
    public static final String CANNOT_PARSE_TO_LIST = "해당 값을 List로 파싱할 수 없습니다: ";
}