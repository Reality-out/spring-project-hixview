package site.hixview.aggregate.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    // ConversionFailedException
    public static final String CANNOT_CONVERT_TO_LOCAL_DATE = "Cannot convert the value to the type LocalDate: ";

    // EntityExistsException
    public static final String ALREADY_EXISTED_ENTITY_WITH_ID = "The entity that has the ID already exists: ";

    // EntityExistsWithNameException
    public static final String ALREADY_EXISTED_ENTITY_WITH_ENGLISH_NAME = "The entity that has the english name already exists: ";
    public static final String ALREADY_EXISTED_ENTITY_WITH_NAME = "The entity that has the name already exists: ";

    // EntityExistsWithNumberException
    public static final String ALREADY_EXISTED_ENTITY_WITH_NUMBER = "The entity that has the number already exists: ";

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

    // Others
    public static final String FOR_THE_CLASS_NAMED = ", for the class named ";
}