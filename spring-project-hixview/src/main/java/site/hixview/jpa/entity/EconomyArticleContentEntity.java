package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import site.hixview.jpa.entity.type.MapperTable;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = ECON_ARTI_CONT_MAPPER_SNAKE)
@MapperTable
@Getter
@NoArgsConstructor
public class EconomyArticleContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private EconomyArticleEntity economyArticle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = ECON_CONT_NUM_SNAKE)
    private EconomyContentEntity economyContent;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EconomyArticleContentEntity economyArticleContentMapper = (EconomyArticleContentEntity) obj;
        return new EqualsBuilder()
                .append(getEconomyArticle().getName(), economyArticleContentMapper.getEconomyArticle().getName())
                .append(getEconomyContent().getName(), economyArticleContentMapper.getEconomyContent().getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getEconomyArticle().getName())
                .append(getEconomyContent().getName())
                .toHashCode();
    }

    public EconomyArticleContentEntity(EconomyArticleEntity economyArticle, EconomyContentEntity economyContent, Long versionNumber) {
        this.economyArticle = economyArticle;
        this.economyContent = economyContent;
        this.versionNumber = versionNumber;
    }

    public EconomyArticleContentEntity(EconomyArticleEntity economyArticle, EconomyContentEntity economyContent) {
        this.economyArticle = economyArticle;
        this.economyContent = economyContent;
    }

    public void updateArticle(EconomyArticleEntity article) {
        this.economyArticle = article;
    }

    public void updateEconomyContent(EconomyContentEntity economyContent) {
        this.economyContent = economyContent;
    }
}
