package site.hixview.domain.vo.name;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ViewName {
    public static final String SUB_VIEW = "sub-page";
    public static final String SHOW_VIEW = "show-page";
    public static final String PROCESS_VIEW = "process-page";
    public static final String FINISH_VIEW = "finish-page";
    public static final String BEFORE_PROCESS_VIEW = "before-process-page";
    public static final String AFTER_PROCESS_VIEW = "after-process-page";
    public static final String SINGLE_PROCESS_VIEW = "single-process-page";
    public static final String SINGLE_FINISH_VIEW = "single-finish-page";
}
