package cn.comtom.core.main.config;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * @author wangbingyan
 * 引入FastDFS配置
 */
@Configuration
@Import(FdfsClientConfig.class)
//Jmx重复注册问题
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class DfsConfig {
}
