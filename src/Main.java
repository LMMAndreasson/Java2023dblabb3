import java.sql.*;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:D:/Databases/labb3albums.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void printActions() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - Stäng av\n" +
                "1  - Visa alla skivor\n" +
                "2  - CRUD\n" +
                "3  - Visa alla skivor från en artist\n" +
                "4  - Visa statistik\n" +
                "5  - Visa låtar på en skiva\n" +
                "6  - Visa alla låtar\n" +
                "7  - Visa en lista över alla val.");
    }

    private static void statisticPrint(){
        System.out.println("\nVälj:\n");
        System.out.println("0  - Gå tillbaka\n" +
                "1  - Visa antal skivor\n" +
                "2  - Visa antal skivor tillhörande en artist\n" +
                "3  - Visa antal låtar tillhörande en artist\n" +
                "4  - Visa en lista över alla val.");
    }

    private static void CRUDPrint(){
        System.out.println("\nVälj:\n");
        System.out.println("0  - Gå tillbaka\n" +
                "1  - Lägg till skiva\n" +
                "2  - Ta bort skiva\n" +
                "3  - Uppdatera skiva\n" +
                "4  - Lägg till låt\n" +
                "5  - Visa en lista över alla val.");
    }


    private static void albumSelectAll(){
        String sql = "SELECT * FROM album";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("albumId") +  "\t" +
                        rs.getString("albumTitle") + "\t" +
                        rs.getString("albumArtist") + "\t" +
                        rs.getString("albumYear"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void albumInsert(String title, String artist, int year, int rating) {
        String sql = "INSERT INTO album(albumArtist, albumTitle, albumYear, albumRating) VALUES(?,?,?,?)";

        try{
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, artist);
            pstmt.setString(2, title);
            pstmt.setInt(3, year);
            pstmt.setInt(4, rating);
            pstmt.executeUpdate();
            System.out.println("Du har lagt till ett nytt album");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void albumInput(){
        System.out.println("Skriv in albumets titel");
        String inputTitle= scanner.nextLine();
        System.out.println("Skriv in albumets artist");
        String inputArtist= scanner.nextLine();
        System.out.println("Skriv in vilket år albumet kom ut");
        int inputYear= scanner.nextInt();
        scanner.nextLine();
        System.out.println("Skriv in albumets betyg");
        int inputRating = scanner.nextInt();
        scanner.nextLine();

        albumInsert(inputTitle, inputArtist, inputYear, inputRating);
    }

    private static void trackInsert(String title, String album, int trackN) {
        String sql = "INSERT INTO track(trackTitle, trackNumber, trackAlbumID) VALUES(?,?, (SELECT albumID FROM album WHERE albumTitle = ?))";

        try{
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setInt(2, trackN);
            pstmt.setString(3, album);
            pstmt.executeUpdate();
            System.out.println("Du har lagt till en ny låt");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void trackInput(){
        System.out.println("Skriv in låtens titel");
        String inputTitle= scanner.nextLine();
        System.out.println("Skriv in albumet låten är från");
        String inputAlbum= scanner.nextLine();
        System.out.println("Skriv in vilken plats på skivan låten har");
        int inputNumber= scanner.nextInt();
        scanner.nextLine();

        trackInsert(inputTitle, inputAlbum, inputNumber);
    }



    private static void albumSearchAll(){
        String sql = "SELECT * FROM album WHERE albumArtist = ?";

        System.out.println("Skriv in vilken artist du vill se");

        String inputArtist = scanner.nextLine();

        try {
            Connection conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);

            pstmt.setString(1, inputArtist);

            ResultSet rs    = pstmt.executeQuery();


            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("AlbumID") +  "\t" +
                        rs.getString("albumTitle") + "\t" +
                        rs.getString("albumArtist") + "\t" +
                        rs.getString("albumRating"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private static void albumCount(){
        String sql = "SELECT COUNT (albumID) AS albumCount FROM album";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            // loop through the result set
            while (rs.next()) {
                System.out.println("Antal skivor:  " +
                        rs.getInt( "albumCount"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void artistCountAlbums(){
        String sql = "SELECT COUNT (albumID) AS albumCount FROM album WHERE albumArtist = ?";

        System.out.println("Skriv in vilken artist du vill se");

        String inputArtist = scanner.nextLine();


        try {
            Connection conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);

            pstmt.setString(1, inputArtist);

            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println("Antal skivor:  " +
                        rs.getInt( "albumCount"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void artistCountTracks(){
        String sql = "SELECT COUNT (trackID) AS trackCount FROM track INNER JOIN album a ON a.albumID = track.trackAlbumID AND albumArtist = ?";

        System.out.println("Skriv in vilken artist du vill se");

        String inputArtist = scanner.nextLine();


        try {
            Connection conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);

            pstmt.setString(1, inputArtist);

            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println("Antal låtar:  " +
                        rs.getInt( "trackCount"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showAllTracks(){
        String sql = "SELECT * FROM track INNER JOIN album a ON a.albumID = track.trackAlbumID ";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            System.out.println("ID Title Album TrackNumber");
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("trackID") +  "\t" +
                        rs.getString("trackTitle") + "\t" +
                        rs.getString("albumTitle") + "\t" +
                        rs.getString("trackNumber"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showTracksForAlbum(){
        String sql = "SELECT * FROM track INNER JOIN album a ON a.albumID = track.trackAlbumID AND albumTitle = ?";

        System.out.println("Skriv vilket album du vill visa låtar för");

        String inputAlbum = scanner.nextLine();


        try {
            Connection conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);

            pstmt.setString(1, inputAlbum);

            ResultSet rs    = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("trackNumber") +  "\t" +
                        rs.getString("trackTitle"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void albumDelete(){
        String sql = "DELETE FROM album WHERE albumTitle = ?";

        System.out.println("Skriv in vilken skiva du vill ta bort");

        String inputAlbum = scanner.nextLine();

        try {
            Connection conn = connect();
            PreparedStatement pstmt  = conn.prepareStatement(sql);

            pstmt.setString(1, inputAlbum);

            pstmt.executeUpdate();

            System.out.println("deleted");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void albumUpdateInput(){
        System.out.println("Skriv in albumets ID");
        int inputID = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Skriv in albumets titel");
        String inputTitle= scanner.nextLine();
        System.out.println("Skriv in albumets artist");
        String inputArtist= scanner.nextLine();
        System.out.println("Skriv in vilket år albumet kom ut");
        int inputYear= scanner.nextInt();
        scanner.nextLine();
        System.out.println("Skriv in albumets betyg");
        int inputRating = scanner.nextInt();
        scanner.nextLine();

        updateAlbum(inputTitle, inputArtist, inputRating, inputYear, inputID);
    }

    private static void updateAlbum(String artist, String title, int rating, int year, int id) {
        String sql = "UPDATE album SET albumArtist = ? , "
                + "albumTitle = ? , "
                + "albumRating = ? ,"
                + "albumYear = ? "
                + "WHERE albumID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, artist);
            pstmt.setString(2, title);
            pstmt.setInt(3, rating);
            pstmt.setInt(4, year);
            pstmt.setInt(5, id);
            // update
            pstmt.executeUpdate();
            System.out.println("Du har uppdaterat valt album");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void statisticAction() {
        boolean quit = false;
        statisticPrint();

        while (!quit) {
            System.out.println("\nVälj 4 för att visa val):");
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0 -> {
                    printActions();
                    quit = true;
                }
                case 1 -> albumCount();
                case 2 -> artistCountAlbums();
                case 3 -> artistCountTracks();
                case 4 -> statisticPrint();
            }
        }
    }

    public static void CRUDAction() {
        boolean quit = false;
        CRUDPrint();

        while (!quit) {
            System.out.println("\nVälj 5 för att visa val):");
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0 -> {
                    printActions();
                    quit = true;
                }
                case 1 -> albumInput();
                case 2 -> albumDelete(); //delete
                case 3 -> albumUpdateInput(); //update
                case 4 -> trackInput(); //add track
                case 5 -> CRUDPrint();
            }
        }
    }


    public static void main(String[] args) {
        connect();

        boolean quit = false;
        printActions();
        while(!quit) {
            System.out.println("\nVälj 7 för att visa val):");
            int action = scanner.nextInt();
            scanner.nextLine();

            switch (action) {
                case 0 -> {
                    System.out.println("\nStänger ner...");
                    quit = true;
                }
                case 1 -> albumSelectAll();
                case 2 -> CRUDAction();
                case 3 -> albumSearchAll();

                //update("Bilbo", "Tolkien, J.R.R", 100, 1);
                case 4 -> statisticAction();
                case 5 -> showTracksForAlbum();
                case 6 -> showAllTracks();
                case 7 -> printActions();
            }
        }

    }

}