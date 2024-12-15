package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = PRESS)
@Getter
@NoArgsConstructor
public class PressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String koreanName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String englishName;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PressEntity press = (PressEntity) obj;
        return new EqualsBuilder()
                .append(getKoreanName(), press.getKoreanName())
                .append(getEnglishName(), press.getEnglishName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getKoreanName())
                .append(getEnglishName())
                .toHashCode();
    }

    public PressEntity(Long number, String koreanName, String englishName) {
        this.number = number;
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    public PressEntity(String koreanName, String englishName, Long versionNumber) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.versionNumber = versionNumber;
    }

    public PressEntity(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    public void updateKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public void updateEnglishName(String englishName) {
        this.englishName = englishName;
    }
}
