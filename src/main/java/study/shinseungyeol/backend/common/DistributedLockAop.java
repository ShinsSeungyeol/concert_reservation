package study.shinseungyeol.backend.common;

import java.lang.reflect.Method;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

  private static final String REDISSON_LOCK_PREFIX = "LOCK:";

  private final RedissonClient redissonClient;
  private final AopForTransaction aopForTransaction;

  @Around("@annotation(study.shinseungyeol.backend.common.DistributedLock)")
  public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("DistributedLockAop.lock");
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    String key =
        REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(),
            joinPoint.getArgs(), distributedLock.key());

    System.out.println("key = " + key);

    RLock rLock = redissonClient.getLock(key);

    try {
      boolean available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
          distributedLock.timeUnit());

      if (!available) {
        return false;
      }

      return aopForTransaction.proceed(joinPoint);

    } catch (InterruptedException e) {
      throw new InterruptedException();

    } finally {
      try {
        rLock.unlock();
      } catch (IllegalMonitorStateException e) {

        log.info("Redisson Lock Already UnLock {} {}",
            Map.of("serviceName", method.getName()),
            Map.of("key", key));
      }
    }


  }

}