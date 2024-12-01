package site.hixview.support.jdbc.callback;

import io.micrometer.common.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(DynamicTestResetListener.class);

    public void addListener(String key, TestExecutionListener listener) {
        listeners.put(key, listener);
    }

    @Override
    public void beforeTestClass(@NonNull TestContext testContext) throws Exception {
        TestExecutionListener listener = getTestResetListener(testContext);
        if (listener instanceof TestResetTableListener) {
            listener.beforeTestClass(testContext);
        }
    }

    @Override
    public void afterTestMethod(@NonNull TestContext testContext) throws Exception {
        TestExecutionListener listener = getTestResetListener(testContext);
        if (listener instanceof TestResetTableListener) {
            listener.afterTestMethod(testContext);
        }
    }
    
    @Override
    public void afterTestClass(@NonNull TestContext testContext) throws Exception {
        TestExecutionListener listener = getTestResetListener(testContext);
        if (listener instanceof TestResetAutoIncrementListener) {
            listener.afterTestClass(testContext);
        }
    }

    private TestExecutionListener getTestResetListener(TestContext testContext) throws AccessException {
        Class<?> testClass = testContext.getTestClass();
        OnlyRealRepositoryContext annotation = testClass.getAnnotation(OnlyRealRepositoryContext.class);
        validateAnnotation(annotation, testClass.getSimpleName());
        String listenerKey = annotation.resetMode().name();
        TestExecutionListener listener = listeners.get(listenerKey);
        if (listener == null) {
            throw new ConfigurationException(NOT_REGISTERED_LISTENER + listenerKey);
        }
        return listener;
    }

    private void validateAnnotation(OnlyRealRepositoryContext annotationOrEmpty, String testClass) throws AccessException {
        if (annotationOrEmpty == null) {
            throw new AccessException(NOT_REPOSITORY_TEST + testClass);
        }
    }
}
