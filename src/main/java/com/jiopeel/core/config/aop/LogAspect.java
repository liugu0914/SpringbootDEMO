package com.jiopeel.core.config.aop;

import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.util.MathUtil;
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
    public void pointcut() {
    }

    /**
     * 环切 前后都切
     * @param joinPoint
     * @return Object
     * @throws Throwable
     * @auhor: lyc
     * @Date: 2020/7/21 15:48
     */
    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String servletPath = request.getServletPath();
        Class<?> aopClass = joinPoint.getTarget().getClass();
        Logger log = LoggerFactory.getLogger(aopClass);
        log.info("********** URI   : [{}] **********", servletPath);
        User user = (User) request.getAttribute(UserConstant.USER);
        String methodName = joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName();
        //记录执行时间
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        long totalTime = System.currentTimeMillis() - startTime;
        double seconds = MathUtil.div(totalTime,1000);
        String account = user == null ? "" : user.getAccount();
        String id = user == null ? "" : String.valueOf(user.getId());
        if (seconds >= 5d)
            log.warn("超时请求 : {}s | User : [{} : {}] | URI   : {}", seconds, account, id, servletPath);
        log.info("********** User  : [{} : {}] | Method: {} | Seconds: {}s **********", account, id, methodName, seconds);
        log.info("********** Result: [{}] **********", result);
        return result;
    }

}


