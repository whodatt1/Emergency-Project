# Emergency-Project

### 트러블슈팅

로그인시 Set-Cookie를 하였는데 네트워크에는 제대로 헤더가 붙어있지만 Cookie쪽에 저장되지 않는 상황 발생
=> axios쪽에 withCredentials: true, // 쿠키와 자격증명 포함

로그인시 response.headers['authorization'] 이 안가져와짐
=> 서버 CORS 필터에 .addExposedHeader("Authorization"); 추가하여 클라이언트쪽에서 읽을 수 있도록 수정한 후 해결

SERVICE KEY IS NOT REGISTERED ERROR 발생
=> WebClient을 이용하여 http 요청을 할 때 service key를 queryParam으로 전달했는데, WebClient가 queryParam을 UriComponentsBuilder#encode() 방식을 이용해서 인코딩하기 때문에 service key의 값이 달라져서 생기는 문제

DefaultUriBuilderFactory() 객체를 생성하여 인코딩 모드를 NONE이나 VALUES_ONLY로 변경

XMLMAPPER 사용하여 API XML 응답을 JSON으로 바꿔 처리하려고 의존성 추가하였는데
컨트롤러 응답시 XML이 우선시되는 상황이 발생 설정 추가하여 OBJECTMAPPER가 우선시되도록 수정
