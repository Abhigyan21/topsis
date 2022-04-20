package topsis;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import topsis.models.ParameterWeightage;
import topsis.service.SupplierRankingService;

import java.io.IOException;

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
        service.generateSupplierRanking();
    }
}
