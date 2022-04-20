package topsis.models;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class Factory {
    private String factoryName;
    private double acceptanceQualityLevel;
    private double avgResponseTime;
    private double sampleAcceptanceRate;
    private double samplingTime;
    private double certificationCount;
    private double averageOfParameters;
    private double averageOfParametersWithWeightage;
    private double relativeCloseness;
}
