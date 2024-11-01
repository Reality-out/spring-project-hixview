package site.hixview.domain.entity.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank(message = "ID가 비어 있습니다.")
    private String id;

    @NotBlank(message = "비밀번호가 비어 있습니다.")
    private String password;
}
