package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Post;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.mapper.support.PostEntityMapperSupport;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PostEntityMapper extends PostEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    PostEntity toPostEntity(Post post);

    Post toPost(PostEntity postEntity);
}
