package project.photogallery.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import project.photogallery.models.Response;
import project.photogallery.services.GalleryService;

@RestController
@RequestMapping(path = "/api")
public class GalleryRestController {
    
    @Autowired
    private GalleryService gallerySvc;

    @Autowired
    private AmazonS3 amazonS3;

    @GetMapping(path = "/gallery")
    public ResponseEntity<String> getGallery() {

        List<String> objects = gallerySvc.listObjectsInFolder();

        JsonArrayBuilder array = Json.createArrayBuilder();
        for (String object: objects) {
            array.add(object);
        }

        JsonArray jsonArray = array.build();
        System.out.println(jsonArray);
        
        return ResponseEntity.ok().body(jsonArray.toString());
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadToGallery(@RequestPart MultipartFile file) {

        
        Map<String, String> myData = new HashMap<>();

        String postId = UUID.randomUUID().toString().substring(0,8);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setUserMetadata(myData);
        
        try {
            PutObjectRequest putReq = new PutObjectRequest("myproject", "gallery/%s".formatted(postId), file.getInputStream(), metadata);
            putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
            amazonS3.putObject(putReq);

            JsonObject resp = Json.createObjectBuilder()
            .add("postId", postId)
            .add("code", 200)
            .build();

            return ResponseEntity.status(HttpStatus.OK).body(resp.toString());

        } catch (Exception e) {
            // TODO: handle exception
            Response resp = new Response();
            resp.setCode(400);
            resp.setMessage("400 Bad Request");
            
            return ResponseEntity.status(HttpStatus.OK).body(resp.toJson().toString());
        }
    }
}
