package site.hixview.aggregate.repository;

import site.hixview.aggregate.domain.Press;

import java.util.List;
import java.util.Optional;

public interface PressRepository {
    /**
     * SELECT Press
     */
    List<Press> getPresses();
    
    Optional<Press> getPressByNumber(Long number);

    Optional<Press> getPressByKoreanName(String koreanName);

    Optional<Press> getPressByEnglishName(String englishName);
    
    /**
     * INSERT Press
     */
    Long savePress(Press press);

    /**
     * UPDATE Press
     */
    void updatePress(Press press);

    /**
     * REMOVE Press
     */
    void deletePressByNumber(Long number);
}
