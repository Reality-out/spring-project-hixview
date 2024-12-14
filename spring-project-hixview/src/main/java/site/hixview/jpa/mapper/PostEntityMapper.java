package site.hixview.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import site.hixview.aggregate.domain.Post;
import site.hixview.jpa.entity.PostEntity;

import static site.hixview.aggregate.vo.WordCamel.VERSION_NUMBER;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PostEntityMapper {
    @Mapping(target = VERSION_NUMBER, ignore = true)
    PostEntity toPostEntity(Post post);

    Post toPost(PostEntity postEntity);
}
