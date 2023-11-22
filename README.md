## 상세 내용

북엔드는 독후감을 작성하고 같은 도서를 읽은 사람들과 채팅(개발중)을 할 수 있는 서비스 입니다. 
오프라인 또는 화상 독서 모임보다는 가볍게 책에 대해서 대화를 하고 싶어하는 사람들을 위해 개발하게 되었습니다.
공개여부를 설정하여 독후감을 혼자만 볼 수도, 다른 사용자들에게 공개할 수도 있습니다.(개발중)
같은 도서에 대한 독후감을 작성한 사람들끼리만 채팅을 할 수 있습니다.(개발중)

## 사용 기술 및 라이브러리

- Spring Boot, Spring Security, JPA/Hibernate, Gradle, MySQL
- HTML/CSS, JavaScript, jQuery, Thymeleaf
- Google OAuth2

## 주요 기능

- 구글 OAuth2 로그인
- Form 로그인 기능을 이용한 게스트 로그인
- 자신이 작성한 독후감 목록 조회
- 알라딘 API를 이용하여 도서 조회
- 독후감 작성 및 저장, 수정, 삭제
- 도서명으로 독후감 검색

## 개발 중인 기능

- 공개 중인 독후감은 자신이 쓴 독후감이 아니더라도 목록에 노출
- 독후감 작성한 날은 오른쪽 캘린더에 표시
- 독후감을 작성한 날을 캘린더에서 클릭하면 해당 일자에 작성한 독후감 목록 조회
- 같은 도서의 독후감을 작성한 사람들끼리 채팅방 이용 가능
- 채팅방에서 서로의 독후감을 전달

## 깨달은 점

- SpringSecurity를 이용하여 로그인 기능을 구현할 때 콘솔 로그에도 찍히지 않고 인터셉트 되는 경우가 많아서 기능이 작동되지 않는 이유를 찾기가 어려웠다. 
→ 해당 기능의 흐름을 파악해서 정리함.
- 쿼리를 작성하여 데이터를 조회하는 것처럼 JPA 메소드를 이용하는 것이 어려웠다.
    
    → JPA 메소드 뿐만 아니라 여러가지 대체 기능들을 공부함.
    
- 화면을 처음 로드했을때는 데이터를 Model에 넣고, 이후에 검색을 했을때는 ajax로 호출하려고 했을때 코드가 깔끔하지 않았다.
    
    → Thymeleaf 기능을 이용하여 데이터를 화면에 뿌려줄 수 있는 기능을 공부함.
    

## 개발 진행 상황
