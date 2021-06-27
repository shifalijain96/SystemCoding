package com.dunzo.coffeemachine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class Outlet {

    @JsonProperty("count_n")
    @Min(1)
    @Max(50)
    private Integer count;
}
