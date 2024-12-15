package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.Post;
import site.hixview.jpa.entity.PostEntity;

public interface PostEntityMapperSupport {
    @AfterMapping
    default PostEntity afterMappingToEntity(
            @MappingTarget PostEntity postEntity, Post post) {
        PostEntity newPostEntity = new PostEntity();
        newPostEntity.updateNumber(post.getNumber());
        return newPostEntity;
    }
}
