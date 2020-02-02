package xyz.zjhwork.springApplicationStarter.mvcConf;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.zjhwork.interceptor.LoginInterceptor;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan("xyz.zjhwork")
public class MvcConf implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        字符转换  包括解决中文乱码
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("text/html;charset=utf-8"));
        converters.add(fastJsonHttpMessageConverter);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器注册
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/newException").addPathPatterns("/")
                .addPathPatterns("/userStatus").addPathPatterns("/userExit").addPathPatterns("/newException").addPathPatterns("/myListException").addPathPatterns("/userInfo")
        .addPathPatterns("/isFavByUsernameAndExceptionId").addPathPatterns("/findFavByUsername").addPathPatterns("/deleteFavFromFavByUsernameAndExceptionId").addPathPatterns("/addFavByUsernameAndExceptionId")
        .addPathPatterns("/isAproByUsernameAndExceptionId").addPathPatterns("/addAproByUsernameAndExceptionId").addPathPatterns("/insertComment").addPathPatterns("/findHistoryByUsername").addPathPatterns("/userInfoUpdate")
        ;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/theme/**").addResourceLocations("classpath:/static/theme/");
//        静态资源存放
        registry.addResourceHandler("/*.html").addResourceLocations("classpath:/static/");

    }

    /**
     * mybatisConf
     *
     * @return
     */
    @Bean("pooledDataSource")
    public DataSource dataSource() {
        //加载db.properties 读取数据库基本信息
        Properties pop = new Properties();
        try {
            pop.load(this.getClass().getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PooledDataSource dataSource = new PooledDataSource();
        try {
            dataSource.setDriver(pop.getProperty("jdbc.driver"));
            dataSource.setUsername(pop.getProperty("jdbc.username"));
            dataSource.setPassword(pop.getProperty("jdbc.password"));
            dataSource.setUrl(pop.getProperty("jdbc.url"));
            dataSource.setDefaultAutoCommit(true);
            dataSource.setPoolMaximumActiveConnections(20);
            dataSource.setPoolMaximumIdleConnections(0);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    @Bean("sqlSessionFactoryBean")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws IOException {
        SqlSessionFactory factory;
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        //加载mapper.xml
        ResourcePatternResolver resolver = new ClassPathXmlApplicationContext();
        bean.setMapperLocations(resolver.getResources("classpath*:/daoMappers/*.xml"));
        try {
            factory = bean.getObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return factory;
    }

    @Bean("mapperScannerConfigurer")
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("xyz.zjhwork.dao");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactoryBean");
        return mapperScannerConfigurer;
    }
}
