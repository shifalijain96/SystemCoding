package com.dunzo.coffeemachine;

import com.dunzo.coffeemachine.service.CoffeeMachineService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Objects;


public class CoffeeMachineServiceTests {
    CoffeeMachineService coffeeMachineService;

    //Test with 3 outlets json
    @Test
    public void testGivenInputJson() throws Exception {
        final String filePath = "input.json";
        File file = new File(Objects.requireNonNull(CoffeeMachineService.class.getClassLoader().getResource(filePath)).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachineService = CoffeeMachineService.getInstance(jsonInput);
        coffeeMachineService.process();
        Assertions.assertEquals(4, coffeeMachineService.coffeeMachineConfig.getMachine().getBeverages().size());
        coffeeMachineService.stop();
    }

    // coffee machine json null
    @Test
    public void testNullConfig() throws Exception {
        final String filePath = "input2.json";
        File file = new File(Objects.requireNonNull(CoffeeMachineService.class.getClassLoader().getResource(filePath)).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachineService = CoffeeMachineService.getInstance(jsonInput);
        coffeeMachineService.process();
        Assertions.assertNull(coffeeMachineService.coffeeMachineConfig.getMachine());
        coffeeMachineService.stop();
    }

    // no inventory
    @Test
    public void testNullInventory() throws Exception{
        final String filePath = "input3.json";
        File file = new File(Objects.requireNonNull(CoffeeMachineService.class.getClassLoader().getResource(filePath)).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachineService = CoffeeMachineService.getInstance(jsonInput);
        coffeeMachineService.process();
        Assertions.assertEquals(4, coffeeMachineService.coffeeMachineConfig.getMachine().getBeverages().size());
        coffeeMachineService.stop();
    }

    // outlets are lesser then number of beverages to be prepared with one no ingredient drink
    @Test
    public void testInput4Json() throws Exception{
        final String filePath = "input4.json";
        File file = new File(Objects.requireNonNull(CoffeeMachineService.class.getClassLoader().getResource(filePath)).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachineService = CoffeeMachineService.getInstance(jsonInput);
        coffeeMachineService.process();
        Assertions.assertEquals(9, coffeeMachineService.coffeeMachineConfig.getMachine().getBeverages().size());
        coffeeMachineService.stop();
    }

    // test no beverages provided
    @Test
    public void testNoBeverages() throws Exception{
        CoffeeMachineService coffeeMachineService;
        final String filePath = "input5.json";
        File file = new File(Objects.requireNonNull(CoffeeMachineService.class.getClassLoader().getResource(filePath)).getFile());
        String jsonInput = FileUtils.readFileToString(file, "UTF-8");
        coffeeMachineService = CoffeeMachineService.getInstance(jsonInput);
        coffeeMachineService.process();
        Assertions.assertEquals(0, coffeeMachineService.coffeeMachineConfig.getMachine().getBeverages().size());
        coffeeMachineService.stop();
    }

}
