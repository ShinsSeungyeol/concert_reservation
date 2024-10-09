# 콘서트 예약 시스템

---

## Description

1. 콘서트 예약 서비스를 구현한다.
2. 대기열 시스템을 구축하고, 예약 서비스는 작업 가능한 유저만 수행할 수 있도록 한다.
3. 사용자는 좌석 예약 전에 미리 충전한 잔액을 이용한다.
4. 좌석 예약 요청 시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 한다.

---

## Requirements

1. 아래 5가지 API를 구현한다.
    - 유저 토큰 발급 API
    - 예약 가능 날짜 / 좌석 API
    - 좌석 예약 요청 API
    - 잔액 충전 / 조회 API
    - 결제 API
2. 각 기능 및 제약사항에 대해 단위 테스트를 반드시 하나 이상 작성한다.
3. 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성한다.
4. 동시성 이슈를 고려한다.
5. 대기열 개념을 고려한다.

---

## 시퀀스 다이어그램

### 토큰 발급 API

![토큰발급](token_generate.png)

### 토큰 상태 조회 API

![token_search.png](token_search.png)

### 콘서트 예약 가능한 날짜 조회 API

![콘서트 예약 가능한 날짜 조회](reservation_possible_datetime.png)

### 콘서트의 예약 가능한 좌석 조회 API

![콘서트의_예약_가능_좌석_조회](reservation_possible_seat.png)

### 콘서트 좌석 예약 API

![콘서트 좌석 예약](reservation.png)

### 포인트 충전 API

![포인트 충전](point_charge.png)

### 포인트 조회 API

![포인트 조회](point_search.png)

### 결제 API

![payment.png](payment.png)



