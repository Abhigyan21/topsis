package topsis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import topsis.models.ParameterWeightage;
import topsis.repository.SupplierRankingRepository;
import topsis.service.SupplierRankingService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Topsis implements RequestHandler<S3Event, String> {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LoggerFactory.getLogger(Topsis.class);

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        logger.info("EVENT: " + gson.toJson(s3Event));
        S3EventNotification.S3EventNotificationRecord record = s3Event.getRecords().get(0);

        String srcBucket = record.getS3().getBucket().getName();

        // Object key may have spaces or unicode non-ASCII characters.
        String srcKey = record.getS3().getObject().getUrlDecodedKey();

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").build();
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                srcBucket, srcKey));
        InputStream objectData = s3Object.getObjectContent();

        try {
            Path path = Paths.get("/tmp/" + srcKey);
            Files.copy(objectData, path, StandardCopyOption.REPLACE_EXISTING);

            logger.info("File Copied successfully. FileName: " + path);

            ParameterWeightage parameterWeightage = new ParameterWeightage();
            parameterWeightage.setAcceptanceQualityLevelWeightage(0.3);
            parameterWeightage.setAvgResponseTimeWeightage(0.2);
            parameterWeightage.setSampleAcceptanceRateWeightage(0.3);
            parameterWeightage.setSamplingTimeWeightage(0.1);
            parameterWeightage.setCertificationCountWeightage(0.1);

            SupplierRankingService service = new SupplierRankingService(path, parameterWeightage);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return "Lambda Processing Completed Successfully!!!";
    }
}
