package site.hixview.domain.vo.name;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EntityName {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Article {
        public static final String ARTICLE = "article";
        public static final String NUMBER = "number";
        public static final String PRESS = "press";
        public static final String SUBJECT_COMPANY = "subjectCompany";
        public static final String SUBJECT_FIRST_CATEGORY = "subjectFirstCategory";
        public static final String SUBJECT_SECOND_CATEGORY = "subjectSecondCategory";
        public static final String DATE = "date";
        public static final String LINK = "link";
        public static final String IMPORTANCE = "importance";
        public static final String IMAGE_PATH = "imagePath";
        public static final String SUMMARY = "summary";
        public static final String ARTICLE_CLASS_NAME = "classification";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Company {
        public static final String COMPANY = "company";
        public static final String CODE = "code";
        public static final String COUNTRY = "country";
        public static final String SCALE = "scale";
        public static final String FIRST_CATEGORY = "firstCategory";
        public static final String SECOND_CATEGORY = "secondCategory";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Member {
        public static final String MEMBER = "member";
        public static final String IDENTIFIER = "identifier";
        public static final String ID = "id";
    }
}
