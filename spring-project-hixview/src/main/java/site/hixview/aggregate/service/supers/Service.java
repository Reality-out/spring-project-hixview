package site.hixview.aggregate.service.supers;

import java.util.List;

public interface Service<T> {
    List<T> getAll();

    void correct(T from, T to);
}
