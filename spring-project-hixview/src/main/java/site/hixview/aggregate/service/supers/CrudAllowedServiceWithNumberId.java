package site.hixview.aggregate.service.supers;

public interface CrudAllowedServiceWithNumberId<T> extends CrudAllowedService<T>, ServiceWithNumberId<T> {
    void removeByNumber(Long number);
}
