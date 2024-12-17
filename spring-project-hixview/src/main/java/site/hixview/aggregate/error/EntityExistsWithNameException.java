package site.hixview.aggregate.error;

import jakarta.persistence.EntityExistsException;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NAME;

public class EntityExistsWithNameException extends EntityExistsException {
    public EntityExistsWithNameException(String name, Class<?> clazz) {
        super(getFormattedExceptionMessage(ALREADY_EXISTED_ENTITY, NAME, name, clazz));
    }
}
