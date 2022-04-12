package Crawler;

public class print {
    public static void print_book_data(int book_id, int series_num, int price, String book_name, String series_name, String book_url, String isbn,
                                       String author, String created_at, String publisher) {
        System.out.println("book_id : " + book_id);
        System.out.println("book_name : " + book_name);
        System.out.println("series_num : " + series_num);
        System.out.println("series_name : " + series_name);
        System.out.println("book_url : " + book_url);
        System.out.println("isbn : " + isbn);
        System.out.println("author : " + author);
        System.out.println("created_at : " + created_at);
        System.out.println("publisher : " + publisher);
        System.out.println("price : " + price + "\n");
    }
}
