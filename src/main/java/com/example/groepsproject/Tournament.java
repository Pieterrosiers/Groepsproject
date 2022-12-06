package com.example.groepsproject;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.example.groepsproject.Sport.DartsEnkel;

public class Tournament {
    private int tournamentNumber;
    private int SignedUpParticipants;
    private int maxParticipants;
    private LocalDateTime date;
    private double entryPrice;
    private double procentFee;
    private double procentGoodCause;
    private double procentPrizeMoney;
    private int clubNumber;
    private Sport sport;
    private String Name_GoodCause;
    private String usernameOrganizer;

    public static int generateNewTournamentNumber() throws SQLException {
        //nieuw tournament number = hoogste tournament number dat tot nu toe is gebruikt + 1

        //SQL String definiëren
        String sql = "SELECT TOURNAMENT_NR FROM Tournament";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Hoogste tournament number uit de ResultSet halen
        int hoogsteTournamentNumber = 0;
        while(resultSet.next()) {
            if(resultSet.getInt("TOURNAMENT_NR")>hoogsteTournamentNumber){
                hoogsteTournamentNumber = resultSet.getInt("TOURNAMENT_NR");
            }
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Nieuw tournament number teruggeven
        int nieuwTournamentNumber = hoogsteTournamentNumber + 1;
        return nieuwTournamentNumber;
    }

    public Tournament(int maxParticipants, LocalDateTime date, double entryPrice, double procentFee, double procentGoodCause, double procentPrizeMoney, int clubNumber, Sport sport, String name_GoodCause, String usernameOrganizer) throws SQLException {
        this.tournamentNumber = generateNewTournamentNumber();
        this.SignedUpParticipants = 0;
        this.maxParticipants = maxParticipants;
        this.date = date;
        this.entryPrice = entryPrice;
        this.procentFee = procentFee;
        this.procentGoodCause = procentGoodCause;
        this.procentPrizeMoney = procentPrizeMoney;
        this.clubNumber = clubNumber;
        this.sport = sport;
        String sportString = this.sport.toString();
        this.Name_GoodCause = name_GoodCause;
        this.usernameOrganizer = usernameOrganizer;

        //SQL strings definiëren
        String sql = "INSERT INTO Tournament (TOURNAMENT_NR, entry_price, max_participants, signed_up_participants, date, procent_fee, procent_good_cause, procent_prize_money, CLUB_NR, sport) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sql2 = "INSERT INTO choice_good_cause (TournamentNR, NAME_FOUNDATION) VALUES (?, ?)";
        String sql3 = "INSERT INTO organised_by (TOURNAMENT_NUMBER, USERNAMEo) VALUES (?, ?)";

        //SQL statement objects aanmaken
        PreparedStatement statement = MySQLConnect.insertQuery(sql);
        PreparedStatement statement2 = MySQLConnect.insertQuery(sql2);
        PreparedStatement statement3 = MySQLConnect.insertQuery(sql3);

        //Parameters opgeven
        statement.setInt(1, this.tournamentNumber);
        statement.setDouble(2, this.entryPrice);
        statement.setInt(3, this.maxParticipants);
        statement.setInt(4, 0);
        statement.setTimestamp(5, Timestamp.valueOf(this.date));
        statement.setDouble(6, this.procentFee);
        statement.setDouble(7, this.procentGoodCause);
        statement.setDouble(8, this.procentPrizeMoney);
        statement.setInt(9, this.clubNumber);
        statement.setString(10, sportString);

        statement2.setInt(1,this.tournamentNumber);
        statement2.setString(2,name_GoodCause);

        statement3.setInt(1, this.tournamentNumber);
        statement3.setString(2,this.usernameOrganizer);

        //SQL statements uitvoeren
        statement.executeUpdate();
        statement2.executeUpdate();
        statement3.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static void makeNewTournament(int maxParticipants, LocalDateTime date, double entryPrice, double procentFee, double procentGoodCause, double procentPrizeMoney, int clubNumber, Sport sport,  String name_GoodCause, String usernameOrganizer) throws SQLException {
        Tournament tournament = new Tournament(maxParticipants, date, entryPrice, procentFee, procentGoodCause, procentPrizeMoney, clubNumber, sport, name_GoodCause, usernameOrganizer);
    }

    public static boolean PercentageAddUpToHundred(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT procent_fee, procent_good_cause, procent_prize_money FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Procenten uit de ResultSet halen
        double procentFee = 0;
        double procentGoodCause = 0;
        double procentPrizeMoney = 0;
        boolean b = false;
        while (resultSet.next()) {
            procentFee = resultSet.getDouble("procent_fee");
            procentGoodCause = resultSet.getDouble("procent_good_cause");
            procentPrizeMoney = resultSet.getDouble("procent_prize_money");
            if (procentFee + procentGoodCause + procentPrizeMoney == 100) {
                b = true;
            }
        }
        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //De boolean b retourneren
        return b;
    }

    static public int getMaxParticipants(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT max_participants FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Max participants uit de ResultSet halen
        int maxParticipants = 0;
        while(resultSet.next()) {
            maxParticipants = resultSet.getInt("max_participants");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Max participants teruggeven
        return maxParticipants;
    }

    static public int getSignedUpParticipants(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT signed_up_participants FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Signed up participants uit de ResultSet halen
        int signedUpParticipants = 0;
        while(resultSet.next()) {
            signedUpParticipants = resultSet.getInt("signed_up_participants");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Signed up participants teruggeven
        return signedUpParticipants;
    }

    static public LocalDateTime getDateTime(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT date FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Date uit de ResultSet halen
        Timestamp date = null;
        while(resultSet.next()) {
            date = resultSet.getTimestamp("date");
        }

        //Date van SQL omzetten naar LocalDateTime
        String dateString = date.toString();
        LocalDateTime localDateTime = Timestamp.valueOf(dateString).toLocalDateTime();


        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Password teruggeven
        return localDateTime;
    }

    static public double getEntryPrice(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT entry_price FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Entry price uit de ResultSet halen
        double entryPrice = 0;
        while(resultSet.next()) {
            entryPrice = resultSet.getDouble("entry_price");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Entry prize teruggeven
        return entryPrice;
    }

    static public double getProcentFee(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT procent_fee FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Procent fee uit de ResultSet halen
        double procentFee = 0;
        while(resultSet.next()) {
            procentFee = resultSet.getDouble("procent_fee");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Procent fee teruggeven
        return procentFee;
    }

    static public double getProcentGoodCause(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT procent_good_cause FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Procent good cause uit de ResultSet halen
        double procentGoodCause = 0;
        while(resultSet.next()) {
            procentGoodCause = resultSet.getDouble("procent_good_cause");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Procent good cause teruggeven
        return procentGoodCause;
    }

    static public double getProcentPrizeMoney(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT procent_prize_money FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Procent prize money uit de ResultSet halen
        double procentPrizeMoney = 0;
        while(resultSet.next()) {
            procentPrizeMoney = resultSet.getDouble("procent_prize_money");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Procent prize money teruggeven
        return procentPrizeMoney;
    }

    static public int getClubNumber(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT CLUB_NR FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Club number uit de ResultSet halen
        int clubNumber = 0;
        while(resultSet.next()) {
            clubNumber = resultSet.getInt("CLUB_NR");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Club number teruggeven
        return clubNumber;
    }

    static public String getSport(int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT sport FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Sport uit de ResultSet halen
        String sport = "";
        while(resultSet.next()) {
            sport = resultSet.getString("sport");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Sport teruggeven
        return sport;
    }

    static public void setMaxParticipants(int maxParticipants, int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Tournament SET max_participants=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setInt(1,maxParticipants);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setSignedUpParticipants(int signedUpParticipants, int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Tournament SET signed_up_participants=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setInt(1,signedUpParticipants);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setDateTime(LocalDateTime dateTime, int tournamentNumber) throws SQLException {
        //dateTime omzetten naar Timestamp voor SQL
        Timestamp timestamp = Timestamp.valueOf(dateTime);

        //SQL String definiëren
        String sql = "UPDATE Tournament SET date=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setTimestamp(1,timestamp);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setEntryPrice(double entryPrice, int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Tournament SET entry_price=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setDouble(1,entryPrice);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setProcentFee(double procentFee, int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Tournament SET procent_fee=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setDouble(1,procentFee);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setProcentGoodCause(double procentGoodCause, int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Tournament SET procent_good_cause=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setDouble(1,procentGoodCause);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setProcentPrizeMoney(double procentPrizeMoney, int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Tournament SET procent_prize_money=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setDouble(1,procentPrizeMoney);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setClubNumber(int clubNumber, int tournamentNumber) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Tournament SET CLUB_NR=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setInt(1,clubNumber);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setSport(Sport sport, int tournamentNumber) throws SQLException {
        //Sport omzetten naar een String
        String sportString = sport.toString();

        //SQL String definiëren
        String sql = "UPDATE Tournament SET sport=? WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,sportString);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static void signUp(String username, int tournamentNumber) throws SQLException {
        //SQL string definiëren
        String sql = "INSERT INTO Competes (USERNAMEp, TOURNAMENT_NR) VALUES (?, ?)";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.insertQuery(sql);

        //Parameters opgeven
        statement.setString(1, username);
        statement.setInt(2, tournamentNumber);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Signed up participants van het tornooi met 1 verhogen
        int signedUpParticipants = getSignedUpParticipants(tournamentNumber);
        setSignedUpParticipants(signedUpParticipants + 1, tournamentNumber);
    }

    public static void delete(int tournamentNumber) throws SQLException{
        //SQL String definiëren
        String sql = "DELETE FROM Tournament WHERE TOURNAMENT_NR = '" + tournamentNumber + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.deleteQuery(sql);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static void main(String[] args) throws SQLException {
        makeNewTournament(30, LocalDateTime.now(), 15, 2, 4, 5, 2, DartsEnkel, "WWF", "xanderDG2");
        System.out.println(PercentageAddUpToHundred(11));
    }
}
