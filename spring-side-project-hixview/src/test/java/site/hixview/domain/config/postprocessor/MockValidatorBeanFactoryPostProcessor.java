package site.hixview.domain.config.postprocessor;

import io.micrometer.common.lang.NonNullApi;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

@NonNullApi
public class MockValidatorBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final String BASE_PACKAGE = "site.hixview.domain.validation.validator";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(Validator.class));
        ClassLoader classLoader = this.getClass().getClassLoader();

        for (BeanDefinition validator : scanner.findCandidateComponents(BASE_PACKAGE)) {
            Class<?> clazz;
            try {
                clazz = ClassUtils.forName(Objects.requireNonNull(validator.getBeanClassName()), classLoader);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("해당 검증자 클래스를 불러오는 데 실패하였습니다: " + validator.getBeanClassName());
            }
            beanFactory.registerSingleton(clazz.getSimpleName(), Mockito.mock(clazz));
        }
    }
}
