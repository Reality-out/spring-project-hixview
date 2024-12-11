package site.hixview.aggregate.util;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_CONVERT_TO_LOCAL_DATE;
import static site.hixview.aggregate.vo.Regex.NUMBER_REGEX;

public abstract class ConverterUtils {
    private static final String YEAR_VALUE = "24";

    public static LocalDate convertFromStringToLocalDate(String string) {
        if (!string.matches(NUMBER_REGEX) || string.length() != 6) {
            throw new ConversionFailedException(TypeDescriptor.valueOf(String.class), TypeDescriptor.valueOf(LocalDate.class), string, new IllegalArgumentException(CANNOT_CONVERT_TO_LOCAL_DATE + string));
        }
        return LocalDate.of(Integer.parseInt(YEAR_VALUE + string.substring(0, 2)),
                Integer.parseInt(string.substring(2, 4)),
                Integer.parseInt(string.substring(4, 6)));
    }
}
