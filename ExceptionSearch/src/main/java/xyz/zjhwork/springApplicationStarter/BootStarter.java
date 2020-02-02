package xyz.zjhwork.springApplicationStarter;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class BootStarter {
    public static void run(){
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        // 标识tomcat启动为webapp
        tomcat.addWebapp("/","D://test/");
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
        run();
    }
}
