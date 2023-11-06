package project.photogallery.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import project.photogallery.models.Login;
import project.photogallery.models.User;
import project.photogallery.repositories.AccountRepository;
import project.photogallery.services.RegistrationException;

@RestController
@RequestMapping(path = "/api")
public class LoginRestController {
    
    @Autowired
    private AccountRepository accRepo;

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) throws IOException {

        Login login = new Login();
        login.setEmail(email);
        login.setPassword(password);
        System.out.println(login);

        Optional<User> user = accRepo.loginAndSelectUsername(login);
        if (user.isEmpty()) {
            JsonObject resp = Json.createObjectBuilder()
                .add("message", "Invalid Credentials")
                .add("code", 400)
                .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp.toString());
        }

        JsonObject resp = Json.createObjectBuilder()
            .add("user", email)
            .add("code", 200)
            .build();


        return ResponseEntity.status(HttpStatus.OK).body(resp.toString());
    }
}
