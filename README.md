# anchor_crawler

## onnada의 만화책 정보를 크롤링하는 프로그램
https://onnada.com/comic/1 에서 시작해서 인덱스 순서대로 웹페이지의 json을 파싱하여 db의 book, series 테이블에 데이터를 저장합니다.  
db erd https://www.erdcloud.com/d/y4SFMLxXPR7qCBzzc

### 실행 전에 sql파일로 데이터베이스를 먼저 만들어야 합니다.

main method는 두개의 파일에 각각 있습니다. ~~(중간에 빈 페이지들이 있기 때문에 따로 나눠서 작성)~~  
~~CrawlerVeryFirst - 최초 db생성시 실행 (timeout issue 있음)~~  
CrawlerDBUpdate만 사용해도 됩니다.  
CrawlerDBUpdate - 크론잡을 통해 주기적 db업데이트를 위한 프로그램  



### (issue) 실행 도중 connection timeout exeption이 발생한다면 CrawlerDBUpdate를 다시 실행시켜서 중단된 곳 부터 다시 크롤링해야합니다.

쿼리문이 INSERT IGNORE 형식이므로 onnada에서 기존 데이터를 수정할 시 업데이트가 반영되지 않을 수 있습니다.
