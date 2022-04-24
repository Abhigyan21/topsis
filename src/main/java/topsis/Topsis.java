package topsis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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
import topsis.models.ParameterWeightage;
import topsis.models.ResponseData;
import topsis.service.SupplierRankingService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Topsis implements RequestHandler<Object, String> {

    private static final Logger logger = LoggerFactory.getLogger(Topsis.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String handleRequest(Object data, Context context) {
        ObjectMapper mapper = new ObjectMapper();
        ParameterWeightage parameterWeightage = null;
        Gson jsonMapper = new GsonBuilder().create();
        String inputData = "";

        if (data instanceof String) {
            inputData = (String) data;
        } else if (data instanceof Map) {
            inputData = gson.toJson(data);
        }

        logger.info("EVENT: " + inputData);

        try {
            parameterWeightage = mapper.readValue(inputData, ParameterWeightage.class);
        } catch (JsonProcessingException e) {
            logger.error("Failed to process json data. Exception - " + e.getMessage());
        }

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
            List<ResponseData> responseData = new ArrayList<>();

            service.generateSupplierRanking().stream().forEach(
                    factory -> responseData.add(ResponseData.builder()
                    .factoryName(factory.getFactoryName())
                    .relativeCloseness(factory.getRelativeCloseness())
                    .build()));

            logger.info("Lambda Processing Completed Successfully!!!");

            String response = "{" +
                    "\"isBase64Encoded\":false," +
                    "\"statusCode\": 200" +
                    "\"headers\": {\"Access-Control-Allow-Origin\":\"*\"}" +
                    "\"body\":" +
                    jsonMapper.toJson(responseData) +
                    "}";

            return response;
        } catch (IOException | InvalidFormatException e) {
            logger.error("Exception occurred while processing lambda: " + e.getMessage());
        }

        return "Lambda Processing Completed Successfully!!!";
    }
}
