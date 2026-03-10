/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.model;

/**
 *
 * @author brandon.delacruz
 */
public class ActionRecord {
    public enum ActionType{ADD, REMOVE, LOAN, RETURN}
    
    private final ActionType type;
    private final Integer itemId;
    private final Integer userId;
    private final long timestamp;
    
    public ActionRecord(ActionType type, Integer itemId, Integer userId){
        this.type = type;
        this.itemId = itemId;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }

    public ActionType getType() {
        return type;
    }

    public Integer getItemId() {
        return itemId;
    }

    public Integer getUserId() {
        return userId;
    }
    
    @Override
    public String toString(){
        if(userId == null){
            return String.format("[%tT] Acción: %s | Item ID: %s", timestamp, type, itemId);
        }
        return String.format("[%tT] Acción: %s | Item ID: %s | User ID: %s", timestamp, type, itemId, userId);
    }
}
