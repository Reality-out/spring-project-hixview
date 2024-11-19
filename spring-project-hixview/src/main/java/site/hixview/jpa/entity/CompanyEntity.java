package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.COMPANY;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMPANY)
@Getter
@Builder(access = AccessLevel.PUBLIC)
public class CompanyEntity {
    @Id
    @Column(nullable = false)
    private String code;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String korName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String engName;

    @Column(name = NAME_LISTED_SNAKE, length = 80, nullable = false)
    private String nameListed;

    @Column(name = COUNTRY_LISTED_SNAKE, length = 80, nullable = false)
    private String countryListed;

    @Column(length = 80, nullable = false)
    private String scale;

    @Column(nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = FIR_CATE_NUM_SNAKE)
    private FirstCategoryEntity firstCategory;

    @Column(nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = SEC_CATE_NUM_SNAKE)
    private SecondCategoryEntity secondCategory;
}
