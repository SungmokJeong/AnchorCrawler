# anchor_crawler

## onnada의 만화책 정보를 크롤링하는 프로그램


CrawlerVeryFirst - 최초 db생성시 실행 (timeout issue 있음)  
CrawlerDBUpdate - 크론잡을 통해 주기적 db업데이트를 위한 프로그램  
(중간에 빈 페이지들이 있기 때문에 따로 나눠서 작성)

쿼리문이 INSERT IGNORE 형식이므로 onnada에서 기존 데이터를 수정할 시 업데이트가 반영되지 않을 수 있음.