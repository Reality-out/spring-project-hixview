package springsideproject1.springsideproject1build.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.*;
import static springsideproject1.springsideproject1build.domain.error.constant.EXCEPTION_STRING.ERROR_SINGLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.LAYOUT_PATH;

public abstract class ControllerUtils {

    private static final Logger log = LoggerFactory.getLogger(ControllerUtils.class);

    /**
     * Decode
     */
    public static List<String> decodeWithUTF8(List<String> list) {
        return list.stream()
                .map(ControllerUtils::decodeWithUTF8)
                .collect(Collectors.toList());
    }

    public static String decodeWithUTF8(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }

    /**
     * Encode
     */
    public static List<String> encodeWithUTF8(List<String> list) {
        return list.stream()
                .map(ControllerUtils::encodeWithUTF8)
                .collect(Collectors.toList());
    }

    public static String encodeWithUTF8(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    /**
     * Handle Error
     */
    public static void finishForRollback(String logMessage, String layoutPath, String error, Model model) {
        log.error(ERRORS_ARE, logMessage);
        model.addAttribute(LAYOUT_PATH, layoutPath);
        model.addAttribute(ERROR, error);
    }

    public static void finishForRedirect(String logMessage, RedirectAttributes redirect,
                                         List<String> nameListString, boolean isBeanValidationError, String errorSingle) {
        if (!logMessage.isEmpty()) {
            log.error(ERRORS_ARE, logMessage);
        }
        redirect.addAttribute("nameList", nameListString);
        redirect.addAttribute(IS_BEAN_VALIDATION_ERROR, isBeanValidationError);
        redirect.addAttribute(ERROR_SINGLE, errorSingle);
    }

    /**
     * Parse
     */
    public static List<List<String>> parseArticleString(String articleString) {
        List<String> dividedArticle = List.of(articleString.split("\\R"));
        List<List<String>> returnArticle = new ArrayList<>();

        for (int i = 0; i < dividedArticle.size(); i++) {
            if (i % 2 == 0) {
                returnArticle.add(new ArrayList<>(List.of(dividedArticle.get(i))));
            } else {
                returnArticle.getLast().addAll(List.of(dividedArticle.get(i)
                        .replaceAll("^\\(|\\)$", "").split(",\\s|-")));
                if (returnArticle.getLast().size() != 5) {
                    returnArticle.remove(i);
                    break;
                }
            }
        }
        if (returnArticle.getLast().size() != 5) {
            returnArticle.removeLast();
        }
        return returnArticle;
    }

    public static List<String> parseLinkString(String linkString) {
        if (linkString.isEmpty()) {
            return Collections.emptyList();
        }
        return List.of(linkString.split("\\R"));
    }
}
