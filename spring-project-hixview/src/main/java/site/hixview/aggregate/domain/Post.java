package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToDto;
import site.hixview.aggregate.dto.PostDto;

import java.time.LocalDate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class Post implements ConvertibleToDto<PostDto> {

    private final Long number;

    @Override
    public PostDto toDto() {
        PostDto postDto = new PostDto();
        postDto.setNumber(number);
        return postDto;
    }

    public static final class PostBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;

        public PostBuilder post(final Post post) {
            this.number = post.getNumber();
            return this;
        }

        public PostBuilder postDto(final PostDto postDto) {
            this.number = postDto.getNumber();
            return this;
        }
    }
}
