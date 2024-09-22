package site.hixview.domain.postprocessor;

import io.micrometer.common.lang.NonNullApi;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import site.hixview.domain.service.*;

import java.util.List;

import static org.mockito.Mockito.mock;

@NonNullApi
public class MockServiceBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (Class<?> serviceClass : List.of(CompanyArticleService.class, IndustryArticleService.class, ArticleMainService.class,
                CompanyService.class, MemberService.class)) {
            beanFactory.registerSingleton(serviceClass.getSimpleName(), mock(serviceClass));
        }
    }
}
