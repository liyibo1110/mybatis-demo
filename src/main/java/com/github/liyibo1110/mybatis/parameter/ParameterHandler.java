package com.github.liyibo1110.mybatis.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterHandler {

    private PreparedStatement pstmt;

    public ParameterHandler(PreparedStatement pstmt) {
        this.pstmt = pstmt;
    }

    /**
     * 用实际参数填充ps里面的占位符
     * @param parameters
     */
    public void setParameters(Object[] parameters) {
        try {
            for (int i = 0; i < parameters.length; i++) {
                int k = i + 1;
                if(parameters[i] instanceof Integer) {
                    pstmt.setInt(k, (Integer)parameters[i]);
                }else if(parameters[i] instanceof Long) {
                    pstmt.setLong(k, (Long)parameters[i]);
                }else if(parameters[i] instanceof Boolean) {
                    pstmt.setBoolean(k, (Boolean)parameters[i]);
                }else {
                    pstmt.setString(k, String.valueOf(parameters[i]));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
