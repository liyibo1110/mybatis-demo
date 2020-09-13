package com.github.liyibo1110.mybatis.executor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetHandler {

    public <T> T handle(ResultSet rs, Class type) {

        Object obj = null;
        try {
            obj = type.newInstance();
            if(rs.next()) {
                for(Field field : obj.getClass().getDeclaredFields()) {
                    setValue(obj, field, rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T)obj;
    }

    /**
     * 通过反射给字段赋值
     * @param obj
     * @param field
     * @param rs
     */
    private void setValue(Object obj, Field field, ResultSet rs) {

        try {
            Method m = obj.getClass().getMethod("set" + firstWordCapital(field.getName()));
            m.invoke(obj, getResult(rs, field));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射判断类型，从rs中取得特定类型的值
     * @param rs
     * @param field
     * @return
     */
    private Object getResult(ResultSet rs, Field field) throws SQLException {

        Class type = field.getType();
        String columnLabel = humpToUnderline(field.getName());
        if(Integer.class == type) {
            return rs.getInt(columnLabel);
        }else if(Long.class == type) {
            return rs.getLong(columnLabel);
        }else if(Boolean.class == type) {
            return rs.getBoolean(columnLabel);
        }else if(Double.class == type) {
            return rs.getDouble(columnLabel);
        }else {
            return rs.getString(columnLabel);
        }
    }

    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public static String humpToUnderline(String str) {

        StringBuilder sb = new StringBuilder(str);
        int extraLength = 0;
        if(!str.contains("_")) {
            for (int i = 0; i < str.length(); i++) {
                if(Character.isUpperCase(str.charAt(i))) {
                    sb.insert(i + extraLength, "_");
                    extraLength++;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    private String firstWordCapital(String word) {

        String first = word.substring(0, 1);
        String tail = word.substring(1);
        return first.toUpperCase() + tail;
    }
}
