package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class SiteMember {
    private final Long number;
    private final String id;
    private final String pw;
    private final String name;
    private final String email;
}