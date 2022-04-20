package topsis.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import topsis.models.Factory;
import topsis.models.ParameterWeightage;
import topsis.models.SolutionValues;
import topsis.models.TotalValues;
import topsis.repository.SupplierRankingRepository;
import topsis.utility.ExcelReaderUtility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SupplierRankingService {

    private List<Factory> factoryList;
    private ParameterWeightage parameterWeightage;


    public SupplierRankingService(String excelFilePath, ParameterWeightage parameterWeightage) throws IOException, InvalidFormatException {
        this.factoryList = ExcelReaderUtility.readExcelFile(new File(excelFilePath));
        this.parameterWeightage = parameterWeightage;
    }

    public List<Factory> generateSupplierRanking() {
        factoryList.stream().forEach(SupplierRankingRepository::squareParameterWithSelfValue);

        TotalValues totalValues = SupplierRankingRepository.generateTotalValues(factoryList);

        factoryList.stream().forEach(factory -> SupplierRankingRepository.divideParameterWithSqrt(factory, totalValues));
        factoryList.stream().forEach(factory -> SupplierRankingRepository.divideParameterWithWeightage(factory, parameterWeightage));

        SolutionValues solutionValues = SupplierRankingRepository.generateSolutionValues(factoryList);

        factoryList.stream().forEach(factory -> SupplierRankingRepository.subtractParameterFromIdealSolutionAndSquare(factory, solutionValues));
        factoryList.stream().forEach(SupplierRankingRepository::calculateAverageIdealSolutionForFactoryParameters);
        factoryList.stream().forEach(factory -> SupplierRankingRepository.subtractParameterFromWeightageAndSquare(factory, parameterWeightage));
        factoryList.stream().forEach(SupplierRankingRepository::calculateAverageNegativeIdealSolutionForFactoryParameters);
        factoryList.stream().forEach(SupplierRankingRepository::calculateRelativeCloseness);

        return factoryList.stream().sorted(Comparator
                .comparing(Factory::getRelativeCloseness, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}
