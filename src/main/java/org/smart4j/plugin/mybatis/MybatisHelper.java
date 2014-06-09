package org.smart4j.plugin.mybatis;

import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.core.ClassHelper;
import org.smart4j.framework.core.ConfigHelper;
import org.smart4j.framework.dao.DatabaseHelper;
import org.smart4j.framework.plugin.Plugin;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * 支持注解和包配置
 * <p/>
 * Created by liuzh on 14-4-8.
 */
public class MybatisHelper implements Plugin {
    private static final Logger logger = LoggerFactory.getLogger(MybatisHelper.class);
    /**
     * 包扫描路径,逗号隔开
     */
    public static final String MYBATIS_MAPPER_PACKAGE = "mybatis.mapper.package";

    /**
     * Mybatis - xml文件,多个文件逗号隔开,文件类型必须带.xml后缀,文件夹不能带后缀
     */
    public static final String MYBATIS_MAPPER_XML = "mybatis.mapper.xml";

    /**
     * Mybatis 别名配置 mybatis.aliases.name为单独的类，mybatis.aliases.package为包
     */
    public static final String MYBATIS_ALIASES = "mybatis.aliases.";
    public static final String MYBATIS_ALIASES_PACKAGE = "mybatis.aliases.package";
    public static final String MYBATIS_ALIASES_NAME = "mybatis.aliases.name.";

    public static final String MYBATIS_PLUGIN = "mybatis.plugin.";

    /**
     * Mybatis 日志
     */
    public static final String MYBATIS_LOGIMPL = "mybatis.logImpl";


    private static SqlSessionFactory sqlSessionFactory;

    private static ThreadLocal<SqlSession> localSession = new ThreadLocal<SqlSession>();

    @Override
    public void init() {
        DataSource dataSource = DatabaseHelper.getDataSource();
        if (dataSource != null) {
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("smart_database", transactionFactory, dataSource);
            Configuration configuration = new Configuration(environment);

            //Mybatis日志配置
            String logImpl = ConfigHelper.getString(MYBATIS_LOGIMPL);
            if (logImpl != null && !logImpl.equals("")) {
                configuration.setLogImpl(configuration.getTypeAliasRegistry().resolveAlias(logImpl));
            }

            //Aliases别名
            Map<String, Object> aliases = ConfigHelper.getMap(MYBATIS_ALIASES);
            registerAliases(aliases, configuration);

            //加载插件
            Map<String, Object> plugins = ConfigHelper.getMap(MYBATIS_PLUGIN);
            registerPlugins(plugins, configuration);

            //注册xml
            String _resource = ConfigHelper.getString(MYBATIS_MAPPER_XML);
            if (StringUtil.isNotEmpty(_resource)) {
                String[] resources = StringUtil.splitString(_resource, ",");
                for (String res : resources) {
                    try {
                        registerXml(res, configuration);
                    } catch (Exception e) {
                        logger.debug("注册xml[" + res + "]失败，失败信息:" + e.getMessage());
                    }
                }
            }

            //注册包
            String _package = ConfigHelper.getString(MYBATIS_MAPPER_PACKAGE);
            if (StringUtil.isNotEmpty(_package)) {
                String[] packages = StringUtil.splitString(_package, ",");
                for (String pk : packages) {
                    try {
                        configuration.addMappers(pk);
                    } catch (Exception e) {
                        logger.debug("注册包[" + pk + "]失败，失败信息:" + e.getMessage());
                    }
                }
            }

            //注册注解
            List<Class<?>> mappers = ClassHelper.getClassListByAnnotation(Mapper.class);
            if (CollectionUtil.isNotEmpty(mappers)) {
                for (Class<?> clazz : mappers) {
                    try {
                        Mapper mapper = clazz.getAnnotation(Mapper.class);
                        String xml = null;
                        if (mapper != null) {
                            xml = mapper.value();
                            if (xml != null && !xml.equals("")) {
                                registerXml(xml, configuration);
                            }
                        }
                        //XML和接口只注册一个
                        if (StringUtil.isEmpty(xml)) {
                            configuration.addMapper(clazz);
                        }
                    } catch (Exception e) {
                        logger.error("注解Mapper类[" + clazz.getCanonicalName() + "]出错:" + e.getMessage());
                    }
                }
            }

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

            logger.info("smart4j-plugin-mybatis 加载成功");
            logger.info("author:         abel533/isea533");
            logger.info("gitosc:         http://git.oschina.net/free");
            logger.info("blog_csdn:      http://blog.csdn.net/isea533");
            logger.info("blog_osc:       http://my.oschina.net/flags/blog");
            logger.info("Mybatis专栏:   　http://blog.csdn.net/column/details/mybatis-sample.html");
            logger.info("Mybatis贴吧:   　http://tieba.baidu.com/f?kw=mybatis\n");
        }
    }

