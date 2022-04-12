package topsis.models;

import lombok.Data;

@Data
public class SolutionValues {
    private Double acceptanceQualityLevelIdealSolution;
    private Double acceptanceQualityLevelNegativeSolution;
    private Double avgResponseTimeIdealSolution;
    private Double avgResponseTimeNegativeSolution;
    private Double sampleAcceptanceRateIdealSolution;
    private Double sampleAcceptanceRateNegativeSolution;
    private Double samplingTimeIdealSolution;
    private Double samplingTimeNegativeSolution;
    private Double certificationCountIdealSolution;
    private Double certificationCountNegativeSolution;
}
