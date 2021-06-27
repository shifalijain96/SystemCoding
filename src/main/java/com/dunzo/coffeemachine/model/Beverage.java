package com.dunzo.coffeemachine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beverage {
    @Size(min=3, max=32)
    private String name;

    @Size(min = 1)
    @NotEmpty
    private Map<String,Integer> ingredientsMap;
}
