package com.github.liyibo1110.mybatis.session;

import com.github.liyibo1110.mybatis.annotation.Entity;
import com.github.liyibo1110.mybatis.annotation.Select;
import com.github.liyibo1110.mybatis.binding.MapperRegistry;
import com.github.liyibo1110.mybatis.executor.CachingExecutor;
import com.github.liyibo1110.mybatis.executor.Executor;
import com.github.liyibo1110.mybatis.executor.SimpleExecutor;
import com.github.liyibo1110.mybatis.plugin.Interceptor;
import com.github.liyibo1110.mybatis.plugin.InterceptorChain;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Configuration {

    public static final ResourceBundle sqlMappings;
    public static final ResourceBundle properties;

    /**
     * 维护Mapper类型和代理工厂类映射
     */
    public static final MapperRegistry MAPPER_REGISTRY = new MapperRegistry();

    /**
     *维护Mapper接口方法全名和实际SQL的映射
     */
    public static final Map<String, String> mappedStatements = new HashMap<>();

    /**
     * 插件用的拦截器责任链
     */
    private InterceptorChain interceptorChain = new InterceptorChain();

    /**
     * 所有Mapper接口的Class集合
     */
    private List<Class<?>> mapperList = new ArrayList<>();

    /**
     * 所有扫描的类文件名集合（因为是单例所以可以随便存）
     */
    private List<String> classPaths = new ArrayList<>();

    static{
        sqlMappings = ResourceBundle.getBundle("sql");
        properties = ResourceBundle.getBundle("mybatis");
    }

    public Configuration() {
        // 1.解析sql.properties
        for(String key : sqlMappings.keySet()) {
            // SQL语句
            String statement = sqlMappings.getString(key).split("--")[0];
            // 结果集对应的POJO类型
            String resultClassName = sqlMappings.getString(key).split("--")[1];
            Class mapper = null;
            Class resultType = null;
            try {
                // 查询接口的Class对象
                mapper = Class.forName(key.substring(0, key.lastIndexOf(".")));
                // 结果集对应的POJO的Class对象
                resultType = Class.forName(resultClassName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            MAPPER_REGISTRY.addMapper(mapper, resultType);
            mappedStatements.put(key, statement);
        }
        // 2.扫描并解析Mapper接口配置
        String mapperPath = properties.getString("mapper.path");
        scanPackage(mapperPath);
        for(Class<?> mapper : mapperList) {
            parsingClass(mapper);
        }
        // 3.解析插件
        String pluginPathValue = properties.getString("plugin.path");
        String[] pluginPaths = pluginPathValue.split(",");
        if(pluginPaths != null) {
            // 全部添加到interceptorChain中
            for(String plugin : pluginPaths) {
                Interceptor interceptor = null;
                try {
                    interceptor = (Interceptor)Class.forName(plugin).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                interceptorChain.addInterceptor(interceptor);
            }
        }
    }

    public boolean hasStatement(String statement) {
        return mappedStatements.containsKey(statement);
    }

    public String getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public <T> T getMapper(Class<T> clazz, DefaultSqlSession sqlSession) {
        return MAPPER_REGISTRY.getMapper(clazz, sqlSession);
    }

    public Executor newExecutor() {
        Executor executor = null;
        if(properties.getString("cache.enabled").equals(true)) {
            executor = new CachingExecutor(new SimpleExecutor());
        }else {
            executor = new SimpleExecutor();
        }

        // 尝试对Executor进行插件增强
        if(interceptorChain.hasPlugin()) {
            return (Executor)interceptorChain.pluginAll(executor);
        }
        return executor;
    }

    /**
     * 解析Mapper接口上配置的注解（即SQL语句）
     * @param mapper
     */
    private void parsingClass(Class<?> mapper) {

        // 解析寻找Entity注解
        if(mapper.isAnnotationPresent(Entity.class)) {
            for(Annotation annotation : mapper.getAnnotations()) {
                if(Entity.class.equals(annotation.annotationType())) {
                    // 直接存入接口Class和类注解配置的结果Class类型
                    MAPPER_REGISTRY.addMapper(mapper, ((Entity)annotation).value());
                }
            }
        }

        // 解析寻找Select注解
        Method[] methods = mapper.getMethods();
        for(Method method : methods) {
            if(method.isAnnotationPresent(Select.class)) {
                for(Annotation annotation : method.getAnnotations()) {
                    if(Select.class.equals(annotation.annotationType())) {
                        String statement = method.getDeclaringClass().getName() + "." + method.getName();
                        mappedStatements.put(statement, ((Select)annotation).value());
                    }
                }
            }
        }
    }

    private void scanPackage(String mapperPath) {

        String classPath = getClass().getResource("../").getPath();
        mapperPath = mapperPath.replace(".", File.separator);
        String path = classPath + mapperPath;
        doPath(new File(path));
        for(String className : classPaths) {
            // 目录分隔符转换为类分隔符
            className = className.replace(classPath.replace("/","\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(clazz.isInterface()) mapperList.add(clazz);
        }
    }

    private void doPath(File file) {
        if(file.isDirectory()) {
            for(File f : file.listFiles()) doPath(f);
        }else {
            // 文件则添加
            if(file.getName().endsWith(".class")) classPaths.add(file.getPath());
        }
    }
}
