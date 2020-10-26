package xyz.zjhwork.utils;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

/** 用于加载application.yml文件的静态配置
 * @Describe:
 * @Author: zjhChester
 * @Date: 15:24 2020/10/12
 */
public class PropertiesUtils {
    /**
     *   配置接收对象
     */
    private static Properties properties;
    /**
     *  当前系统
     */
    public static String currentSystem;
    static{
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("application.yml"));
        properties = yamlPropertiesFactoryBean.getObject();
        if(properties!=null){
        currentSystem = properties.getProperty("tomcat.current-system");
        }
    }
    public static String  getConfig(String config){
        return properties.getProperty(config);
    }
}
