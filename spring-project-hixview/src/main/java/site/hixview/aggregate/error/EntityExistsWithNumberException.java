package site.hixview.aggregate.error;

import jakarta.persistence.EntityExistsException;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

public class EntityExistsWithNumberException extends EntityExistsException {
    public EntityExistsWithNumberException(Long number, Class<?> clazz) {
        super(getFormattedExceptionMessage(ALREADY_EXISTED_ENTITY, NUMBER, number, clazz));
    }
}
