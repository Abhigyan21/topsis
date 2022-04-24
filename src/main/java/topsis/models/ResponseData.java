package topsis.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseData {
    private String factoryName;
    private Double relativeCloseness;
}
