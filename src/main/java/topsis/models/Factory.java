package topsis.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Factory {
    private String factoryName;
    private Double acceptanceQualityLevel;
    private Double avgResponseTime;
    private Double sampleAcceptanceRate;
    private Double samplingTime;
    private Double certificationCount;
    private Double averageOfParameters;
    private Double averageOfParametersWithWeightage;
}
