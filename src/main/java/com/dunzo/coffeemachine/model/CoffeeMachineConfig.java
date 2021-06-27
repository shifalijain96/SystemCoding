package com.dunzo.coffeemachine.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CoffeeMachineConfig {
    @NotNull
    private Machine machine;
}
