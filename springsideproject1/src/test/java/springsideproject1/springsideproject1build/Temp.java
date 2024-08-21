package springsideproject1.springsideproject1build;

public class Temp {

//    @DisplayName("문자열을 사용하는 기업 기사들 추가 완료 페이지 접속")
//    @Test
//    public void accessCompanyArticleAddWithStringFinish() throws Exception {
//        // given
//        List<String> articleString = createTestStringArticle();
//        List<String> nameList = articleService.registerArticlesWithString(
//                        articleString.getFirst(), articleString.get(1), articleString.getLast())
//                .stream().map(CompanyArticle::getName).collect(Collectors.toList());
//        articleService.removeArticle(createTestEqualDateArticle().getName());
//        articleService.removeArticle(createTestNewArticle().getName());
//
//        // when
//        companyService.registerCompany(createSamsungElectronics());
//
//        // then
//        String nameListForURL = toStringForUrl(nameList);
//        String nameListString = "nameList";
//
//        assertThat(requireNonNull(mockMvc.perform(postWithMultipleParams(ADD_COMPANY_ARTICLE_WITH_STRING_URL, new HashMap<>() {{
//                    put("subjectCompany", articleString.getFirst());
//                    put("articleString", articleString.get(1));
//                    put("linkString", articleString.getLast());
//                }}))
//                .andExpectAll(status().isSeeOther(),
//                        redirectedUrlPattern(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
//                .andReturn().getModelAndView()).getModelMap().get(nameListString))
//                .usingRecursiveComparison()
//                .isEqualTo(nameListForURL);
//
//        assertThat(requireNonNull(mockMvc.perform(getWithSingleParam(ADD_COMPANY_ARTICLE_WITH_STRING_URL + URL_FINISH_SUFFIX,
//                        nameListString, nameListForURL))
//                .andExpectAll(status().isOk(),
//                        view().name(MANAGER_ADD_VIEW + "multipleFinishPage"),
//                        model().attribute(LAYOUT_PATH, ADD_FINISH_PATH),
//                        model().attribute(DATA_TYPE_KOREAN, dataTypeKorValue),
//                        model().attribute(KEY, keyValue),
//                        model().attribute(nameListString, decodeUTF8(nameList)))
//                .andReturn().getModelAndView()).getModelMap().get(nameListString))
//                .usingRecursiveComparison()
//                .isEqualTo(decodeUTF8(nameList));
//    }

//    @DisplayName("기업 기사들 단일 문자열로 동시 등록")
//    @Test
//    public void registerCompanyArticlesWithString() {
//        // given
//        List<String> articleString = createTestStringArticle();
//
//        // then
//        assertThat(articleService.registerArticlesWithString(articleString.getFirst(), articleString.get(1), articleString.getLast()))
//                .usingRecursiveComparison()
//                .ignoringFields("number")
//                .isEqualTo(List.of(createTestEqualDateArticle(), createTestNewArticle()));
//    }

//    @DisplayName("기업 기사 단일 문자열로 중복으로 등록")
//    @Test
//    public void registerDuplicatedCompanyArticleWithString() {
//        // given
//        CompanyArticle article = createTestNewArticle();
//        List<String> articleString = createTestStringArticle();
//
//        // when
//        articleService.registerArticle(article);
//
//        // then
//        AlreadyExistException e = assertThrows(AlreadyExistException.class,
//                () -> articleService.registerArticlesWithString(
//                        articleString.getFirst(), articleString.get(1), articleString.getLast()));
//        assertThat(e.getMessage()).isEqualTo(ALREADY_EXIST_ARTICLE_NAME);
//    }
}
