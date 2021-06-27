package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.model.Beverage;
import com.dunzo.coffeemachine.model.CoffeeMachineConfig;
import com.dunzo.coffeemachine.util.RequestBodyValidator;
import com.dunzo.coffeemachine.util.TransformUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Description:
 *  This service supports preparation of concurrent beverages in case a coffee machine has multiple outlets.
 *  It takes care that no two beverages should try to get a same ingredient at the same time.
 *
 * Assumptions:
 *  Input JSON is correct and validated. I have added few javax validations as well but more could have added here. Considered such validations to be out of scope for this assignment.
 *  All the beverage names have been considered to be unique. For now, if multiple beverages with same name is provided,
 *  the last unique beverage name would be considered. Considered this to be out of scope for this assignment.
 *
 * Approach
 *   Used executor service to submit concurrent beverage requests.
 *   Used singleton design pattern and synchronized methods to make methods thread safe.
 *   Added some javax validations as well to validate the coffee machine json. Some more validations definitely could be added but considered them to be out of scope
 *   It also queues up all the requests and then tries to prepare beverages. Thread safety has been prioritized here.
 *   Have also exposed a method in inventory through which we can add more items to the inventory.
 */

@Slf4j
public class CoffeeMachineService {
    private int outletCount;
    private ThreadPoolExecutor executor;
    private final int MAX_QUEUED_REQUEST = 50;
    private static CoffeeMachineService coffeeMachineService;
    public CoffeeMachineConfig coffeeMachineConfig;
    private InventoryService inventoryService;

    public static CoffeeMachineService getInstance(String json){
        if(Objects.isNull(coffeeMachineService)) {
            coffeeMachineService  = new CoffeeMachineService(json);
            return coffeeMachineService;
        }
        return coffeeMachineService;
    }

    private CoffeeMachineService(String coffeeMachineJson){
        log.info("Processing json");
        coffeeMachineConfig = TransformUtil.fromJson(coffeeMachineJson,CoffeeMachineConfig.class);
        if(validateCoffeeMachineConfig(coffeeMachineConfig)) {
            if (Objects.nonNull(coffeeMachineConfig)) {
                int count = coffeeMachineConfig.getMachine().getOutlets().getCount();
                log.info("Outlet count {}", count);
                executor = new ThreadPoolExecutor(count, count, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_QUEUED_REQUEST));
            }
        }
    }

    public void process(){
        if(Objects.nonNull(executor) && Objects.nonNull(coffeeMachineConfig)){
            fillInventory();
            Map<String, Map<String,Integer>> beverages = coffeeMachineConfig.getMachine().getBeverages();
            for(String key: beverages.keySet()) {
                Beverage beverage = new Beverage(key, beverages.get(key));
                coffeeMachineService.submitBeverageRequest(beverage);
            }
        }

    }

    private boolean validateCoffeeMachineConfig(CoffeeMachineConfig coffeeMachineConfig){
        RequestBodyValidator<CoffeeMachineConfig> requestBodyValidator = new RequestBodyValidator<>();
        Map<String, List<String>> errors = requestBodyValidator.validateRequestBody(coffeeMachineConfig);
        if(errors.size() > 0){
            log.error("Validation for coffee machine json failed. errors are {}",errors);
            return false;
        }
        return true;
    }

    private void fillInventory(){
        this.inventoryService = InventoryService.getInstance();
        if(Objects.nonNull(coffeeMachineConfig) && Objects.nonNull(coffeeMachineConfig.getMachine()))
        this.inventoryService.addItemsToInventory(coffeeMachineConfig.getMachine().getInventoryMap());
    }

    private void submitBeverageRequest(Beverage beverage){
        BeverageTask beverageTask = new BeverageTask(beverage);
        executor.submit(beverageTask);
    }

    public void stop(){
        if(Objects.nonNull(executor)) {
            executor.shutdown();
        }
        if(Objects.nonNull(inventoryService))
        this.inventoryService.clearInventory();
        coffeeMachineService = null;
    }

}
