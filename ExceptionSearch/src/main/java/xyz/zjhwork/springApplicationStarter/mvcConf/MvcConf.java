package xyz.zjhwork.springApplicationStarter.mvcConf;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import xyz.zjhwork.interceptor.LoginInterceptor;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;




@EnableAspectJAutoProxy
@ComponentScan("xyz.zjhwork")
@EnableWebMvc
@Configuration
@EnableSwagger2
public class MvcConf implements WebMvcConfigurer {



    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        字符转换  包括解决中文乱码
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json,application/json;charset=utf-8,text/html;charset=utf-8"));
        converters.add(fastJsonHttpMessageConverter);
        AbstractJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("text/html"));
        converters.add(jackson2HttpMessageConverter);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器注册
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/newException").addPathPatterns("/")
                .addPathPatterns("/userStatus").addPathPatterns("/userExit").addPathPatterns("/newException").addPathPatterns("/myListException").addPathPatterns("/userInfo")
        .addPathPatterns("/isFavByUsernameAndExceptionId").addPathPatterns("/findFavByUsername").addPathPatterns("/deleteFavFromFavByUsernameAndExceptionId").addPathPatterns("/addFavByUsernameAndExceptionId")
        .addPathPatterns("/isAproByUsernameAndExceptionId").addPathPatterns("/addAproByUsernameAndExceptionId").addPathPatterns("/insertComment").addPathPatterns("/findHistoryByUsername").addPathPatterns("/userInfoUpdate")
        .addPathPatterns("exception/exceptionByUserAndId/*")
        ;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
        registry.addResourceHandler("/theme/**").addResourceLocations("classpath:/static/theme/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-resources/configuration/ui").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        静态资源存放
        registry.addResourceHandler("/*.html").addResourceLocations("classpath:/static/");

    }

    /**
     * put请求的参数过滤器
     */
    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter(){
        return new HttpPutFormContentFilter();
    }

    /**
     * mybatisConf
     *连接池
     * 使用hikariDataSource
     * @return
     */
    @Bean("hikariDataSource")
    public DataSource dataSource() {
        //加载db.properties 读取数据库基本信息
        Properties pop = new Properties();
        try {
            pop.load(this.getClass().getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HikariDataSource hikariDataSource = new HikariDataSource();
        try {
            hikariDataSource.setDriverClassName(pop.getProperty("jdbc.driver"));
            hikariDataSource.setUsername(pop.getProperty("jdbc.username"));
            hikariDataSource.setPassword(pop.getProperty("jdbc.password"));
            hikariDataSource.setJdbcUrl(pop.getProperty("jdbc.url"));
            hikariDataSource.setAutoCommit(true);
            hikariDataSource.setMinimumIdle(0);
            hikariDataSource.setMaximumPoolSize(10);
            hikariDataSource.setPoolName("hikariDataSource in Hikari System");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hikariDataSource;
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
