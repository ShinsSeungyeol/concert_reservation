package study.shinseungyeol.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NFU001", "사용자를 찾을 수 없습니다."),
  NOT_FOUND_CONCERT(HttpStatus.NOT_FOUND, "NFC002", "콘서트를 찾을 수 없습니다."),
  NOT_FOUND_SEAT(HttpStatus.NOT_FOUND, "NFS003", "콘서트 좌석을 찾을 수 없습니다."),
  NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "NFT004", "토큰을 찾을 수 없습니다."),
  NOT_FOUND_POINT(HttpStatus.NOT_FOUND, "NFP005", "포인트를 찾을 수 없습니다."),
  NOT_FOUND_RESERVATION(HttpStatus.NOT_FOUND, "NFR006", "예약 내역을 찾을 수 없습니다"),

  NOT_AVAILABLE_SEAT(HttpStatus.BAD_REQUEST, "NAS007", "좌석이 사용 가능 상태가 아닙니다"),
  NOT_ENOUGH_BALANCE(HttpStatus.PAYMENT_REQUIRED, "EEP008", "포인트가 부족합니다."),
  EXPIRED_SEAT_RESERVATION(HttpStatus.BAD_REQUEST, "ESR009", "좌석 예약이 이미 만료되었습니다"),
  NOT_ACTIVE_TOKEN(HttpStatus.FORBIDDEN, "NAT010", "토큰이 액티브 상태가 아닙니다"),
  ;

  private HttpStatus status;
  private String code;
  private String message;
}
