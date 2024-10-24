package study.shinseungyeol.backend.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import study.shinseungyeol.backend.exception.CustomException;
import study.shinseungyeol.backend.exception.ErrorCode;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

  @ExceptionHandler(value = {CustomException.class})
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    log.error(ex.getMessage(), ex);
    ErrorCode errorCode = ex.getErrorCode();

    return ResponseEntity.status(errorCode.getStatus())
        .body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
  }

  @ExceptionHandler(value = {IllegalArgumentException.class})
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
      IllegalArgumentException ex) {
    log.error(ex.getMessage(), ex);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse("400", ex.getMessage()));
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    log.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("500", "에러가 발생했습니다."));
  }


}
