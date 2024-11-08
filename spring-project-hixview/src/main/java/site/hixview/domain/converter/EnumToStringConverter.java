package site.hixview.domain.converter;

import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;

import java.util.*;

public class EnumToStringConverter<T extends Enum<T>> implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> convertiblePairs = new HashSet<>();
        for (Class<T> enumClass: getEnumClass()) {
            convertiblePairs.add(new ConvertiblePair(enumClass, String.class));
        }
        return convertiblePairs;
    }

    @Override
    public Object convert(Object source, @NonNull TypeDescriptor sourceType, @NonNull TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        String string = source.toString().trim();
        Class<T> sourceEnumType = (Class<T>) Objects.requireNonNull(sourceType.getElementTypeDescriptor()).getType();
        return Enum.valueOf(sourceEnumType, string).name();
    }

    private List<Class<T>> getEnumClass() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(Enum.class));
        ClassLoader classLoader = this.getClass().getClassLoader();
        List<Class<T>> returnClassList = new ArrayList<>();

        for (BeanDefinition enumDef : scanner.findCandidateComponents("site.hixview.domain.entity")) {
            try {
                returnClassList.add((Class<T>) ClassUtils.forName(Objects.requireNonNull(enumDef.getBeanClassName()), classLoader));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("해당 enum type을 불러오는 데 실패하였습니다: " + enumDef.getBeanClassName());
            }
        }

        return returnClassList;
    }
}