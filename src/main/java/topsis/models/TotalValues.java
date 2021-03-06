package topsis.models;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TotalValues {
    private Double totalAcceptanceQualityLevel;
    private Double sqrtAcceptanceQualityLevel;
    private Double totalAvgResponseTime;
    private Double sqrtAvgResponseTime;
    private Double totalSampleAcceptanceRate;
    private Double sqrtSampleAcceptanceRate;
    private Double totalSamplingTime;
    private Double sqrtSamplingTime;
    private Double totalCertificationCount;
    private Double sqrtCertificationCount;
}
