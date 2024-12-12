package site.hixview.aggregate.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    // ConversionFailedException
    public static final String CANNOT_CONVERT_TO_LOCAL_DATE = "Cannot convert the value to the type LocalDate: ";

    // EntityNotFoundException
    public static final String CANNOT_FOUND_ENTITY_WITH_CODE = "Cannot find the entity that has the code: ";

    // EntityNotFoundWithNumberException
    public static final String CANNOT_FOUND_ENTITY_WITH_NUMBER = "Cannot find the entity that has the number: ";

    // RuntimeJsonMappingException
    public static final String CANNOT_MAP_TO_JSON = "Cannot map the value to JSON: ";

    // RuntimeJsonParsingException
    public static final String CANNOT_PARSE_TO_LIST = "Cannot parse the value to List: ";

    // UnexpectedClassTypeException
    public static final String NOT_ENTITY_CLASS = "The class is not an entity class: ";
}