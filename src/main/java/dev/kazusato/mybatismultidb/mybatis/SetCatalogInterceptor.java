package dev.kazusato.mybatismultidb.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

@Intercepts(
        @Signature(
                type = StatementHandler.class,
                method = "prepare",
                args = {Connection.class, Integer.class}
        )
)
public class SetCatalogInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetCatalogInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        var catalog = CatalogHolder.getCatalog();
        if (catalog != null) {
            LOGGER.info("Set Catalog: " + catalog);

            var args = invocation.getArgs();
            var conn = (Connection) args[0];
            conn.setCatalog(catalog);
        } else {
            LOGGER.info("No catalog is specified. Skip calling Connection#setCatalog.");
        }

        return invocation.proceed();
    }

}
