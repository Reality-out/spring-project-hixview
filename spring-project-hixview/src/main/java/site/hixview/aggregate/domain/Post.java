package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.PostDto;
import site.hixview.aggregate.dto.PostDtoNoNumber;

import java.time.LocalDate;

import static site.hixview.aggregate.util.ConverterUtils.convertFromStringToLocalDate;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class Post implements ConvertibleToWholeDto<PostDto, PostDtoNoNumber> {

    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;

    @Override
    public PostDto toDto() {
        PostDto postDto = new PostDto();
        postDto.setNumber(number);
        postDto.setName(name);
        postDto.setLink(link);
        postDto.setDate(String.valueOf(date));
        return postDto;
    }

    @Override
    public PostDtoNoNumber toDtoNoNumber() {
        PostDtoNoNumber postDtoNoNumber = new PostDtoNoNumber();
        postDtoNoNumber.setName(name);
        postDtoNoNumber.setLink(link);
        postDtoNoNumber.setDate(String.valueOf(date));
        return postDtoNoNumber;
    }

    public static final class PostBuilder {
        private Long number;
        private String name;
        private String link;
        private LocalDate date;

        public PostBuilder post(final Post post) {
            this.number = post.getNumber();
            this.name = post.getName();
            this.link = post.getLink();
            this.date = post.getDate();
            return this;
        }

        public PostBuilder postDto(final PostDto postDto) {
            this.number = postDto.getNumber();
            this.name = postDto.getName();
            this.link = postDto.getLink();
            this.date = convertFromStringToLocalDate(postDto.getDate());
            return this;
        }

        public PostBuilder postDtoNoNumber(final PostDtoNoNumber postDtoNoNumber) {
            this.name = postDtoNoNumber.getName();
            this.link = postDtoNoNumber.getLink();
            this.date = convertFromStringToLocalDate(postDtoNoNumber.getDate());
            return this;
        }
    }
}
