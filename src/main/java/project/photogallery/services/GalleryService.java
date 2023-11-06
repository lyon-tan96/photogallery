package project.photogallery.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class GalleryService {
    
    @Autowired
    private AmazonS3 amazonS3;

    String bucketName = "myproject";
    String folderName = "gallery/";

    private String generateObjectURL(S3ObjectSummary objectSummary) {
        return amazonS3.getUrl(bucketName, objectSummary.getKey()).toString();
    }

    public List<String> listObjectsInFolder() {

        List<String> pictureUrls = new ArrayList<>();

        ObjectListing objectListing = amazonS3.listObjects(bucketName, folderName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        
        for (S3ObjectSummary objectSummary: objectSummaries) {
            String pictureUrl = generateObjectURL(objectSummary);
            pictureUrls.add(pictureUrl);
        }
        
        pictureUrls.remove(0);
        return pictureUrls;
    }

    public byte[] convertHeicToJpg(MultipartFile heicFile) throws IOException {
        BufferedImage image = ImageIO.read(heicFile.getInputStream());

        if (image == null) {
            throw new IOException("Failed to read the HEIC image.");
        }

        // Create a ByteArrayOutputStream to hold the JPG data
        ByteArrayOutputStream jpgOutput = new ByteArrayOutputStream();

        // Write the image as JPG to the output stream
        ImageIO.write(image, "jpg", jpgOutput);

        return jpgOutput.toByteArray();
    }


}
