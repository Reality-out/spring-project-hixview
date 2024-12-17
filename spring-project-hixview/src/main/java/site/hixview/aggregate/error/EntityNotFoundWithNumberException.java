package site.hixview.aggregate.error;

import jakarta.persistence.EntityNotFoundException;

import static site.hixview.aggregate.util.ExceptionUtils.getFormattedExceptionMessage;
import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY;
import static site.hixview.aggregate.vo.WordCamel.NUMBER;

public class EntityNotFoundWithNumberException extends EntityNotFoundException {
    public EntityNotFoundWithNumberException(Long number, Class<?> clazz) {
        super(getFormattedExceptionMessage(CANNOT_FOUND_ENTITY, NUMBER, number, clazz));
    }
}
