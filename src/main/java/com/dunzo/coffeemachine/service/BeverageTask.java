package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.model.Beverage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeverageTask implements Runnable{
    private Beverage beverage;

    BeverageTask(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public void run() {
        if(InventoryService.getInstance().checkInventoryAndUpdate(beverage)){
            log.info("{} is prepared",beverage.getName());
        }
    }
}
