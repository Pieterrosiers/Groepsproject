package com.example.groepsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;

@SpringBootApplication
public class GroepsprojectApplication {


    @GetMapping("/registerPlayer")
    public String registrationStudent(Model model){
        model.addAttribute("player",new Player());
        return "/registerPlayer";
    }


    @PostMapping("/registerPlayer")
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



    public static void main(String[] args) {
        SpringApplication.run(GroepsprojectApplication.class, args);
    }


}
