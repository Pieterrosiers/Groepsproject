package com.example.groepsproject;

import java.sql.*;
import java.time.LocalDate;
import java.time.Period;


public class Player extends Person {

    public Player(String firstName, String lastName, String mail, String username, String password, LocalDate dateOfBirth, String IBANnr, int dummy) {
        super(firstName,lastName,mail,username,password,dateOfBirth,IBANnr);
    }
    public Player(String firstName, String lastName, String mail, String username, String password, LocalDate dateOfBirth, String IBANnr) throws SQLException {

        super(firstName,lastName,mail,username,password,dateOfBirth,IBANnr);

        //Localdate naar date omzetten
        Date date = java.sql.Date.valueOf(dateOfBirth);

        //Leeftijd van de persoon berekenen
        LocalDate now = LocalDate.now();
        Period period = Period.between(dateOfBirth,now);
        int age = period.getYears();

        //SQL string definiëren
        String sql = "INSERT INTO Player (USERNAME, name, firstname, mail, password, date_of_birth, age, IBAN_nr) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.insertQuery(sql);

        //Parameters opgeven
        statement.setString(1, username);
        statement.setString(2, lastName);
        statement.setString(3, firstName);
        statement.setString(4, mail);
        statement.setString(5, password);
        statement.setDate(6, date);
        statement.setInt(7,age);
        statement.setString(8, IBANnr);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }
    static public void makeNewPlayer(String firstName, String lastName, String mail, String username, String password, LocalDate dateOfBirth, String IBANnr) throws SQLException {
        Player player = new Player(firstName, lastName, mail, username, password, dateOfBirth, IBANnr);
    }

    static public String getFirstName(String username) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT firstname FROM Organizer WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Firstname uit de ResultSet halen
        String firstname = "";
        while(resultSet.next()) {
            firstname = resultSet.getString("firstname");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Firstname teruggeven
        return firstname;
    }

    static public String getLastName(String username) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT name FROM Organizer WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Lastname uit de ResultSet halen
        String lastname = "";
        while(resultSet.next()) {
            lastname = resultSet.getString("name");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Firstname teruggeven
        return lastname;
    }

    static public String getMail(String username) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT mail FROM Organizer WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Mail uit de ResultSet halen
        String mail = "";
        while(resultSet.next()) {
            mail = resultSet.getString("mail");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Mail teruggeven
        return mail;
    }

    static public String getPassword(String username) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT password FROM Organizer WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Password uit de ResultSet halen
        String password = "";
        while(resultSet.next()) {
            password = resultSet.getString("password");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Password teruggeven
        return password;
    }

    static public LocalDate getDateOfBirth(String username) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT date_of_birth FROM Organizer WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Date uit de ResultSet halen
        Date date = null;
        while(resultSet.next()) {
            date = resultSet.getDate("date_of_birth");
        }

        //Date van SQL omzetten naar LocalDate
        String dateString = date.toString();
        LocalDate localDate = Date.valueOf(dateString).toLocalDate();


        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Password teruggeven
        return localDate;
    }

    static public String getIBANnr(String username) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT IBAN_nr FROM Organizer WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //IBANnr uit de ResultSet halen
        String IBANnr = "";
        while(resultSet.next()) {
            IBANnr = resultSet.getString("IBAN_nr");
        }

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //Password teruggeven
        return IBANnr;
    }

    static public void setFirstName(String firstname, String username) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Organizer SET firstname=? WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,firstname);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setLastName(String lastName, String username) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Organizer SET name=? WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,lastName);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setMail(String mail, String username) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Organizer SET mail=? WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,mail);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setPassword(String password, String username) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Organizer SET password=? WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,password);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setDateOfBirth(LocalDate localDate, String username) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Organizer SET date_of_birth=? WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //LocalDate omzetten naar Date van SQL
        Date date = java.sql.Date.valueOf(localDate);
        String dateString = date.toString();

        //Parameter opgeven
        statement.setString(1,dateString);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    static public void setIBANnr(String IBANnr, String username) throws SQLException {
        //SQL String definiëren
        String sql = "UPDATE Organizer SET IBAN_nr=? WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.updateQuery(sql);

        //Parameter opgeven
        statement.setString(1,IBANnr);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static void delete(String username) throws SQLException{
        //SQL String definiëren
        String sql = "DELETE FROM Organizer WHERE USERNAME = '" + username + "'";

        //SQL statement object aanmaken
        PreparedStatement statement = MySQLConnect.deleteQuery(sql);

        //SQL statement uitvoeren
        statement.executeUpdate();

        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;
    }

    public static boolean usernameExists(String username) throws SQLException {
        //SQL String definiëren
        String sql = "SELECT USERNAME FROM Player";

        //SQL statement object aanmaken
        Statement statement = MySQLConnect.selectQuery();

        //SQL statement uitvoeren
        ResultSet resultSet = statement.executeQuery(sql);

        //Username uit de ResultSet halen en controleren of de username in de tabel zit
        String usernameFromDB = "";
        boolean b = false;
        while (resultSet.next()) {
            username = resultSet.getString("USERNAME");
            if (usernameFromDB.equals(username)) {
                b = true;
            }
        }
        //Connection sluiten
        MySQLConnect.staticConnection.close();
        MySQLConnect.staticConnection = null;

        //De boolean b retourneren
        return b;
    }
    public static void main(String[] args) throws SQLException {

    }
}
