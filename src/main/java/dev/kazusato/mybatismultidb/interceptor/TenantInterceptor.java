package dev.kazusato.mybatismultidb.interceptor;

import dev.kazusato.mybatismultidb.mybatis.CatalogHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class TenantInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantInterceptor.class);

    private static final List<String> VALID_TENANTS = List.of("tokyo", "osaka");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod handlerMethod) {
            var tenantRequired = handlerMethod.getMethod().getAnnotation(TenantRequired.class);

            if (tenantRequired == null) {
                // The target method is not annotated with TenantRequired.
                // Then, clear a ThreadLocal variable.
                CatalogHolder.setCatalog(null);
            } else {
                // get a tenant ID from the URI path.
                var contextPath = request.getContextPath();
                var servletPath = request.getServletPath();
                var requestUri = request.getRequestURI();
                var pathInfo = request.getPathInfo();

                LOGGER.info("ContentPath=" + contextPath + ", ServletPath=" + servletPath
                        + ", RequestURI=" + requestUri + ", PathInfo=" + pathInfo);

                var pathElements = servletPath.split("/");
                // path pattern: /api/v1/{tenantId}/...
                if (pathElements.length < 4) {
                    CatalogHolder.setCatalog(null);
                    throw new RuntimeException("Invalid servlet path pattern: " + servletPath);
                }

                var tenantId = pathElements[3].toLowerCase();

                // validate tenantId
                if (!StringUtils.hasText(tenantId)) {
                    throw new RuntimeException("Tenant ID is not a valid text: [" + tenantId + "]");
                }
                // TODO should check if the tenant ID exists in database but compare with the list defined in this class.
                if (!VALID_TENANTS.contains(tenantId)) {
                    throw new RuntimeException("Invalid tenant ID: [" + tenantId + "]");
                }

                CatalogHolder.setCatalog(tenantId);
            }
        } else {
            // API is not called correctly.
            CatalogHolder.setCatalog(null);
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
