package springsideproject1.springsideproject1build;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class Utility {
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }
}
