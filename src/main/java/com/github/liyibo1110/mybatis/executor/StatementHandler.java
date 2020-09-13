package com.github.liyibo1110.mybatis.executor;

import com.github.liyibo1110.mybatis.parameter.ParameterHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementHandler {

    private ResultSetHandler resultSetHandler = new ResultSetHandler();

    public <T> T query(String statement, Object[] parameter, Class type) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        Object result = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(statement);
            ParameterHandler parameterHandler = new ParameterHandler(pstmt);
            parameterHandler.setParameters(parameter);
            pstmt.execute();
            result = resultSetHandler.handle(pstmt.getResultSet(), type);
            return (T)result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private Connection getConnection() {
        return null;
    }
}
