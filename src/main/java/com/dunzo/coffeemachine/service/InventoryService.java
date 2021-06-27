package com.dunzo.coffeemachine.service;

import com.dunzo.coffeemachine.model.Beverage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class InventoryService {
    public Map<String,Integer> inventoryMap = new HashMap<>();
    private InventoryService(){};

    // To make sure Inventory service is singleton and thread safe.
    private static class InventoryServiceInstance{
        public static final InventoryService instance = new InventoryService();
    }

    public static InventoryService getInstance(){
        return InventoryServiceInstance.instance;
    }


    /**
     * Method to add/update items to the inventory
     * @param map inventory items map
     *
     */
    public void addItemsToInventory(Map<String,Integer> map){
        if(Objects.nonNull(map) && map.size()>0) {
            for (String key : map.keySet()) {
                int quantity = inventoryMap.getOrDefault(key, 0);
                inventoryMap.put(key, quantity + map.get(key));
            }
        }
        log.info("inventory map {}", inventoryMap);
    }


    /**
     * Check if beverage can be prepared, if no return false else update the inventory and return true
     * @param beverage
     * @return
     */
    public boolean checkInventoryAndUpdate(Beverage beverage){
        Map<String,Integer> reqdBeverageIngredients = beverage.getIngredientsMap();
        for(String item : reqdBeverageIngredients.keySet()){
            int existingQty = inventoryMap.getOrDefault(item,-1);
            if(existingQty == -1){
                log.error("{} cannot be prepared because {} is not available",beverage.getName(),item);
                return false;
            }
            if(reqdBeverageIngredients.get(item)>existingQty){
                log.error("{} cannot be prepared because {} is not sufficient",beverage.getName(),item);
                return false;
            }
        }
        updateInventory(beverage);
        return true;
    }

    /**
     * Method to update inventory if beverage can be prepared.
     * Made it synchronized so that inventoryMap is not updated by multiple threads at same time
     * @param beverage
     */
    private synchronized void updateInventory(Beverage beverage){
        Map<String,Integer> reqdBeverageIngredients = beverage.getIngredientsMap();
        for(String item : reqdBeverageIngredients.keySet()){
            int existingQty = inventoryMap.getOrDefault(item,0);
            int reqdQty = reqdBeverageIngredients.get(item);
            if(existingQty>0 && existingQty>=reqdQty){
                inventoryMap.put(item,existingQty-reqdQty);
            }
        }
    }

    /**
     * If items are present in inventory, this method allows to delete all inventory items and reset it.
     */
    public void clearInventory() {
        if(Objects.nonNull(inventoryMap))
        inventoryMap.clear();
    }
}
