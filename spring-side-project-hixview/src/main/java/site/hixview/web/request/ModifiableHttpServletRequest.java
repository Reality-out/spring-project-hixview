package site.hixview.web.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Setter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ModifiableHttpServletRequest extends HttpServletRequestWrapper {

    private final HashMap<String, String[]> paramMap;

    @Setter
    private String requestURI;

    @Setter
    private StringBuffer requestURL;

    public ModifiableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.paramMap = new HashMap<>(request.getParameterMap());
        this.requestURI = request.getRequestURI();
        this.requestURL = request.getRequestURL();
    }

    @Override
    public String getParameter(String name) {
        String[] paramArray = getParameterValues(name);
        return paramArray != null && paramArray.length > 0 ? paramArray[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(paramMap);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(paramMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] paramArray = paramMap.get(name);
        return paramArray != null ? paramArray.clone() : null;
    }

    public void setParameter(String name, String value) {
        paramMap.put(name, new String[]{value});
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        return requestURL;
    }
}