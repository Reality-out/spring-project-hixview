package site.hixview.aggregate.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionMessage {
    // ConversionFailedException
    public static final String CANNOT_CONVERT_TO_LOCAL_DATE = "Cannot convert the value to the type LocalDate: ";

    // DataIntegrityViolationException
    public static final String REMOVE_REFERENCED_ENTITY = "Cannot remove the entity by the value. It is referenced by other entities: ";

    // EntityExistsException
    public static final String ALREADY_EXISTED_ENTITY = "The entity that has the value already exists: ";

    // EntityNotFoundException
    public static final String CANNOT_FOUND_ENTITY = "Cannot find the entity that has the value: ";

    // RuntimeJsonMappingException
    public static final String CANNOT_MAP_TO_JSON = "Cannot map the value to JSON: ";

    // RuntimeJsonParsingException
    public static final String CANNOT_PARSE_TO_LIST = "Cannot parse the value to List: ";

    // UnexpectedClassTypeException
    public static final String NOT_ENTITY_CLASS = "The class is not an entity class: ";

    // Others
    public static final String FOR_THE_CLASS = ", for the class ";
}