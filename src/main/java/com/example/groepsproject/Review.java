package com.example.groepsproject;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;


public class Review implements Comparable<Review> {

    private int referenceNumber;
    private String comment;
    private int rating;

    private LocalDateTime date;

    public static int generateNewReferenceNumber() throws SQLException{
        //nieuw reference nummer = hoogste reference nummer dat tot nu toe is gebruikt + 1

        //SQL String definiëren
        String sql = "SELECT reference_nr FROM Review";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Hoogste reference nummer uit de ResultSet halen
        int hoogstereferencenummer = 0;
        while(resultSet.next()) {
            if(resultSet.getInt("reference_nr")>hoogstereferencenummer){
                hoogstereferencenummer = resultSet.getInt("reference_nr");
            }
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Nieuw reference nummer teruggeven
        int nieuwreferencenummer = hoogstereferencenummer + 1;
        return nieuwreferencenummer;
    }

    //dummy constructor die we in de getReviews gebruiken om review objecten aan te maken zonder deze opnieuw in database te zetten
    public Review(String comment, int rating, LocalDateTime date){
        this.comment = comment;
        this.rating = rating;
        this.date = date;
    }

    public Review(String comment, int rating) throws SQLException{
        this.referenceNumber = Review.generateNewReferenceNumber();
        this.comment = comment;
        this.rating = rating;
        this.date = LocalDateTime.now();

        //SQL string definiëren
        String sql = "INSERT INTO Review (reference_nr, comment, rating, date) VALUES (?, ?, ?, ?)";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.insertQuery(sql);

        //Parameters opgeven
        statement.setInt(1, this.referenceNumber);
        statement.setString(2, this.comment);
        statement.setInt(3, this.rating);
        statement.setTimestamp(4, Timestamp.valueOf(this.date));

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        //MySQLConnect.staticConnection.close();
        //MySQLConnect.staticConnection = null;
    }

    public static void writeReview(String usernameofplayer,String usernameoforganizer, String comment, int rating) throws SQLException{

        //SQL string definiëren
        String sql = "INSERT INTO Reviews (USERNAMEORGANIZER,USERNAMEPLAYER,REFERENCE_NUMBER) VALUES (?, ?, ?)";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.insertQuery(sql);

        //Parameters opgeven en review object maken
        statement.setString(1, usernameoforganizer);
        statement.setString(2, usernameofplayer);
        Review review = new Review(comment,rating);
        statement.setInt(3,review.getReferenceNumber());

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

    }

    public static ArrayList<Review> getGivenReviewsOf(String usernameOfPlayer) throws SQLException{
        ArrayList<Review> reviews = new ArrayList<>();

        //SQL String definiëren
        String sql = "SELECT reference_number FROM Reviews WHERE USERNAMEplayer = '" + usernameOfPlayer + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //juiste referencenummers uit de ResultSet halen
        ArrayList<Integer> nummers = new ArrayList<>();
        while(resultSet.next()){
            int nummer = resultSet.getInt("reference_number");
            nummers.add(nummer);
        }

        //juiste reviews ophalen
        for(int nummer: nummers){
            //SQL String definiëren
            String sql1 = "SELECT * FROM Review WHERE reference_nr = '" + nummer + "'";

            //SQL statement object aanmaken
            Statement statement1 = MySQLConnect.selectQuery();

            //SQL statement uitvoeren
            ResultSet resultSet1 = statement.executeQuery(sql1);

            //Review aanmaken en in arraylist zetten
            while(resultSet1.next()){
                String comment = resultSet1.getString("comment");
                int rating = resultSet1.getInt("rating");
                Timestamp timestamp = resultSet1.getTimestamp("date");
                LocalDateTime datetime = timestamp.toLocalDateTime();
                Review review = new Review(comment,rating,datetime);
                reviews.add(review);
            }
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Reviews sorteren en teruggeven
        Collections.sort(reviews);
        return reviews;
    }

    public static ArrayList<Review> getReceivedReviewsOf(String usernameOfOrganizer) throws SQLException{
        ArrayList<Review> reviews = new ArrayList<>();

        //SQL String definiëren
        String sql = "SELECT reference_number FROM Reviews WHERE USERNAMEorganizer = '" + usernameOfOrganizer + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //juiste referencenummers uit de ResultSet halen
        ArrayList<Integer> nummers = new ArrayList<>();
        while(resultSet.next()){
            int nummer = resultSet.getInt("reference_number");
            nummers.add(nummer);
        }

        //juiste reviews ophalen
        for(int nummer: nummers){
            //SQL String definiëren
            String sql1 = "SELECT * FROM Review WHERE reference_nr = '" + nummer + "'";

            //SQL statement object aanmaken
            Statement statement1 = MySQLConnect.selectQuery();

            //SQL statement uitvoeren
            ResultSet resultSet1 = statement.executeQuery(sql1);

            //Review aanmaken en in arraylist zetten
            while(resultSet1.next()){
                String comment = resultSet1.getString("comment");
                int rating = resultSet1.getInt("rating");
                Timestamp timestamp = resultSet1.getTimestamp("date");
                LocalDateTime datetime = timestamp.toLocalDateTime();
                Review review = new Review(comment,rating,datetime);
                reviews.add(review);
            }
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Reviews sorteren en teruggeven
        Collections.sort(reviews);
        return reviews;
    }

    public int getReferenceNumber() {
        return referenceNumber;
    }

    public String getComment() {
        return comment;
    }

    public int getRating() {
        return rating;
    }

    public int compareTo(Review otherReview){
        if(this.date.isBefore(otherReview.date)){
            return 1;
        }
        else{
            return -1;
        }
    }

    public String toString(){
        String string = "[Rating: "+this.getRating()+"] "+this.date.getYear()+"-"+this.date.getMonthValue()+"-"+this.date.getDayOfMonth()+"\n"+this.getComment();
        return string;
    }



    public static double CalculateAverageGivenScore(String usernameOfPlayer) throws SQLException{
        ArrayList<Review> reviews = Review.getGivenReviewsOf(usernameOfPlayer);
        int aantalreviews = reviews.size();
        double totaalscore = 0.00;
        for(Review review : reviews){
            totaalscore = totaalscore + review.getRating();
        }
        return round(totaalscore/aantalreviews,2);
    }

    public static double CalculateAverageReceivedScore(String usernameOfOrganizer) throws SQLException{
        ArrayList<Review> reviews = Review.getReceivedReviewsOf(usernameOfOrganizer);
        int aantalreviews = reviews.size();
        double totaalscore = 0.00;
        for(Review review : reviews){
            totaalscore = totaalscore + review.getRating();
        }
        return round(totaalscore/aantalreviews,2);
    }

    private static double round(double input, int nrOfDigits) {
        return ((double) Math.round(input * Math.pow(10, nrOfDigits)))/Math.pow(10, nrOfDigits);
    }

    public static void main(String[] args) throws SQLException{
        ArrayList<Review> reviews = Review.getReceivedReviewsOf("xanderDG2");
        for(Review review : reviews){
            System.out.println(review.toString());
        }
    }

}

