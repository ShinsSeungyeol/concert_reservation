package study.shinseungyeol.backend.common;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class AopForTransaction {

  @Transactional(TxType.REQUIRES_NEW)
  public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
    return joinPoint.proceed();
  }

}
