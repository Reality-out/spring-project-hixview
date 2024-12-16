package site.hixview.aggregate.error;

import jakarta.persistence.EntityExistsException;

import static site.hixview.aggregate.vo.ExceptionMessage.ALREADY_EXISTED_ENTITY_WITH_NAME;
import static site.hixview.aggregate.vo.ExceptionMessage.FOR_THE_CLASS_NAMED;

public class EntityExistsWithNameException extends EntityExistsException {
    public EntityExistsWithNameException(String message) {
        super(message);
    }

    public EntityExistsWithNameException(String name, Class<?> clazz) {
        super(ALREADY_EXISTED_ENTITY_WITH_NAME + name + FOR_THE_CLASS_NAMED + clazz.getSimpleName());
    }
}
