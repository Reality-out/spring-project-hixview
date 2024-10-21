package site.hixview.util;

import org.springframework.context.MessageSource;

import java.util.Locale;

public abstract class MessageUtils {

    public static String getDefaultMessage(MessageSource source, String errCode, String errMessage) {
        if (errCode.equals("Range")) {
            return getDefaultMessageForRange(source, errMessage.split("\\.")[2], errMessage);
        } else {
            return source.getMessage(errMessage, null, Locale.getDefault());
        }
    }

    public static String getDefaultMessageForRange(MessageSource source, String dateMessage, String rangeMessage) {
        String dateTarget = source.getMessage(dateMessage, null, Locale.getDefault());
        if (dateTarget.equals("연")) {
            return source.getMessage(rangeMessage, new Object[]{dateTarget, 2099, 1960}, Locale.getDefault());
        } else if (dateTarget.equals("월")) {
            return source.getMessage(rangeMessage, new Object[]{dateTarget, 12, 1}, Locale.getDefault());
        } else {
            return source.getMessage(rangeMessage, new Object[]{dateTarget, 31, 1}, Locale.getDefault());
        }
    }
}
