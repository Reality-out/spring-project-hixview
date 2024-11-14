package site.hixview.aggregate.domain.convertible;

public interface ConvertibleToDto<T> {
    T toDto();
}
