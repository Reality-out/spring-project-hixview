package site.hixview.domain.vo.name;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorCodeName {
    public static final String EXIST = "Exist";
    public static final String NOT_FOUND = "NotFound";
}
