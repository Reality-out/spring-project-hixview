package site.hixview.aggregate.service.supers;

public interface CrudAllowedService<T> extends Service<T> {

    T insert(T object);

    T update(T from, T to);
}
