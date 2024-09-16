package springsideproject1.springsideproject1build.domain.vo.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Layout {
    public static final String ADD_PROCESS_LAYOUT = "/layout/manager/add/process-layout";
    public static final String ADD_FINISH_LAYOUT = "/layout/manager/add/finish-layout";
    public static final String SELECT_LAYOUT = "/layout/manager/select-layout";
    public static final String UPDATE_PROCESS_LAYOUT = "/layout/manager/update/process-layout";
    public static final String UPDATE_FINISH_LAYOUT = "/layout/manager/update/finish-layout";
    public static final String REMOVE_PROCESS_LAYOUT = "/layout/manager/remove/process-layout";
    public static final String REMOVE_FINISH_LAYOUT = "/layout/manager/remove/finish-layout";
}
