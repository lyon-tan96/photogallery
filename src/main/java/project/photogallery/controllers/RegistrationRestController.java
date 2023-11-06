package project.photogallery.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import project.photogallery.models.Registration;
import project.photogallery.services.RegistrationException;
import project.photogallery.services.RegistrationService;

@RestController
@RequestMapping(path = "/api")
public class RegistrationRestController {

    @Autowired
    private RegistrationService registrationSvc;

    @Autowired
    private AmazonS3 amazonS3;

    private String IMG_URL = "https://myproject.sgp1.digitaloceanspaces.com";

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> newRegistration(@RequestPart MultipartFile myFile, @RequestPart String username, @RequestPart String email, @RequestPart String password) throws IOException {
        
        Registration r = new Registration();

        Map<String, String> myData = new HashMap<>();


        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(myFile.getContentType());
        metadata.setContentLength(myFile.getSize());
        metadata.setUserMetadata(myData);

        String imageUrl = IMG_URL + "/" + username;

        r.setUsername(username);
        r.setEmail(email);
        r.setPassword(password);
        r.setPic(imageUrl);

        try {
            PutObjectRequest putReq = new PutObjectRequest("myproject", "profilepic/%s".formatted(username), 
            myFile.getInputStream(), metadata);
            putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putReq);

            registrationSvc.registerUser(r);

            JsonObject data = Json.createObjectBuilder()
                .add("name", myFile.getName())
                .add("content-type", myFile.getContentType())
                .add("size", myFile.getSize())
                .build();

            return ResponseEntity.status(HttpStatus.OK).body(data.toString());

        } catch (RegistrationException ex) {

            System.out.println(ex.getReason());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.toString());
        }
        

    }
    
}
