package site.hixview.aggregate.error;

import jakarta.persistence.EntityNotFoundException;

import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY_WITH_NUMBER;
import static site.hixview.aggregate.vo.ExceptionMessage.FOR_THE_CLASS_NAMED;

public class EntityNotFoundWithNumberException extends EntityNotFoundException {
    public EntityNotFoundWithNumberException(Long number, Class<?> clazz) {
        super(CANNOT_FOUND_ENTITY_WITH_NUMBER + number + FOR_THE_CLASS_NAMED + clazz.getSimpleName());
    }
}
