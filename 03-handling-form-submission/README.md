# 3단계: Handling Form Submission

## 무엇을 만드는 Guide인가?

브라우저 화면에 입력 폼(Id, Message)을 띄우고, 사용자가 값을 입력해 제출(Submit)하면
그 값을 서버가 받아서 결과 화면에 그대로 보여주는 서비스.
2단계가 URL 쿼리스트링(?name=)으로 값을 받았다면, 이번엔 HTML <form>을 통해 POST 방식으로 값을 받는다.

## 새롭게 배운 Spring 기술

- GET / POST 요청의 차이와 역할 분리
- @ModelAttribute를 이용한 폼 데이터 자동 바인딩
- Thymeleaf th:object, th:field, th:action
- 일반 class + getter/setter (record 대신 사용하는 이유)

## 핵심 Annotation / 개념 설명

| 개념                          | 설명                                                                                      |
|-----------------------------|-----------------------------------------------------------------------------------------|
| @GetMapping vs @PostMapping | 같은 경로(/greeting)라도 HTTP 메서드에 따라 다른 메서드가 처리하도록 분리 가능                                     |
| @ModelAttribute             | 폼에서 넘어온 여러 필드 값을 자동으로 하나의 객체(Greeting)에 채워서 파라미터로 받음                                    |
| th:object="${greeting}"     | 폼 전체가 다룰 객체를 지정                                                                         |
| th:field="*{id}"            | th:object로 지정한 객체의 필드와 input을 연결. ${greeting.id}의 축약형                                   |
| record 대신 class 사용          | 폼은 "빈 객체를 먼저 만들고 나중에 setter로 값을 채우는" 방식이 필요한데, record는 생성 후 값 변경이 불가능(불변)해서 이 방식에 맞지 않음 |

## Step 1: 기본 구현 및 실행 화면

### GET /greeting (폼 화면)

![greeting form](./screenshots/greeting-form.png)

### POST /greeting 제출 후 결과 화면

![result page](./screenshots/result-page.png)

### 겪은 문제와 해결

- 폴더 이름을 handling-form-submission → 03-handling-form-submission으로 리팩터링하는 과정에서
  IntelliJ가 이 폴더를 Gradle 모듈로 인식하지 못해 New 메뉴에 Java 파일 생성 옵션이 안 뜨는 문제 발생
  → 프로젝트를 닫았다가 다시 열어서(Close Project → Reopen) 모듈로 재인식시켜 해결

## 느낀 점

- Model이 내가 직접 만드는 게 아니라 Spring이 요청 처리마다 자동으로 만들어서 파라미터에 넣어주는 객체라는 걸 3단계에서도 다시 확인함 (제어의 역전)
- 같은 URL(/greeting)이라도 HTTP 메서드(GET/POST)에 따라 완전히 다른 메서드가 처리된다는 걸 직접 확인
- 폼을 다룰 때는 record가 아니라 getter/setter가 있는 일반 클래스를 써야 하는 이유(값이 나중에 채워져야 하므로)를 이해함