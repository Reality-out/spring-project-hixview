package springsideproject1.springsideproject1build.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.service.MemberService;

import java.time.LocalDate;

import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;

@Controller
@RequiredArgsConstructor
public class UserMemberController {

    @Autowired
    private final MemberService memberService;

    /**
     * Membership
     */
    @GetMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processMembership() {
        return MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX;
    }

    @GetMapping(MEMBERSHIP_URL + URL_FINISH_SUFFIX)
    @ResponseStatus(HttpStatus.OK)
    public String finishMembership() {
        return MEMBERSHIP_VIEW + VIEW_FINISH_SUFFIX;
    }

    @PostMapping(MEMBERSHIP_URL)
    @ResponseStatus(HttpStatus.SEE_OTHER)
    public String submitMembership(String id, String password, String name,
                                   Integer year, Integer month, Integer date, String phoneNumber) {
        memberService.joinMember(Member.builder().id(id).password(password).name(name)
                .birth(LocalDate.of(year, month, date)).phoneNumber(phoneNumber).build());
        return URL_REDIRECT_PREFIX + MEMBERSHIP_URL + URL_FINISH_SUFFIX;
    }
}