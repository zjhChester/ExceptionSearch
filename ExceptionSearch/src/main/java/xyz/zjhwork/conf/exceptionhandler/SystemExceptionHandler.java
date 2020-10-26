package xyz.zjhwork.conf.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.conf.exception.BlogSystemException;
import xyz.zjhwork.dto.ResponseModel;
import java.util.Objects;
import static xyz.zjhwork.common.Constant.*;

/**
 * @Describe: 处理系统自定义总线异常的处理
 * @Author: zjhChester
 * @Date: 14:54 2020/9/29
 */
@Slf4j
@Component
@ControllerAdvice
public class SystemExceptionHandler {
    @ResponseBody
    @ExceptionHandler({BlogSystemException.class})
    public ResponseEntity<ResponseModel> handleValidException(RuntimeException e) {
        //日志记录错误信息
        log.error(Objects.requireNonNull(e.getMessage()));
        //将错误信息返回给前台
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( ResponseModel.failResModel(SYSTEM_EXCEPTION_CODE,Objects.requireNonNull(e.getMessage()), e.getStackTrace()));
    }
}
