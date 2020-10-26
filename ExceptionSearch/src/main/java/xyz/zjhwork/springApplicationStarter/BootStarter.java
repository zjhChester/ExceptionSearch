package xyz.zjhwork.springapplicationstarter;

import lombok.Builder;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import xyz.zjhwork.utils.PropertiesUtils;

/**
 * @author zjhChester
 */

@Builder
public class BootStarter {

    public  void run(){
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(PropertiesUtils.getConfig("config."+PropertiesUtils.currentSystem+".port")));
        // 标识tomcat启动为webapp
        tomcat.addWebapp(PropertiesUtils.getConfig("config."+PropertiesUtils.currentSystem+".context-path"),PropertiesUtils.getConfig("config."+PropertiesUtils.currentSystem+".doc-base").toString());
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
