package codeexamples;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class Examples {
    public static void main(String[] args) throws IOException {

        // load properties
        Properties properties = PropertiesLoader.load("config.properties");
        final String profile = properties.getProperty("S3_PROFILE");
        final String hostname = properties.getProperty("HOSTNAME");
        final String port = properties.getProperty("S3_PORT");

        final String address = "https://" + hostname + ":" + port;

        final AWSCredentialsProvider credentials = new ProfileCredentialsProvider(profile);
        final S3ClientOptions options = new S3ClientOptions().withPathStyleAccess(true);

        final AmazonS3Client s3 = new AmazonS3Client(credentials);
        s3.setEndpoint(address);
        s3.setS3ClientOptions(options);

        System.out.println("\nGetting account owner information...");
        Owner owner = s3.getS3AccountOwner();
        System.out.println("S3 Account Owner Name: " + owner.getDisplayName());
        System.out.println("S3 Account Owner Id: " + owner.getId());

        /*
         * Bucket related operations
         */

        // List buckets
        System.out.println("\nListing buckets...");
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket b : buckets) {
            System.out.println("Bucket name: " + b.getName());
            System.out.println("Bucket owner: " + b.getOwner());
            System.out.println("Bucket creation date: " + b.getCreationDate());
            System.out.println("");
        }

        // Create bucket
        System.out.println("\nCreating bucket...");
        s3.createBucket("new-bucket");

        // Delete bucket
        System.out.println("\nDeleting bucket...");
        s3.deleteBucket("new-bucket");

        /*
         * Object related operations
         */

        // Create object (in real life, point the InputStream to a file)
        System.out.println("\nCreating object...");
        final String content = "This is my object data.";
        final ObjectMetadata metadata = new ObjectMetadata();
        final InputStream inputStream = IOUtils.toInputStream(content, "UTF-8");
        metadata.addUserMetadata("mykey1", "myvalue1");
        metadata.addUserMetadata("mykey2", "myvalue2");
        // encrypt the object if desired
        metadata.setSSEAlgorithm("AES256");
        s3.putObject("test", "my_object", inputStream, metadata);

        // List objects
        System.out.println("\nListing objects...");
        ObjectListing objectListing = s3.listObjects("test");
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        for (S3ObjectSummary o : objects) {
            System.out.println("Object name: " + o.getKey());
            System.out.println("Object size: " + o.getSize() + " bytes");
            System.out.println("Object modification date: " + o.getLastModified());
            System.out.println("");
        }

        // Get object
        System.out.println("\nRetrieving object...");
        S3Object object = s3.getObject("test", "my_object");
        List<String> retrievedContent = IOUtils.readLines(object.getObjectContent());
        System.out.println("Object content: " + retrievedContent.get(0));

        // Get object metadata
        System.out.println("\nRetrieving object metadata...");
        ObjectMetadata oMetadata = s3.getObjectMetadata("test", "my_object");
        for (Entry<String, String> md : oMetadata.getUserMetadata().entrySet()) {
            System.out.println("Object metadata: " + md.getKey() + "=" + md.getValue());
        }
        System.out.println("Object content length: " + oMetadata.getContentLength());
        System.out.println("Object modification date: " + oMetadata.getLastModified());

        // Delete object
        System.out.println("\nDeleting object...");
        s3.deleteObject("test", "my_object");

    }
}
