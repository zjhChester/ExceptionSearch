package xyz.zjhwork.springApplicationStarter;

import lombok.Builder;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import java.util.Properties;
@Builder
public class BootStarter {
    private static YamlPropertiesFactoryBean yamlPropertiesFactoryBean ;
    private static Properties properties;
    private static String currentSystem;
    static{
        yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("application.yml"));
        properties = yamlPropertiesFactoryBean.getObject();
        currentSystem = properties.getProperty("tomcat.current-system");
    }
    public  void run(){
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(properties.getProperty("config."+currentSystem+".port")));
        // 标识tomcat启动为webapp
        tomcat.addWebapp(properties.get("config."+currentSystem+".context-path").toString(),properties.get("config."+currentSystem+".doc-base").toString());
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
