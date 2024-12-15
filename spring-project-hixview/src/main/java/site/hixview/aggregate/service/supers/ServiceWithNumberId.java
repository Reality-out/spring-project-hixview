package site.hixview.aggregate.service.supers;

import java.util.Optional;

public interface ServiceWithNumberId<T> extends Service<T> {
    Optional<T> getByNumber(Long number);
}