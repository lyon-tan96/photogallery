package project.photogallery.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.photogallery.models.Login;
import project.photogallery.models.Registration;
import project.photogallery.models.User;
import project.photogallery.repositories.AccountRepository;

@Service
public class LoginService {
    
    @Autowired
    private AccountRepository accRepo;

    // public User registerUser(Login login) throws RegistrationException {
    //     Optional<User> user = accRepo.loginAndSelectUsername(login);
    //     if (user.isEmpty()) {
    //         throw new RegistrationException("Username/Email is already registered in the system.");
    //     } else {
    //         return user;
    //     }
    // }
}
