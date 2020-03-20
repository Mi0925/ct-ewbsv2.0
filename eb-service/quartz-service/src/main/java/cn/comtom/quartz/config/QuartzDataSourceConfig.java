package cn.comtom.quartz.config;

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
 * 定时任务数据源配置
 * @author huhailang
 */
@Configuration
@MapperScan(value = "tk.mybatis.mapper.annotation",
	//basePackages = "cn.comtom.app.standard.component.quartz.mapper",
	//markerInterface = JobMapper.class,
	mapperHelperRef = "quartzMapperHelper",
	sqlSessionTemplateRef = "quartzSqlSessionTemplate"
)
public class QuartzDataSourceConfig {

	@Bean(name = "quartzDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.quartz")
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

    @Bean(name = "quartzTransactionManager")
    public DataSourceTransactionManager setTransactionManager(@Qualifier("quartzDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "quartSqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory(@Qualifier("quartzDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("cn.comtom.quartz.model.dbo");
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml"));
        org.apache.ibatis.session.Configuration conf = new org.apache.ibatis.session.Configuration();
        conf.setMapUnderscoreToCamelCase(true);
        conf.setLogImpl(Slf4jImpl.class);
        bean.setConfiguration(conf);
        return bean.getObject();
    }

    @Bean(name = "quartzSqlSessionTemplate")
    public SqlSessionTemplate setSqlSessionTemplate(@Qualifier("quartSqlSessionFactory") SqlSessionFactory sqlSessionFactory)  {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public MapperHelper quartzMapperHelper() {
        Config config = new Config();
        List<Class> mappers = new ArrayList<>();
        config.setMappers(mappers);

        MapperHelper mapperHelper = new MapperHelper();
        mapperHelper.setConfig(config);
        return mapperHelper;
    }

}