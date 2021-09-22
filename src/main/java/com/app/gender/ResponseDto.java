package com.app.gender;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ResponseDto {
    @JsonProperty private String designation;
    @JsonProperty private int male;
    @JsonProperty private int female;
    @JsonProperty private int inconclusive;
    @JsonProperty private double percentage;
}