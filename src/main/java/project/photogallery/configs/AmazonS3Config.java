package project.photogallery.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonS3Config {
    
    @Value("${S3_ACCESS_KEY}")
    private String accessKey;

    @Value("${S3_SECRET_KEY}")
    private String secretKey;

    @Bean
    public AmazonS3 createS3Client() {
        BasicAWSCredentials creds = new BasicAWSCredentials(accessKey, secretKey);
        
        EndpointConfiguration endpointConfiguration = new EndpointConfiguration("sgp1.digitaloceanspaces.com", "sgp1");

        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(endpointConfiguration)
            .withCredentials(new AWSStaticCredentialsProvider(creds))
            .build();
    }

}
