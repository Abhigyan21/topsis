package topsis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import topsis.models.Factory;
import topsis.models.ParameterWeightage;
import topsis.repository.SupplierRankingRepository;
import topsis.service.SupplierRankingService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Topsis implements RequestHandler<String, String> {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LoggerFactory.getLogger(Topsis.class);

    @Override
    public String handleRequest(String data, Context context) {
        ObjectMapper mapper = new ObjectMapper();
        ParameterWeightage parameterWeightage = null;
        Gson jsonMapper = new GsonBuilder().create();

        try {
            parameterWeightage = mapper.readValue(jsonMapper.toJson(data), ParameterWeightage.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to process json data. Exception - " + e.getMessage());
        }

        logger.info("EVENT: " + gson.toJson(data));

        String srcBucket = "topsis";
        String srcKey = "input.xlsx";

        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion("ap-south-1").build();
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                srcBucket, srcKey));
        InputStream objectData = s3Object.getObjectContent();

        try {
            Path path = Paths.get("/tmp/" + srcKey);
            Files.copy(objectData, path, StandardCopyOption.REPLACE_EXISTING);

            logger.info("File Copied successfully. FileName: " + path);

            SupplierRankingService service = new SupplierRankingService("/tmp/" + srcKey, parameterWeightage);
            return jsonMapper.toJson(service.generateSupplierRanking());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        return "Lambda Processing Completed Successfully!!!";
    }
}
