package site.hixview.aggregate.error;

import jakarta.persistence.EntityExistsException;

import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY_WITH_NUMBER;
import static site.hixview.aggregate.vo.ExceptionMessage.FOR_THE_CLASS_NAMED;

public class EntityExistsWithNumberException extends EntityExistsException {
    public EntityExistsWithNumberException(Long number, Class<?> clazz) {
        super(ALREADY_EXISTED_ENTITY_WITH_NUMBER + number + FOR_THE_CLASS_NAMED + clazz.getSimpleName());
    }
}
