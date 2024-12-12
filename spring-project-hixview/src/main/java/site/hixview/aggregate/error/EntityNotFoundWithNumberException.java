package site.hixview.aggregate.error;

import jakarta.persistence.EntityNotFoundException;

import static site.hixview.aggregate.vo.ExceptionMessage.CANNOT_FOUND_ENTITY_WITH_NUMBER;

public class EntityNotFoundWithNumberException extends EntityNotFoundException {
    public EntityNotFoundWithNumberException(Long number, Class<?> clazz) {
        super(CANNOT_FOUND_ENTITY_WITH_NUMBER + number + " , for the class named " + clazz.getSimpleName());
    }
}
