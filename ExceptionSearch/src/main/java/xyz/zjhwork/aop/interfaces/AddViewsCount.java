package xyz.zjhwork.aop.interfaces;

import java.lang.annotation.*;

/**
 * 记录浏览记录views的数量
 */
@Documented
@Inherited
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AddViewsCount {

}
