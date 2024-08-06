package springsideproject1.springsideproject1build.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private Long identifier;
    private String id;
    private String password;
    private String name;
    private String year;
    private String month;
    private String date;
    private String phoneNumber;
}
