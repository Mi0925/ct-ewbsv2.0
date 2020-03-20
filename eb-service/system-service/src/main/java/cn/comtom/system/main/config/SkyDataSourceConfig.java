package cn.comtom.system.main.config;

import cn.comtom.system.fw.SystemMapper;
import cn.comtom.system.main.SystemApplication;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库配置
 * @author guomao
 */

@Configuration
@MapperScan(
        basePackageClasses = {SystemApplication.class},
        markerInterface = SystemMapper.class,
        mapperHelperRef = "skyMapperHelper",
        sqlSessionTemplateRef = "skySqlSessionTemplate"
)
public class SkyDataSourceConfig {

    @Bean(name = "skyDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.sky")
//    @Primary
    public DataSource setDataSource() {
    	HikariDataSource hikariDataSource = new HikariDataSource();
    	hikariDataSource.setMinimumIdle(5);
    	hikariDataSource.setMaximumPoolSize(15);
    	hikariDataSource.setAutoCommit(true);
    	hikariDataSource.setIdleTimeout(30000);
    	hikariDataSource.setMaxLifetime(1800000);
    	hikariDataSource.setConnectionTimeout(30000);
        return hikariDataSource;
    }

    @Bean(name = "skyTransactionManager")
//    @Primary
    public DataSourceTransactionManager setTransactionManager(@Qualifier("skyDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "skySqlSessionFactory")
//    @Primary
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("skyDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("cn.comtom.cbs.application.user.domain");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
        conf.setMapUnderscoreToCamelCase(true);
        conf.setLogImpl(Slf4jImpl.class);
        bean.setConfiguration(conf);
        return bean.getObject();
    }

    @Bean(name = "skySqlSessionTemplate")
//    @Primary
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("skySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    //其他

    @Bean
//    @Primary
    public MapperHelper skyMapperHelper() {
        Config config = new Config();
        List<Class> mappers = new ArrayList<>();
        mappers.add(SystemMapper.class);
        config.setMappers(mappers);

        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.setConfig(config);
        return mapperHelper;
    }

}