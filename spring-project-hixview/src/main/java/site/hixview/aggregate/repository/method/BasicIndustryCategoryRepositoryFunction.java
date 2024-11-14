package site.hixview.aggregate.repository.method;

import java.util.List;
import java.util.Optional;

public interface BasicIndustryCategoryRepositoryFunction<T> {
    /**
     * SELECT Category
     */
    List<T> getCategories();
    
    Optional<T> getCategoryByNumber(Long number);

    Optional<T> getCategoryByKoreanName(String koreanName);

    Optional<T> getCategoryByEnglishName(String englishName);

    /**
     * INSERT Category
     */
    Long saveCategory(T category);

    /**
     * UPDATE Category
     */
    void updateCategory(T category);

    /**
     * REMOVE Category
     */
    void deleteCategoryByNumber(Long number);
}
