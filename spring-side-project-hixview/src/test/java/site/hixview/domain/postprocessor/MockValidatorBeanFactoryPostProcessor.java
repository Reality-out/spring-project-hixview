package site.hixview.domain.postprocessor;

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
import java.util.Set;

@NonNullApi
public class MockValidatorBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final String BASE_PACKAGE = "site.hixview.domain.validation.validator";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(Validator.class));
        Set<BeanDefinition> validators = scanner.findCandidateComponents(BASE_PACKAGE);
        ClassLoader classLoader = this.getClass().getClassLoader();

        for (BeanDefinition validator : validators) {
            String validatorName = validator.getBeanClassName();
            Class<?> clazz;
            try {
                clazz = ClassUtils.forName(Objects.requireNonNull(validatorName), classLoader);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            Object mockedBean = Mockito.mock(clazz);
            beanFactory.registerSingleton(clazz.getSimpleName(), mockedBean);
        }
    }
}
