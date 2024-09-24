package site.hixview.domain.config.postprocessor;

import io.micrometer.common.lang.NonNullApi;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Repository;
import org.springframework.util.ClassUtils;

import java.util.Objects;

@NonNullApi
public class MockRepositoryBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private static final String REPOSITORY_KOREAN = "리포지토리";
    private static final String REPOSITORY_BASE_PACKAGE = "site.hixview.repository.jdbc";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Repository.class));
        ClassLoader classLoader = this.getClass().getClassLoader();

        for (BeanDefinition validator : scanner.findCandidateComponents(REPOSITORY_BASE_PACKAGE)) {
            Class<?> clazz;
            try {
                clazz = ClassUtils.forName(Objects.requireNonNull(validator.getBeanClassName()), classLoader);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("해당 " + REPOSITORY_KOREAN + " 클래스를 불러오는 데 실패하였습니다: " + validator.getBeanClassName());
            }
            beanFactory.registerSingleton(clazz.getSimpleName(), Mockito.mock(clazz));
        }
    }
}
