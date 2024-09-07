package springsideproject1.springsideproject1build.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.SerializationUtils;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ModifiableHttpServletRequest extends HttpServletRequestWrapper {

    private final HashMap<String, String[]> paramMap;

    public ModifiableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.paramMap = new HashMap<>(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String[] paramArray = getParameterValues(name);
        return paramArray != null && paramArray.length > 0 ? paramArray[0] : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map getParameterMap() {
        return Collections.unmodifiableMap(paramMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration getParameterNames() {
        return Collections.enumeration(paramMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] paramArray = paramMap.get(name);
        return paramArray != null ? SerializationUtils.clone(paramArray) : null;
    }

    public void setParameter(String name, String value) {
        paramMap.put(name, new String[]{value});
    }
}