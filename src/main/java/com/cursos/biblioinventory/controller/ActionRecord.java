/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.controller;

/**
 *
 * @author brandon.delacruz
 */
public class ActionRecord {
    public enum ActionType{ADD, REMOVE, LOAN, RETURN}
    
    private final ActionType type;
    private final String itemId;
    private final String userId;
    private final long timestamp;
    
    public ActionRecord(ActionType type, String itemId, String userId){
        this.type = type;
        this.itemId = itemId;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
    }

    public ActionType getType() {
        return type;
    }

    public String getItemId() {
        return itemId;
    }

    public String getUserId() {
        return userId;
    }
    
    @Override
    public String toString(){
        if(userId.equalsIgnoreCase("")){
            return String.format("[%tT] Acción: %s | Item ID: %s", timestamp, type, itemId);
        }
        return String.format("[%tT] Acción: %s | Item ID: %s | User ID: %s", timestamp, type, itemId, userId);
    }
}