    /**
     * 注册别名
     *
     * @param aliases
     * @param configuration
     */
    private static void registerAliases(Map<String, Object> aliases, Configuration configuration) {
        for (Iterator<Map.Entry<String, Object>> iterator = aliases.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> kV = iterator.next();
            String name = kV.getKey();
            String value = (String) kV.getValue();
            if (name.startsWith(MYBATIS_ALIASES_PACKAGE)) {
                //String typeAliasPackage = name.substring(MYBATIS_ALIASES_PACKAGE.length());
                configuration.getTypeAliasRegistry().registerAliases(value);
            } else {
                String alias = null;
                String type = value;
                try {
                    alias = name.substring(MYBATIS_ALIASES_NAME.length());
                } catch (Exception e) {
                }
                try {
                    Class<?> clazz = Resources.classForName(type);
                    //测试多个mybatis.aliases.name.
                    if (alias == null || alias.length() == 0) {
                        configuration.getTypeAliasRegistry().registerAlias(clazz);
                    } else {
                        configuration.getTypeAliasRegistry().registerAlias(alias, clazz);
                    }
                } catch (ClassNotFoundException e) {
                    throw new BuilderException("注册别名 '" + alias + "' 异常. 原因: " + e, e);
                }
            }
        }
    }

    /**
     * 注册插件
     *
     * @param plugins
     * @param configuration
     */
    private static void registerPlugins(Map<String, Object> plugins, Configuration configuration) {
        Map<String, Properties> propertiesMap = new HashMap<String, Properties>();
        //处理配置参数
        for (Iterator<Map.Entry<String, Object>> iterator = plugins.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Object> kV = iterator.next();
            String name = kV.getKey();
            String value = (String) kV.getValue();
            if (name.startsWith(MYBATIS_PLUGIN)) {
                String pluginName = name.substring(MYBATIS_PLUGIN.length());
                String propertyName = null;
                if (pluginName.indexOf('.') > 0) {
                    propertyName = pluginName.substring(pluginName.indexOf('.') + 1);
                    pluginName = pluginName.substring(0, pluginName.indexOf('.') + 1);
                }
                if (propertiesMap.get(pluginName) == null) {
                    propertiesMap.put(pluginName, new Properties());
                }
                //存放属性值
                if (propertyName != null && propertyName.length() > 0) {
                    propertiesMap.get(pluginName).put(propertyName, value);
                }
            }
        }
        //注册插件
        for (Iterator<Map.Entry<String, Properties>> iterator = propertiesMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Properties> kV = iterator.next();
            String name = kV.getKey();
            Properties properties = (Properties) kV.getValue();
            String clazz = (String) plugins.get(MYBATIS_PLUGIN + name);
            if (clazz != null && clazz.length() > 0) {
                Interceptor interceptorInstance = null;
                try {
                    interceptorInstance = (Interceptor) Class.forName(clazz).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                interceptorInstance.setProperties(properties);
                configuration.addInterceptor(interceptorInstance);
            }
        }
    }

    /**
     * 注册xml
     *
     * @param res
     * @param configuration
     */
    private static void registerXml(String res, Configuration configuration) {
        if (res.toLowerCase().endsWith(".xml")) {
            InputStream inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream(res);
                XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, res, configuration.getSqlFragments());
                mapperParser.parse();
            } catch (Exception e) {
                logger.error(e.getMessage());
                //不处理
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        //不处理
                        logger.error(e.getMessage());
                    }
                }
            }
        } else {
            try {
                URL url = Resources.getResourceURL(res);
                String resUrl = res;
                if (!resUrl.endsWith("/")) {
                    resUrl += "/";
                }
                if (url != null && url.getPath() != null) {
                    File[] files = new File(url.getPath()).listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            if (name.toLowerCase().endsWith(".xml")) {
                                return true;
                            }
                            return false;
                        }
                    });
                    for (File file : files) {
                        registerXml(resUrl + file.getName(), configuration);
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取SqlSession - 如果不存在则创建一个
     *
     * @return
     */
    public static SqlSession getSqlSession() {
        return getSqlSession(true);
    }

    /**
     * 获取Session
     *
     * @param create 当sqlSession不存在的时候，如果true则创建一个，如果false返回null
     * @return
     */
    public static SqlSession getSqlSession(boolean create) {
        if (sqlSessionFactory == null) {
            return null;
        }
        SqlSession sqlSession = localSession.get();
        if (sqlSession == null && create) {
            sqlSession = sqlSessionFactory.openSession();
            localSession.set(sqlSession);
        }
        return sqlSession;
    }

    /**
     * 获取Mapper
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getMapper(Class<T> type) {
        SqlSession sqlSession = getSqlSession();
        if (sqlSession == null) {
            return (T) null;
        }
        return (T) sqlSession.getMapper(type);
    }

    /**
     * 关闭Session
     */
    public static void closeSession() {
        if (sqlSessionFactory != null) {
            SqlSession sqlSession = getSqlSession(false);
            if (sqlSession != null) {
                sqlSession.close();
                //移出session
                localSession.remove();
                logger.debug("Mybatis SqlSession - 关闭");
            }
        }
    }

    /**
     * 回滚
     */
    public static void rollback() {
        if (sqlSessionFactory != null) {
            SqlSession sqlSession = getSqlSession(false);
            if (sqlSession != null) {
                sqlSession.rollback();
                logger.debug("Mybatis SqlSession - 回滚");
            }
        }
    }

    @Override
    public void destroy() {

    }
}
