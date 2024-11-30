package site.hixview.support.jdbc.callback;

import org.springframework.expression.AccessException;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import site.hixview.aggregate.error.ConfigurationException;
import site.hixview.support.context.OnlyRealRepositoryContext;

import java.util.HashMap;
import java.util.Map;

import static site.hixview.aggregate.vo.ExceptionMessage.NOT_REGISTERED_LISTENER;
import static site.hixview.aggregate.vo.ExceptionMessage.NOT_REPOSITORY_TEST;

public class DynamicTestResetListener implements TestExecutionListener {
    private final Map<String, TestExecutionListener> listeners = new HashMap<>();

    public void addListener(String key, TestExecutionListener listener) {
        listeners.put(key, listener);
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        Class<?> testClass = testContext.getTestClass();
        OnlyRealRepositoryContext annotation = testClass.getAnnotation(OnlyRealRepositoryContext.class);
        if (annotation != null) {
            String listenerKey = annotation.resetMode().name();
            TestExecutionListener listener = listeners.get(listenerKey);
            if (listener != null) {
                listener.beforeTestClass(testContext);
            } else {
                throw new ConfigurationException(NOT_REGISTERED_LISTENER + listenerKey);
            }
        } else {
            throw new AccessException(NOT_REPOSITORY_TEST + testClass.getSimpleName());
        }
    }
}
