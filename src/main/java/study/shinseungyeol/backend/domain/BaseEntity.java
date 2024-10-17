package study.shinseungyeol.backend.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Setter(AccessLevel.PROTECTED)
public class BaseEntity {

  @CreatedDate
  private LocalDateTime createAt;

  @LastModifiedDate
  private LocalDateTime updateAt;

  private LocalDateTime deleteAt;
}
