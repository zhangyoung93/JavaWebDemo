package com.zy.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * mybatis配置文件
 * springboot+mysql+druid
 * @author zy
 */
@Configuration
@MapperScan(basePackages = MysqlDruidConfig.DAO_LOCATION,sqlSessionFactoryRef = MysqlDruidConfig.FACTORY_REF)
public class MysqlDruidConfig {

    //dao路径
    static final String DAO_LOCATION = "com.zy.demo.dao.mysql";
    //mapper路径
    static final String MAPPER_LOCATION = "classpath:mapper/mysql/*.xml";
    //mysql的sqlSessionFactory引用
    static final String FACTORY_REF = "mysqlSqlSessionFactory";

    /**
     * 配置mysql数据源并集成druid实现数据库连接池
     * @param env spring上下文环境变量
     * @return 数据源
     */
    @Primary
    @Bean("mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource mysqlDataSource(Environment env){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        druidDataSource.setUrl(env.getProperty("spring.datasource.url"));
        druidDataSource.setUsername(env.getProperty("spring.datasource.username"));
        druidDataSource.setPassword(env.getProperty("spring.datasource.password"));
        return druidDataSource;
    }

    /**
     * 配置mysql事务
     * @param mysqlDataSource mysql数据源
     * @return 数据源事务管理
     */
    @Primary
    @Bean("mysqlTransactionManager")
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DataSource mysqlDataSource){
        return new DataSourceTransactionManager(mysqlDataSource);
    }

    /**
     * 配置mysql的sqlSessionFactory
     * @param mysqlDataSource mysql数据源
     * @return sqlSessionFactory
     * @throws Exception 异常
     */
    @Primary
    @Bean(MysqlDruidConfig.FACTORY_REF)
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource mysqlDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        //设置mysql数据源
        sqlSessionFactoryBean.setDataSource(mysqlDataSource);
        //设置mapper位置
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MysqlDruidConfig.MAPPER_LOCATION));
        //设置pageHelper插件
        Properties properties = new Properties();
        //指定pageHelper插件使用的数据库方言
        properties.setProperty("helperDialect", "mysql");
        //开启合理化分页模式。当pageNum<=0时查询第一页；当pageNum>总页数时查询最后一页。
        properties.setProperty("reasonable", "true");
        //定义分页插件拦截器
        PageInterceptor pageInterceptor = new PageInterceptor();
        //设置分页拦截器的属性
        pageInterceptor.setProperties(properties);
        //设置mybatis使用的插件
        sqlSessionFactoryBean.setPlugins(pageInterceptor);
        //返回sqlSessionFactory对象
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置mysql的sqlSessionTemplate
     * @param mysqlSqlSessionFactory mysql的sqlSessionFactory
     * @return sqlSessionTemplate
     */
    @Primary
    @Bean("mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier(MysqlDruidConfig.FACTORY_REF) SqlSessionFactory mysqlSqlSessionFactory){
        return new SqlSessionTemplate(mysqlSqlSessionFactory);
    }
}
