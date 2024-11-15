package site.hixview.aggregate.domain.convertible;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ConvertibleToDto<T> {
    T toDto() throws JsonProcessingException;
}
