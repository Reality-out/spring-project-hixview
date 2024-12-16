package site.hixview.jpa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.Post;
import site.hixview.jpa.entity.PostEntity;
import site.hixview.jpa.mapper.support.PostEntityMapperSupport;

@Mapper
public interface PostEntityMapper extends PostEntityMapperSupport {
    @BeanMapping(ignoreByDefault = true)
    PostEntity toPostEntity(Post post);

    Post toPost(PostEntity postEntity);
}
