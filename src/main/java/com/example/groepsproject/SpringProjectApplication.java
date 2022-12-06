package com.example.groepsproject;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;
import java.time.LocalDate;

@SpringBootApplication
@Controller
public class SpringProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringProjectApplication.class, args);
    }

    //Register

    @GetMapping ("/registerPlayer")
    public String registrationStudent(Model model){
        model.addAttribute("player",new Player());
        return "/registerPlayer";
    }


    @PostMapping ("/registerPlayer")
    public String registrationStudentPost(@ModelAttribute Player player, Model model, RedirectAttributes redirAttrs) {
        try {
            App.app.registerPlayer(player.getUsername(), player.getFirstName(), player.getLastName(), player.getMail(), player.getPassword(), player.getIBANnr(), player.getDateOfBirth());
            redirAttrs.addFlashAttribute("success", "Student registered");
            return "redirect:/homepage.html";
        } catch (PlayerException | SQLException e) {
            redirAttrs.addFlashAttribute("failure", e.getMessage());
            return "redirect:/registerStudent";
        }
    }



}
