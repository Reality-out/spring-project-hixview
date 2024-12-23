package site.hixview.jpa.mapper.support;

import org.mapstruct.Named;
import site.hixview.jpa.entity.PostEntity;

interface SuperPostEntityMapperSupport {
    @Named("numberToDomain")
    default Long numberToDomain(PostEntity postEntity) {
        return postEntity.getNumber();
    }
}
