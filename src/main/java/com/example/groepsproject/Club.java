package com.example.groepsproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Club {
    private int clubNumber;
    private String clubName;
    private String address;

    public static int generateNewClubNumber() throws SQLException {
        //nieuw club number = hoogste club number dat tot nu toe is gebruikt + 1

        //SQL String definiëren
        String sql = "SELECT CLUB_NR FROM Club";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Hoogste club number uit de ResultSet halen
        int hoogsteClubNumber = 0;
        while(resultSet.next()) {
            if(resultSet.getInt("CLUB_NR")>hoogsteClubNumber){
                hoogsteClubNumber = resultSet.getInt("CLUB_NR");
            }
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Nieuw tournament number teruggeven
        int nieuwClubNumber = hoogsteClubNumber + 1;
        return nieuwClubNumber;
    }

    public Club(String clubName, String address) throws SQLException {
        this.clubNumber = generateNewClubNumber();
        this.clubName = clubName;
        this.address = address;

        //SQL string definiëren
        String sql = "INSERT INTO Club (CLUB_NR, clubname, address) VALUES (?, ?, ?)";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.insertQuery(sql);

        //Parameters opgeven
        statement.setInt(1, this.clubNumber);
        statement.setString(2, this.clubName);
        statement.setString(3, this.address);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static void makeNewClub(String clubName, String address) throws SQLException {
        Club club = new Club(clubName, address);
    }

    public static String getAddress(String clubName) throws SQLException{
        //SQL String definiëren
        String sql = "SELECT address FROM club WHERE clubname = '" + clubName + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Adress uit de ResultSet halen
        String adress = "";
        while(resultSet.next()) {
            adress = resultSet.getString("address");
        }
        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //adress teruggeven
        return adress;

    }

    public static void setAddress(String address, String clubName) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Club SET address=? WHERE clubname = '" + clubName + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,address);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static void delete(String clubName) throws SQLException{
        //SQL String definiëren
        String sql = "DELETE FROM club WHERE clubname = '" + clubName + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.deleteQuery(sql);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }


    public static void main(String[] args) throws SQLException {
        //Club.makeNewClub("DC De GazettenSmijter","Borluutstraat 1");
        //System.out.println(Club.getAdress("DC De GazettenSmijter"));
        //Club.setAdress("Deirmendreef 12","DC De GazettenSmijter");
        //Club.delete("De Gouden Dart");
    }


}
