package topsis.models;

import lombok.Data;

@Data
public class ParameterWeightage {
    private Double acceptanceQualityLevelWeightage;
    private Double avgResponseTimeWeightage;
    private Double sampleAcceptanceRateWeightage;
    private Double samplingTimeWeightage;
    private Double certificationCountWeightage;
}
