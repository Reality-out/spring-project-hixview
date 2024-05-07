package springsideproject1.springsideproject1production;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class Utility {
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }
}
