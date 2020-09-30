package xyz.zjhwork.conf.exception;

/**
 * @Describe: 主动抛出的异常，用于控制器处理
 * @Author: zjhChester
 * @Date: 14:47 2020/9/29
 */
public class BlogSystemException extends RuntimeException {
    public BlogSystemException() {
        super();
    }

    public BlogSystemException(String message) {
        super("系统出现异常："+message);
    }

    public BlogSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogSystemException(Throwable cause) {
        super(cause);
    }

    protected BlogSystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
