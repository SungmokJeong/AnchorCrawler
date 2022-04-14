package Crawler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class CrawlerDBUpdate {
    private static String ScriptToJSON(int idx) throws Exception {
        String url = "https://onnada.com/comic/" + idx;

        if(url.indexOf("https://") >= 0)
            SSLCaller.setSSL();

        Element script = Jsoup.connect(url).userAgent("Chrome").timeout(0)
                .get()
                .select("script")
                .select("script[type=\"text/javascript\"]").first();
        String json = script.toString();

        // 페이지가 비어있을 때 처리하는 부분
        if (json.contains("\"items\"") == false)
            return "null";

        int start = json.indexOf("\"result\"");
        String tmp = json.substring(start + 9);
        return tmp.substring(0, tmp.indexOf(",\"items\"")) + "}";
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String mysqlUrl = "jdbc:mysql://anchor-rds.c153bppahloq.ap-northeast-2.rds.amazonaws.com/anchor?useSSL=false&allowPublicKeyRetrieval=true";
            String id = "admin";
            String pw = "anchoradmin";

            conn = DriverManager.getConnection(mysqlUrl, id, pw);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(book_id) AS recent_book_id FROM book;");
            int recent_book_id = 1;
    
            // book_id의 MAX값 조회하기
            if (rs.next())
                recent_book_id = rs.getInt("recent_book_id");
            System.out.println("recent book_id : " + recent_book_id + "\n");

            for (int i = recent_book_id + 1; true; i++) {
                String json = ScriptToJSON(i);
                if (json == "null") {
                    System.out.println("update finished");
                    break;
                }

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(json);

                String str[] = obj.get("title").toString().split(" ");
                int series_num = util.isInteger(str[str.length - 1]) == true ? Integer.parseInt(str[str.length - 1]) : 0;
                String book_name = obj.get("title").toString();
                String series_name = obj.get("title").toString().replaceAll("\\s[.0-9]*$", "");
                String book_url = obj.get("link") != null ? obj.get("link").toString() : ""; // "link":null 일 경우에 NullPointerException 발생하는것 방지
                String isbn = obj.get("isbn").toString();
                String author = obj.get("author").toString();
                String created_at = obj.get("pubdate").toString();
                String publisher = obj.get("publisher").toString();

                stmt.execute("INSERT IGNORE INTO series(series_name) VALUES('" + series_name + "');");

                String query = "INSERT IGNORE INTO book(book_id, book_name, series_num, book_url, isbn, author, created_at, publisher, price, series_id) VALUES("
                        + i + ", '" + book_name + "', '" + series_num + "', '" + book_url + "', '" + isbn + "', '"
                        + author + "', '" + created_at + "', '" + publisher + "', " + obj.get("price")
                        + ", (SELECT series_id FROM series WHERE series_name like '" + series_name + "'));";
                util.print_book_data(i, series_num, book_name, series_name, book_url, isbn, author, created_at, publisher);
                System.out.println("price : " + obj.get("price") + "\n");
                stmt.execute(query);
            }
        } catch (Exception e) { e.printStackTrace();}
    }
}