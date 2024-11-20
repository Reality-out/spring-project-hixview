package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.domain.convertible.ConvertibleToWholeDto;
import site.hixview.aggregate.dto.SiteMemberDto;
import site.hixview.aggregate.dto.SiteMemberDtoNoNumber;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class SiteMember implements ConvertibleToWholeDto<SiteMemberDto, SiteMemberDtoNoNumber> {

    private final Long number;
    private final String id;
    private final String pw;
    private final String name;
    private final String email;

    @Override
    public SiteMemberDto toDto() {
        SiteMemberDto siteMemberDto = new SiteMemberDto();
        siteMemberDto.setId(id);
        siteMemberDto.setPw(pw);
        siteMemberDto.setName(name);
        siteMemberDto.setEmail(email);
        return siteMemberDto;
    }

    @Override
    public SiteMemberDtoNoNumber toDtoNoNumber() {
        SiteMemberDtoNoNumber siteMemberDtoNoNumber = new SiteMemberDtoNoNumber();
        siteMemberDtoNoNumber.setId(id);
        siteMemberDtoNoNumber.setPw(pw);
        siteMemberDtoNoNumber.setName(name);
        siteMemberDtoNoNumber.setEmail(email);
        return siteMemberDtoNoNumber;
    }

    public static class SiteMemberBuilder {

        public SiteMemberBuilder member(SiteMember siteMember) {
            number = siteMember.getNumber();
            id = siteMember.getId();
            pw = siteMember.getPw();
            name = siteMember.getName();
            email = siteMember.getEmail();
            return this;
        }

        public SiteMemberBuilder memberDto(SiteMemberDto siteMemberDto) {
            number = siteMemberDto.getNumber();
            id = siteMemberDto.getId();
            pw = siteMemberDto.getPw();
            name = siteMemberDto.getName();
            email = siteMemberDto.getEmail();
            return this;
        }

        public SiteMemberBuilder memberDtoNoNumber(SiteMemberDtoNoNumber siteMemberDtoNoNumber) {
            id = siteMemberDtoNoNumber.getId();
            pw = siteMemberDtoNoNumber.getPw();
            name = siteMemberDtoNoNumber.getName();
            email = siteMemberDtoNoNumber.getEmail();
            return this;
        }
    }
}