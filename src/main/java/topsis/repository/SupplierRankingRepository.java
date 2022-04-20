package topsis.repository;

import topsis.models.Factory;
import topsis.models.ParameterWeightage;
import topsis.models.SolutionValues;
import topsis.models.TotalValues;

import java.util.List;

public class SupplierRankingRepository {

    public static void squareParameterWithSelfValue(Factory factory) {
        factory.setAcceptanceQualityLevel(Math.pow(factory.getAcceptanceQualityLevel(), 2));
        factory.setAvgResponseTime(Math.pow(factory.getAvgResponseTime(), 2));
        factory.setSampleAcceptanceRate(Math.pow(factory.getSampleAcceptanceRate(), 2));
        factory.setSamplingTime(Math.pow(factory.getSamplingTime(), 2));
        factory.setCertificationCount(Math.pow(factory.getCertificationCount(), 2));
    }

    public static void divideParameterWithSqrt(Factory factory, TotalValues totalValues) {
        factory.setAcceptanceQualityLevel(factory.getAcceptanceQualityLevel() / totalValues.getSqrtAcceptanceQualityLevel());
        factory.setAvgResponseTime(factory.getAvgResponseTime() / totalValues.getSqrtAvgResponseTime());
        factory.setSampleAcceptanceRate(factory.getSampleAcceptanceRate() / totalValues.getSqrtSampleAcceptanceRate());
        factory.setSamplingTime(factory.getSamplingTime() / totalValues.getSqrtSamplingTime());
        factory.setCertificationCount(factory.getCertificationCount() / totalValues.getSqrtCertificationCount());
    }

    public static void divideParameterWithWeightage(Factory factory, ParameterWeightage parameterWeightage) {
        factory.setAcceptanceQualityLevel(factory.getAcceptanceQualityLevel() * parameterWeightage.getAcceptanceQualityLevelWeightage());
        factory.setAvgResponseTime(factory.getAvgResponseTime() * parameterWeightage.getAvgResponseTimeWeightage());
        factory.setSampleAcceptanceRate(factory.getSampleAcceptanceRate() * parameterWeightage.getSampleAcceptanceRateWeightage());
        factory.setSamplingTime(factory.getSamplingTime() * parameterWeightage.getSamplingTimeWeightage());
        factory.setCertificationCount(factory.getCertificationCount() * parameterWeightage.getCertificationCountWeightage());
    }

    public static void subtractParameterFromIdealSolutionAndSquare(Factory factory, SolutionValues solutionValues) {
        factory.setAcceptanceQualityLevel(Math.pow(factory.getAcceptanceQualityLevel() - solutionValues.getAcceptanceQualityLevelIdealSolution(), 2));
        factory.setAvgResponseTime(Math.pow(factory.getAvgResponseTime() - solutionValues.getAvgResponseTimeIdealSolution(), 2));
        factory.setSampleAcceptanceRate(Math.pow(factory.getSampleAcceptanceRate() - solutionValues.getSampleAcceptanceRateIdealSolution(), 2));
        factory.setSamplingTime(Math.pow(factory.getSamplingTime() - solutionValues.getSamplingTimeIdealSolution(), 2));
        factory.setCertificationCount(Math.pow(factory.getCertificationCount() - solutionValues.getCertificationCountIdealSolution(), 2));
    }

    public static TotalValues generateTotalValues(List<Factory> factoryList) {
        TotalValues values = new TotalValues();

        values.setTotalAcceptanceQualityLevel(factoryList.stream().mapToDouble(Factory::getAcceptanceQualityLevel).sum());
        values.setSqrtAcceptanceQualityLevel(Math.sqrt(values.getTotalAcceptanceQualityLevel()));

        values.setTotalAvgResponseTime(factoryList.stream().mapToDouble(Factory::getAvgResponseTime).sum());
        values.setSqrtAvgResponseTime(Math.sqrt(values.getTotalAvgResponseTime()));

        values.setTotalSampleAcceptanceRate(factoryList.stream().mapToDouble(Factory::getSampleAcceptanceRate).sum());
        values.setSqrtSampleAcceptanceRate(Math.sqrt(values.getTotalSampleAcceptanceRate()));

        values.setTotalSamplingTime(factoryList.stream().mapToDouble(Factory::getSamplingTime).sum());
        values.setSqrtSamplingTime(Math.sqrt(values.getTotalSamplingTime()));

        values.setTotalCertificationCount(factoryList.stream().mapToDouble(Factory::getCertificationCount).sum());
        values.setSqrtCertificationCount(Math.sqrt(values.getTotalCertificationCount()));

        return values;
    }

