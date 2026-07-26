# 5단계: Uploading Files

## 무엇을 만드는 Guide인가?

파일을 업로드하면 서버(로컬 디스크)에 저장되고, 업로드된 파일 목록을 화면에서 확인하며
각 파일을 다시 다운로드할 수 있는 서비스. 지금까지는 텍스트 값만 다뤘다면,
이번엔 실제 파일(멀티파트 데이터)을 받아 저장하고 서빙하는 것이 핵심이다.

## 새롭게 배운 Spring 기술

- MultipartFile을 이용한 파일 업로드 처리
- @Service를 이용한 비즈니스 로직 계층 분리
- @Autowired 생성자 주입 (의존성 주입, DI)
- 인터페이스(StorageService) + 구현체(FileSystemStorageService) 설계
- @ConfigurationProperties를 이용한 외부 설정값 자동 바인딩
- CommandLineRunner를 이용한 앱 시작 시 초기화 작업
- @ExceptionHandler를 이용한 컨트롤러 단위 예외 처리
- RedirectAttributes(flash attribute)를 이용한 리다이렉트 후 1회성 메시지 전달

## 핵심 Annotation / 개념 설명

| 개념                                     | 설명                                                                                                        |
|----------------------------------------|-----------------------------------------------------------------------------------------------------------|
| MultipartFile                          | HTML 폼에서 enctype="multipart/form-data"로 전송된 파일을 표현하는 타입                                                   |
| @Service                               | 비즈니스 로직(실제 작업 처리)을 담당하는 클래스임을 표시. @Controller와 마찬가지로 Spring이 관리하는 Bean으로 등록됨                              |
| @Autowired (생성자 주입)                    | 필요한 객체를 직접 new로 만들지 않고, Spring이 이미 등록해둔 Bean을 자동으로 찾아 생성자에 넣어주는 것                                         |
| StorageService (인터페이스)                 | "저장소라면 이런 기능이 있어야 한다"는 규칙만 정의. 실제 저장 방식(로컬 디스크, 클라우드 등)은 구현체에서 결정하므로, 나중에 저장 방식이 바뀌어도 컨트롤러 코드는 그대로 둘 수 있음 |
| @ConfigurationProperties               | application.properties의 특정 접두사(storage.) 값을 자동으로 클래스 필드에 채워주는 애노테이션                                       |
| CommandLineRunner + @Bean              | 애플리케이션이 완전히 시작된 직후 한 번 실행되는 코드를 등록. 여기서는 업로드 폴더를 초기화하는 데 사용                                               |
| @ExceptionHandler                      | 특정 예외가 발생했을 때 이 컨트롤러 안에서 어떻게 처리할지 지정                                                                      |
| RedirectAttributes / addFlashAttribute | redirect: 이후에도 딱 한 번 값을 전달할 수 있게 해주는 기능. 일반 Model 값은 리다이렉트 시 사라지는 문제를 해결                                  |

## Step 1: 기본 구현 및 실행 화면

### 파일 업로드 폼 및 목록 화면

![upload form](./screenshots/upload-form.png)

### 파일 업로드 성공 후 메시지 및 목록 갱신

![upload success](./screenshots/upload-success.png)

### 업로드된 파일 다운로드

![file download](./screenshots/file-download.png)

### 겪은 문제와 해결

- 파일 업로드 성공 시 redirect:/ 리다이렉트 과정에서 주소창에 ;jsessionid=... 가 붙으며
  Whitelabel 404 에러 페이지가 뜨는 문제 발생 (실제 파일 저장은 정상적으로 이루어짐)
  → 쿠키/시크릿 모드 여부와 무관하게 동일하게 발생하는 것을 확인해 코드 문제가 아님을 좁혀냄
  → application.properties에 server.servlet.session.tracking-modes=cookie 를 추가해
  세션 추적 방식을 쿠키로 고정시켜 URL에 세션ID가 붙는 현상을 막아 해결함

## Step 4: 기능 확장 — 파일 삭제 기능 추가

기존에는 파일 업로드(Create), 목록 조회 및 다운로드(Read)만 가능했는데,
파일 삭제(Delete) 기능을 추가해 CRUD 흐름을 이 도메인에서도 완성했다.

### 추가한 것

- StorageService 인터페이스에 delete(filename) 메서드 추가
- FileSystemStorageService에서 Files.deleteIfExists(...)로 실제 파일 삭제 구현
- POST /files/{filename}/delete 엔드포인트 추가 (삭제는 상태를 변경하는 작업이므로 GET이 아닌 POST 사용)
- 목록 화면에 각 파일마다 삭제 버튼(폼) 추가

### 설계 관련 고민

- 처음에는 목록의 다운로드 URL 문자열에서 Thymeleaf 문자열 함수(#strings.substringAfter)로
  파일명을 역산해서 삭제 폼에 쓰려고 했으나, 원본 가이드가 "View는 단순하게, 컨트롤러가 필요한
  형태로 미리 가공해서 넘긴다"는 설계 스타일(예: MvcUriComponentsBuilder로 URL을 컨트롤러에서
  미리 계산해 넘기는 방식)을 따르고 있다는 걸 다시 확인하고, 컨트롤러에서 filenames 리스트를
  별도로 하나 더 Model에 담아 View에서는 단순히 인덱스로 매칭만 하도록 수정함

### 실행 화면

![delete button](./screenshots/delete-button.png)
![delete success](./screenshots/delete-success.png)

## 느낀 점

- @Autowired 생성자 주입 덕분에, 컨트롤러가 StorageService 인터페이스만 알고 있으면 되고
  실제 구현체(FileSystemStorageService)가 무엇인지 몰라도 된다는 설계를 실전에서 체감함
- StorageException, StorageFileNotFoundException처럼 예외를 계층적으로 나눠두면,
  @ExceptionHandler로 특정 예외만 골라서 다르게 처리할 수 있다는 걸 확인함
- 새 기능을 추가할 때, 그 프로젝트의 기존 설계 스타일(여기서는 "View는 단순하게 유지")을
  일관되게 따르는 것이 중요하다는 걸 삭제 기능 구현 과정에서 배움
- try (자원 = ...) { } 형태의 try-with-resources와, 예외를 잡는 일반 try-catch가
  서로 다른 목적(자원 자동 해제 vs 예외 처리)을 가진다는 것을 구분해서 이해함