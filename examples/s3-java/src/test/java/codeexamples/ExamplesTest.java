package codeexamples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
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
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.Upload;

public class ExamplesTest {

    final private S3ClientOptions options = new S3ClientOptions().withPathStyleAccess(true);
    private AmazonS3Client s3;

    private static String address;
    private static AWSCredentialsProvider credentials;

    // Test variables
    final private String testBucket = "testbucket-" + System.currentTimeMillis();
    final private String testContent = "This is my test object's content!";
    final private String testObjectKey = "test_object";

    final private File testFile = new File("src/test/resources/testfile");
    final private File retrievedFile = new File("src/test/resources/testfile.retrieved");

    @BeforeClass
    public static void loadConfig() throws IOException {
        // load properties
        Properties properties = PropertiesLoader.load("config.properties");
        String profile = properties.getProperty("S3_PROFILE");
        String hostname = properties.getProperty("HOSTNAME");
        String port = properties.getProperty("S3_PORT");
        address = "https://" + hostname + ":" + port;

        // initialize AWS credentials
        credentials = new ProfileCredentialsProvider(profile);
    }

    @Before
    public void setup() {
        s3 = new AmazonS3Client(credentials);
        s3.setEndpoint(address);
        s3.setS3ClientOptions(options);
        s3.createBucket(testBucket);
    }

    @After
    public void cleanup() {
        s3.deleteBucket(testBucket);
        FileUtils.deleteQuietly(retrievedFile);
    }

    @Test
    public void testListBuckets() {
        List<Bucket> buckets = s3.listBuckets();
        assertTrue(buckets.size() >= 0);
    }

    @Test
    public void testCreateAndDeleteBucket() {

        // create new bucket
        String bucketName = "bucket-" + System.currentTimeMillis();
        s3.createBucket(bucketName);

        // make sure it is there
        List<Bucket> buckets = s3.listBuckets();
        List<String> bucketNames = extractBucketNames(buckets);
        assertTrue(bucketNames.contains(bucketName));

        // delete it
        s3.deleteBucket(bucketName);

        // make sure it is gone
        buckets = s3.listBuckets();
        bucketNames = extractBucketNames(buckets);
        assertFalse(bucketNames.contains(bucketName));
    }

    @Test
    public void testGetS3AccountOwner() {
        Owner owner = s3.getS3AccountOwner();

        // Makes sure it returns something
        assertTrue(owner.getDisplayName().length() > 0);
        assertTrue(owner.getId().length() > 24);
    }

    @Test
    public void testMultiPartUpload() throws IOException, AmazonServiceException, AmazonClientException,
        InterruptedException {

        // Setup TransferManager for multi-part uploads (1MB parts for testing)
        TransferManagerConfiguration c = new TransferManagerConfiguration();
        c.setMultipartUploadThreshold(1 * 1024 * 12024);
        TransferManager tm = new TransferManager(s3);
        tm.setConfiguration(c);

        // Upload object and wait until it is done
        Upload upload = tm.upload(testBucket, testObjectKey, testFile);
        upload.waitForCompletion();

        // Download ingested object
        Download download = tm.download(testBucket, testObjectKey, retrievedFile);
        download.waitForCompletion();

        // Make sure they are the Same
        assertTrue(FileUtils.contentEquals(testFile, retrievedFile));

        // Delete the object
        s3.deleteObject(testBucket, testObjectKey);
    }

    @Test
    public void testCreateAndDeleteObject() throws IOException {
        // create object
        final ObjectMetadata metadata = new ObjectMetadata();
        final InputStream inputStream = IOUtils.toInputStream(testContent, "UTF-8");
        metadata.addUserMetadata("mykey1", "myvalue1");
        metadata.addUserMetadata("mykey2", "myvalue2");
        s3.putObject(testBucket, testObjectKey, inputStream, metadata);

        // list objects and make sure it is there
        ObjectListing objectListing = s3.listObjects(testBucket);
        List<String> keys = extractObjectKeys(objectListing);
        assertTrue(keys.contains(testObjectKey));

        // make sure it is our object
        S3Object object = s3.getObject(testBucket, testObjectKey);
        List<String> retrievedContent = IOUtils.readLines(object.getObjectContent());
        assertTrue(testContent.equals(retrievedContent.get(0)));

        // make sure the metadata matches too
        ObjectMetadata oMetadata = s3.getObjectMetadata(testBucket, testObjectKey);
        assertEquals(2, oMetadata.getUserMetadata().keySet().size());
        assertTrue("myvalue1".equals(oMetadata.getUserMetadata().get("mykey1")));
        assertTrue("myvalue2".equals(oMetadata.getUserMetadata().get("mykey2")));

        // delete object
        s3.deleteObject(testBucket, testObjectKey);

        // make sure it is done
        objectListing = s3.listObjects(testBucket);
        keys = extractObjectKeys(objectListing);
        assertFalse(keys.contains(testObjectKey));
    }

    @Test
    public void testListObject() {
        ObjectListing objectListing = s3.listObjects(testBucket);
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        assertEquals(0, objects.size());
    }

    private List<String> extractBucketNames(List<Bucket> buckets) {
        List<String> bucketNames = new ArrayList<String>();
        for (Bucket b : buckets) {
            bucketNames.add(b.getName());
        }
        return bucketNames;
    }

    private List<String> extractObjectKeys(ObjectListing objectListing) {
        List<String> objectKeys = new ArrayList<String>();
        List<S3ObjectSummary> objects = objectListing.getObjectSummaries();
        for (S3ObjectSummary o : objects) {
            objectKeys.add(o.getKey());
        }
        return objectKeys;
    }

}
