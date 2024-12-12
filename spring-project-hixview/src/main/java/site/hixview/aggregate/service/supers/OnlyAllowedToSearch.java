package site.hixview.aggregate.service.supers;

import java.util.List;
import java.util.Optional;

public interface OnlyAllowedToSearch<T> {
    List<T> getAll();

    Optional<T> getByNumber(Long number);
}
