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
            String mysqlUrl = "jdbc:mysql://localhost/anchor?useSSL=false&allowPublicKeyRetrieval=true";
            String id = "root";
            String pw = "1969";

            conn = DriverManager.getConnection(mysqlUrl, id, pw);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(book_id) AS recent_book_id FROM book;");
            int recent_book_id = 1;
    
            // book_id의 MAX값 조회하기
            if (rs.next())
                recent_book_id = rs.getInt("recent_book_id");
            System.out.println("recent book_id : " + recent_book_id);

            for (int i = recent_book_id + 1; i <= 25000; i++) {
                String json = ScriptToJSON(i);
                if (json == "null")
                    break;
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(json);

                System.out.println("book_id : " + i);
                System.out.println("book_name : " + obj.get("title"));
                System.out.println("series_id : " + obj.get("title").toString().replaceAll("\\s[0-9]*$", ""));
                System.out.println("book_url : " + obj.get("link"));
                System.out.println("isbn : " + obj.get("isbn"));
                System.out.println("author : " + obj.get("author"));
                System.out.println("created_at : " + obj.get("pubdate"));
                System.out.println("publisher : " + obj.get("publisher"));
                System.out.println("price : " + obj.get("price"));
                System.out.println("\n");
                String query = "INSERT IGNORE INTO book(book_id, book_name, book_url, isbn, author, created_at, publisher, price) VALUES("
                        + i + ", '" + obj.get("title") + "', '" + obj.get("link") + "', '"+ obj.get("isbn") + "', '"
                        + obj.get("author") + "', '" + obj.get("pubdate") + "', '" + obj.get("publisher")
                        + "', " + obj.get("price") + ");";
                stmt.execute(query);
            }
        } catch (Exception e) { e.printStackTrace();}
    }
}