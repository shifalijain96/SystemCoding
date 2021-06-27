package com.dunzo.coffeemachine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
public class Machine {

    @Valid
    private Outlet outlets;

    @JsonProperty("total_items_quantity")
    @NotNull
    @Size(min = 1)
    private Map<String,Integer> inventoryMap;

    @JsonProperty("beverages")
    @Size(min = 1)
    private Map<String,Map<String,Integer>> beverages;
}
