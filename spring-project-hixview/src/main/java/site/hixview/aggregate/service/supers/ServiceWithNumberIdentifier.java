package site.hixview.aggregate.service.supers;

import java.util.Optional;

public interface ServiceWithNumberIdentifier<T> extends Service<T> {
    Optional<T> getByNumber(Long number);

    void removeByNumber(Long number);
}