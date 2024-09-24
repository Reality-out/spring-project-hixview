package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.config.annotation.OnlyRealControllerConfig;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.service.ArticleMainService;
import site.hixview.domain.service.CompanyArticleService;
import site.hixview.domain.service.IndustryArticleService;
import site.hixview.domain.service.MemberService;
import site.hixview.util.test.ArticleMainTestUtils;
import site.hixview.util.test.CompanyArticleTestUtils;
import site.hixview.util.test.IndustryArticleTestUtils;
import site.hixview.util.test.MemberTestUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.EntityName.Member.MEMBER;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestUrl.*;
import static site.hixview.domain.vo.user.ViewName.*;

@OnlyRealControllerConfig
class UserMainControllerTest implements MemberTestUtils, CompanyArticleTestUtils, IndustryArticleTestUtils, ArticleMainTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyArticleService companyArticleService;

    @Autowired
    private IndustryArticleService industryArticleService;

    @Autowired
    private ArticleMainService articleMainService;

    @Autowired
    private MemberService memberService;

    @DisplayName("유저 메인 페이지 접속")
    @Test
    void accessUserMainPage() throws Exception {
        // given & when
        when(articleMainService.findArticleByName(testCompanyArticle.getName())).thenReturn(Optional.of(testCompanyArticleMain));
        when(articleMainService.findArticleByName(testIndustryArticle.getName())).thenReturn(Optional.of(testIndustryArticleMain));
        when(articleMainService.registerArticle(testCompanyArticleMain)).thenReturn(testCompanyArticleMain);
        when(articleMainService.registerArticle(testIndustryArticleMain)).thenReturn(testIndustryArticleMain);
        when(companyArticleService.findLatestArticles()).thenReturn(List.of(testCompanyArticle));
        when(companyArticleService.registerArticle(testCompanyArticle)).thenReturn(testCompanyArticle);
        when(industryArticleService.findLatestArticles()).thenReturn(List.of(testIndustryArticle));
        when(industryArticleService.registerArticle(testIndustryArticle)).thenReturn(testIndustryArticle);
        articleMainService.registerArticle(testCompanyArticleMain);
        articleMainService.registerArticle(testIndustryArticleMain);
        companyArticleService.registerArticle(testCompanyArticle);
        industryArticleService.registerArticle(testIndustryArticle);

        // then
        mockMvc.perform(getWithNoParam(""))
                .andExpectAll(status().isOk(),
                        view().name(USER_HOME_VIEW),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT));
    }

    @DisplayName("로그인 페이지 접속")
    @Test
    void accessLogin() throws Exception {
        mockMvc.perform(get(LOGIN_URL))
                .andExpectAll(status().isOk(),
                        view().name(LOGIN_VIEW + VIEW_SHOW),
                        model().attribute("membership", MEMBERSHIP_URL),
                        model().attribute("findId", FIND_ID_URL));
    }

    @DisplayName("아이디 찾기 페이지 접속")
    @Test
    void accessFindId() throws Exception {
        mockMvc.perform(get(FIND_ID_URL))
                .andExpectAll(status().isOk(),
                        view().name(FIND_ID_VIEW + VIEW_PROCESS),
                        model().attributeExists(MEMBER));
    }

    @DisplayName("아이디 찾기 완료 페이지 접속")
    @Test
    void accessFindIdFinish() throws Exception {
        // given
        Member member1 = testMember;
        String commonName = member1.getName();
        LocalDate commonBirthday = member1.getBirthday();
        Member member2 = Member.builder().member(testNewMember).name(commonName).birthday(commonBirthday).build();
        when(memberService.findMembersByNameAndBirthday(commonName, commonBirthday)).thenReturn(List.of(member1, member2));
        when(memberService.registerMember(member1)).thenReturn(member1);
        when(memberService.registerMember(member2)).thenReturn(member2);

        // when
        memberService.registerMember(member1);
        memberService.registerMember(member2);

        // then
        List<String> idList = List.of(member1.getId(), member2.getId());
        String idListForUrl = toStringForUrl(idList);
        String idListString = "idList";

        assertThat(requireNonNull(mockMvc.perform(postWithMultipleParams(FIND_ID_URL, new HashMap<>() {{
                    put(NAME, commonName);
                    put(YEAR, String.valueOf(commonBirthday.getYear()));
                    put(MONTH, String.valueOf(commonBirthday.getMonthValue()));
                    put(DAYS, String.valueOf(commonBirthday.getDayOfMonth()));
                }}))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(FIND_ID_URL + FINISH_URL + ALL_QUERY_STRING))
                .andReturn().getModelAndView()).getModelMap().get(idListString))
                .usingRecursiveComparison()
                .isEqualTo(idListForUrl);

        mockMvc.perform(getWithSingleParam(FIND_ID_URL + FINISH_URL, idListString, idListForUrl))
                .andExpectAll(status().isOk(),
                        view().name(FIND_ID_VIEW + VIEW_FINISH),
                        model().attribute(idListString, idList));
    }

    @DisplayName("쿼리 문자열을 사용하는 아이디 찾기 완료 페이지 접속")
    @Test
    void checkSpecialCharacterLogicInQueryString() throws Exception {
        // given
        Member member1 = Member.builder().member(testMember).id("a1!@#$%^&*()-_+=").build();
        String commonName = member1.getName();
        LocalDate commonBirthday = member1.getBirthday();
        Member member2 = Member.builder().member(testNewMember).id("b2{[}]\\|;:'\"<,>.?/").name(commonName).birthday(commonBirthday).build();
        when(memberService.findMembersByNameAndBirthday(commonName, commonBirthday)).thenReturn(List.of(member1, member2));
        when(memberService.registerMember(member1)).thenReturn(member1);
        when(memberService.registerMember(member2)).thenReturn(member2);

        // when
        memberService.registerMember(member1);
        memberService.registerMember(member2);

        // then
        List<String> idList = List.of(member1.getId(), member2.getId());
        String idListForUrl = toStringForUrl(idList);
        String idListString = "idList";

        assertThat(requireNonNull(mockMvc.perform(postWithMultipleParams(FIND_ID_URL, new HashMap<>() {{
                    put(NAME, commonName);
                    put(YEAR, String.valueOf(commonBirthday.getYear()));
                    put(MONTH, String.valueOf(commonBirthday.getMonthValue()));
                    put(DAYS, String.valueOf(commonBirthday.getDayOfMonth()));
                }}))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(FIND_ID_URL + FINISH_URL + ALL_QUERY_STRING))
                .andReturn().getModelAndView()).getModelMap().get(idListString))
                .usingRecursiveComparison()
                .isEqualTo(idListForUrl);
    }
}