package com.jiopeel.core.config.aop;

import com.jiopeel.sys.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Slf4j
@Component
public class LogAspect {

    /**
     * @Description :匹配com.jiopeel.event包及其子包下的所有方法
     * @auhor:lyc
     * @Date:2019/11/1 22:44
     */
    @Pointcut("execution( * com.jiopeel.*.event..*.*(..))")
    public void pointcut() {}

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String servletPath=request.getServletPath();
        Class<?> aopClass = joinPoint.getTarget().getClass();
        Logger log = LoggerFactory.getLogger(aopClass);
        log.info("********** URI   : [{}] **********", servletPath);
        User user = (User) request.getAttribute("user");
        String methodName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        //1、记录执行时间
        long startTime = System.currentTimeMillis();
        Object result=joinPoint.proceed(joinPoint.getArgs());
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double seconds= (double) (totalTime/1000);
        if (seconds>=5)
            log.warn("超时请求 : {}s",seconds);
        String username=user==null?"":user.getUsername();
        String pid=user==null?"":String.valueOf(user.getId());
        log.info("********** User  : [{} : {}] | Method: {} | Seconds: {}s**********", username,pid,methodName, seconds);
        log.info("********** Result: [{}] **********", result);
        return result;
    }

}


