package xyz.zjhwork.aop.impl;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.zjhwork.dao.ExceptionDao;
import org.apache.catalina.connector.RequestFacade;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
@Slf4j(topic = "addViewLog")
public class AddViewCountAopImpl {

    @Autowired
    private ExceptionDao exceptionDao;

//    private final Logger log = LoggerFactory.getLogger(getClass());

    @Pointcut("@annotation(xyz.zjhwork.aop.interfaces.AddViewsCount)")
    private void do_annotation(){

        log.info("do_annotation about add count for exception views ....");
    }

    /**
     * 前置处理浏览文章
     * request -> org.apache.catalina.connector.RequestFacade
     * integer -> java.lang.Integer
     * @param joinPoint 切入点
     */
    @Before(value = "do_annotation()")
    public void addViewCount(JoinPoint joinPoint){
        log.info("do addViewCount.....");
        Map<String,Object> map = new HashMap<>();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if(null!=arg)
          map.put(arg.getClass().toString(),arg);
        }

        RequestFacade request =  (RequestFacade)map.get("class org.apache.catalina.connector.RequestFacade");
        Integer exceptionId =  (Integer) map.get("class java.lang.Integer");
        log.info("api about /getException has visited form ip :[{}],visited exception id is [{}]",request.getRemoteHost(),exceptionId);
        log.info("starting execute add Views....");
        int res=0;
        //判断exceptionId是否为null
        if(exceptionId!=null)
            res = exceptionDao.addViews(exceptionId);
        log.info("execute add result:[{}]",res==1);
    }
}
