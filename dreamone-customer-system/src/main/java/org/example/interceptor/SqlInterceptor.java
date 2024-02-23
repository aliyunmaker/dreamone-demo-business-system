package org.example.interceptor;

import java.sql.Statement;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Component;

/**
 * @author 恬裕
 * @date 2024/1/3
 */
@Slf4j
@Component
@Intercepts({
    @Signature(
        type = StatementHandler.class,
        method = "query",
        args = {Statement.class, ResultHandler.class})
})
public class SqlInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        String sql = ((StatementHandler)invocation.getTarget()).getBoundSql().getSql();
        Object obj = invocation.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        if (executionTime > 3000) {
            log.warn(sql + " Cost: " + executionTime + "ms.");
        } else {
            log.info(sql + " Cost: " + executionTime + "ms.");
        }
        return obj;
    }

}
