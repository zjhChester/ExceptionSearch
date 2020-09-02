package xyz.zjhwork.springApplicationStarter;

import lombok.Builder;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;
@Builder
public class BootStarter {
    public  void run(){
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("application.yml"));
        Properties properties = yamlPropertiesFactoryBean.getObject();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(properties.getProperty("tomcat.port")));
        // 标识tomcat启动为webapp
        tomcat.addWebapp(properties.get("tomcat.context-path").toString(),properties.get("tomcat.doc-base").toString());
        try {
//            tomcat启动
            tomcat.start();
//            tomcat监听用户接入
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BootStarter.builder().build().run();
    }
}
