package com.example.groepsproject;
import java.sql.SQLException;
import java.time.LocalDate;

public class App {

    private static Player loggedInPlayer = null;
    private static Organizer loggedInOrganizer = null;

    public static Player getLoggedInPlayer() {
        return loggedInPlayer;
    }

    public static Organizer getLoggedInOrganizer() {
        return loggedInOrganizer;
    }


    public static void registerPlayer(String username,String firstName, String lastName, String mail, String password,String IBANnr, LocalDate dateOfBirth) throws PlayerException, SQLException {
        if(!Player.usernameExists(username)){
            throw new PlayerException("This username already exists!");
        }
        Player.makeNewPlayer(firstName,lastName,mail,username,password,dateOfBirth,IBANnr); //player in database zetten
        loggedInPlayer = new Player(firstName,lastName,mail,username,password,dateOfBirth,IBANnr,0); //via dummy constructor ingelogde speler aanpassen
    }


    public void registerOrganizer(String username,String firstName, String lastName, String mail, String password,String IBANnr, LocalDate dateOfBirth) throws OrganizerException, SQLException{
        if(!Organizer.usernameExists(username)){
            throw new OrganizerException("This username already exists!");
        }
        Organizer.makeNewOrganizer(firstName,lastName,mail,username,password,dateOfBirth,IBANnr); //organizer in database zetten
        loggedInOrganizer = new Organizer(firstName,lastName,mail,username,password,dateOfBirth,IBANnr,0); //via dummy constructor ingelogde organizer aanpassen
    }


    public void playerLogin(String username, String password) throws PlayerException, SQLException {
        if(Player.usernameExists(username)&&Player.getPassword(username).equals(password)){
            loggedInPlayer = new Player(Player.getFirstName(username), Player.getLastName(username), Player.getMail(username), username, password, Player.getDateOfBirth(username), Player.getIBANnr(username), 0); //via dummy constructor ingelogde speler aanpassen
        }
        else{
            throw new PlayerException("Username and/or password are incorrect, try again!\nIf you don't have an account yet, please register.");
        }
    }

    public void organizerLogin(String username, String password) throws OrganizerException, SQLException {
        if(Organizer.usernameExists(username)&&Organizer.getPassword(username).equals(password)){
            loggedInOrganizer= new Organizer(Organizer.getFirstName(username), Organizer.getLastName(username), Organizer.getMail(username), username, password, Organizer.getDateOfBirth(username), Organizer.getIBANnr(username), 0); //via dummy constructor ingelogde organizer aanpassen
        }
        else{
            throw new OrganizerException("Username and/or password are incorrect, try again!\nIf you don't have an account yet, please register.");
        }
    }
    public void logoutPlayer(){
        loggedInPlayer = null;
    }
    public void logoutOrganizer(){
        loggedInOrganizer = null;
    }

}

