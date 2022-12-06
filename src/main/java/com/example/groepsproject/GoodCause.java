package com.example.groepsproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GoodCause {
    private String nameFoundation;
    private String IBANNumber;

    public GoodCause(String nameFoundation, String IBANNumber) throws SQLException {
        this.nameFoundation = nameFoundation;
        this.IBANNumber = IBANNumber;

        //SQL string definiëren
        String sql = "INSERT INTO Good_Cause (NAME_FOUNDATION, IBAN_nr) VALUES (?, ?)";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.insertQuery(sql);

        //Parameters opgeven
        statement.setString(1, nameFoundation);
        statement.setString(2, IBANNumber);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

    }

    public static void makeGoodCause(String nameFoundation, String IBANNumber) throws SQLException{
        GoodCause goodCause = new GoodCause(nameFoundation,IBANNumber);
    }

    public static String getIBANnr(String nameOfTheFoundation) throws SQLException{
        //SQL String definiëren
        String sql = "SELECT IBAN_nr FROM Good_Cause WHERE NAME_FOUNDATION = '" + nameOfTheFoundation + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //IBANnr uit de ResultSet halen
        String iban_nr = "";
        while(resultSet.next()) {
            iban_nr = resultSet.getString("IBAN_nr");
        }
        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //IBANnr teruggeven
        return iban_nr;

    }

    public static void setIBANR(String iban_nr, String nameOfTheFoundation) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Good_cause SET IBAN_nr=? WHERE NAME_FOUNDATION = '" + nameOfTheFoundation + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,iban_nr);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static void delete(String nameOfTheFoundation) throws SQLException{
        //SQL String definiëren
        String sql = "DELETE FROM Good_Cause WHERE NAME_FOUNDATION = '" + nameOfTheFoundation + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.deleteQuery(sql);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static double amountOfMoneyRaised(String nameOfTheFoundation) throws SQLException {
        //Alle tornooiennummers van de tornooien selecteren met de gegeven Good Cause
        //SQL String definiëren
        String sql = "SELECT TournamentNR FROM Choice_good_cause WHERE NAME_FOUNDATION = '" + nameOfTheFoundation + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Tornooinummer uit de ResultSet halen
        int tournamentNR = 0;
        ArrayList<Integer> tournamentNRList = new ArrayList<Integer>();
        while(resultSet.next()) {
            tournamentNR = resultSet.getInt("TournamentNR");
            tournamentNRList.add(tournamentNR);
        }
        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Voor elk tornooi de entry price, het aantal ingeschreven deelnemers en het deel dat aan het goed doel
        //wordt geschonken, vermenigvuldigen en deze bedragen optellen
        double amountOfMoneyRaised = 0;
        for(int tournamentNumber: tournamentNRList) {
            double entryPrice = Tournament.getEntryPrice(tournamentNumber);
            int signedUpParticipants = Tournament.getSignedUpParticipants(tournamentNumber);
            double procentGoodCause = Tournament.getProcentGoodCause(tournamentNumber);
            amountOfMoneyRaised = amountOfMoneyRaised + entryPrice * signedUpParticipants * (procentGoodCause/100);
        }

        //Het totale bedrag teruggeven
        return amountOfMoneyRaised;
    }




    public static void main(String[] args) throws SQLException {
        System.out.println(amountOfMoneyRaised("WWF"));;
    }



}
