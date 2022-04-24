package topsis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import topsis.models.ParameterWeightage;
import topsis.models.ResponseData;
import topsis.service.SupplierRankingService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Driver {
    public static void main(String[] args) throws IOException, InvalidFormatException {
        ParameterWeightage parameterWeightage = new ParameterWeightage();
        parameterWeightage.setAcceptanceQualityLevelWeightage(0.3);
        parameterWeightage.setAvgResponseTimeWeightage(0.2);
        parameterWeightage.setSampleAcceptanceRateWeightage(0.3);
        parameterWeightage.setSamplingTimeWeightage(0.1);
        parameterWeightage.setCertificationCountWeightage(0.1);

        String path = "/Users/abhigyanmandal/Downloads/Topsis Data Test.xlsx";
        SupplierRankingService service = new SupplierRankingService(path, parameterWeightage);
        //service.generateSupplierRanking();

        List<ResponseData> responseData = new ArrayList<>();

        service.generateSupplierRanking().stream().forEach(
                factory -> responseData.add(ResponseData.builder()
                        .factoryName(factory.getFactoryName())
                        .relativeCloseness(factory.getRelativeCloseness())
                        .build()));

        Gson jsonMapper = new GsonBuilder().create();
        String response = "{" +
                "\"isBase64Encoded\":false," +
                "\"statusCode\": 200," +
                "\"headers\": {\"Access-Control-Allow-Origin\":\"*\"}," +
                "\"body\":" +
                jsonMapper.toJson(responseData) +
                "}";

        System.out.println(response);
    }
}
