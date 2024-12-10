package site.hixview.aggregate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteMemberDto {
    private Long number;
    private String id;
    private String pw;
    private String name;
    private String email;
}