    public static SolutionValues generateSolutionValues(List<Factory> factoryList) {
        SolutionValues values = new SolutionValues();

        values.setAcceptanceQualityLevelIdealSolution(factoryList.stream().mapToDouble(Factory::getAcceptanceQualityLevel).min().getAsDouble());
        values.setAcceptanceQualityLevelNegativeSolution(factoryList.stream().mapToDouble(Factory::getAcceptanceQualityLevel).max().getAsDouble());

        values.setAvgResponseTimeIdealSolution(factoryList.stream().mapToDouble(Factory::getAvgResponseTime).min().getAsDouble());
        values.setAvgResponseTimeNegativeSolution(factoryList.stream().mapToDouble(Factory::getAvgResponseTime).max().getAsDouble());

        values.setSampleAcceptanceRateIdealSolution(factoryList.stream().mapToDouble(Factory::getSampleAcceptanceRate).min().getAsDouble());
        values.setSampleAcceptanceRateNegativeSolution(factoryList.stream().mapToDouble(Factory::getSampleAcceptanceRate).max().getAsDouble());

        values.setSamplingTimeIdealSolution(factoryList.stream().mapToDouble(Factory::getSamplingTime).min().getAsDouble());
        values.setSamplingTimeNegativeSolution(factoryList.stream().mapToDouble(Factory::getSamplingTime).max().getAsDouble());

        values.setCertificationCountIdealSolution(factoryList.stream().mapToDouble(Factory::getCertificationCount).min().getAsDouble());
        values.setCertificationCountNegativeSolution(factoryList.stream().mapToDouble(Factory::getCertificationCount).max().getAsDouble());

        return values;
    }

    public static void calculateAverageIdealSolutionForFactoryParameters(Factory factory) {
        factory.setAverageOfParameters(Math.sqrt(
                factory.getAcceptanceQualityLevel()
                        + factory.getAvgResponseTime()
                        + factory.getSampleAcceptanceRate()
                        + factory.getSamplingTime()
                        + factory.getCertificationCount()));
    }

    public static void subtractParameterFromWeightageAndSquare(Factory factory, ParameterWeightage parameterWeightage) {
        factory.setAcceptanceQualityLevel(Math.pow(factory.getAcceptanceQualityLevel() - parameterWeightage.getAcceptanceQualityLevelWeightage(), 2));
        factory.setAvgResponseTime(Math.pow(factory.getAvgResponseTime() - parameterWeightage.getAvgResponseTimeWeightage(), 2));
        factory.setSampleAcceptanceRate(Math.pow(factory.getSampleAcceptanceRate() - parameterWeightage.getSampleAcceptanceRateWeightage(), 2));
        factory.setSamplingTime(Math.pow(factory.getSamplingTime() - parameterWeightage.getSamplingTimeWeightage(), 2));
        factory.setCertificationCount(Math.pow(factory.getCertificationCount() - parameterWeightage.getCertificationCountWeightage(), 2));
    }

    public static void calculateAverageNegativeIdealSolutionForFactoryParameters(Factory factory) {
        factory.setAverageOfParametersWithWeightage(Math.sqrt(
                factory.getAcceptanceQualityLevel()
                        + factory.getAvgResponseTime()
                        + factory.getSampleAcceptanceRate()
                        + factory.getSamplingTime()
                        + factory.getCertificationCount()));
    }

    public static void calculateRelativeCloseness(Factory factory) {
        factory.setRelativeCloseness(
                factory.getAverageOfParametersWithWeightage() /
                        (factory.getAverageOfParametersWithWeightage() + factory.getAverageOfParameters())
        );
    }
}
