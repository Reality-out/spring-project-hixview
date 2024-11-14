package site.hixview.aggregate.domain.convertible;

public interface ConvertibleToWholeDto<T, S> extends ConvertibleToDto<T>, ConvertibleToDtoNoNumber<S>{
}
