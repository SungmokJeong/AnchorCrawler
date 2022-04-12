package Crawler;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import java.sql.*;

public class CrawlerVeryFirst {
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


            for (int i = 1; i <= 25000; i++) {
                String json = ScriptToJSON(i);
                if (json == "null")
                    continue;
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(json);

                System.out.println("book_id : " + i);
                System.out.println("book_name : " + obj.get("title"));
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