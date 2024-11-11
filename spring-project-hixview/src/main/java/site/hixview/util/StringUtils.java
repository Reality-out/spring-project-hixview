package site.hixview.util;

import static site.hixview.domain.vo.Word.*;

public abstract class StringUtils {

    public static final String FIRST_CATEGORY_SNAKE = camelToSnake(FIRST_CATEGORY);
    public static final String IMAGE_PATH_SNAKE = camelToSnake(IMAGE_PATH);
    public static final String LISTED_COUNTRY_SNAKE = camelToSnake(LISTED_COUNTRY);
    public static final String SECOND_CATEGORY_SNAKE = camelToSnake(SECOND_CATEGORY);
    public static final String SUBJECT_COMPANY_SNAKE = camelToSnake(SUBJECT_COMPANY);
    public static final String SUBJECT_COUNTRY_SNAKE = camelToSnake(SUBJECT_COUNTRY);
    public static final String SUBJECT_FIRST_CATEGORY_SNAKE = camelToSnake(SUBJECT_FIRST_CATEGORY);
    public static final String SUBJECT_SECOND_CATEGORIES_SNAKE = camelToSnake(SUBJECT_SECOND_CATEGORIES);
    public static final String TARGET_ARTICLE_LINKS_SNAKE = camelToSnake(TARGET_ARTICLE_LINKS);
    public static final String TARGET_ARTICLE_NAMES_SNAKE = camelToSnake(TARGET_ARTICLE_NAMES);
    public static final String TARGET_ECONOMY_CONTENTS_SNAKE = camelToSnake(TARGET_ECONOMY_CONTENTS);
    public static final String TARGET_IMAGE_PATH_SNAKE = camelToSnake(TARGET_IMAGE_PATH);
    public static final String TARGET_NAME_SNAKE = camelToSnake(TARGET_NAME);

    public static String camelToSnake(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(str.charAt(0)));

        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }
}
