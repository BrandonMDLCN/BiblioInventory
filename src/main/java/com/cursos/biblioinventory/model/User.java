/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brandon.delacruz
 */
public class User {
    private Integer userId;
    private String nombre;
    private final List<LibraryItem> borrowedItems;

    public User(Integer userId, String nombre) {
        this.userId = userId;
        this.nombre = nombre;
        this.borrowedItems = new ArrayList<>();
    }
    
    public User(String nombre) {
        this.nombre = nombre;
        this.borrowedItems = new ArrayList<>();
    }
    
    public User(Integer userId, String nombre, List<LibraryItem> borrowedItems) {
        this.userId = userId;
        this.nombre = nombre;
        this.borrowedItems = borrowedItems;
    }
    
    public User(String nombre, List<LibraryItem> borrowedItems) {
        this.nombre = nombre;
        this.borrowedItems = borrowedItems;
    }
    
    public void addLoan(LibraryItem item){
            borrowedItems.add(item);   
    }
    
    public void returnLibraryItemById(Integer id){
        LibraryItem itemEncontrado = borrowedItems.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
        if(itemEncontrado != null){
            borrowedItems.remove(itemEncontrado);
            System.out.println("Devolución exitosa.");
        }else{
            System.out.println("No se encontró el ítem con ese ID en tus préstamos.");
        }
    }
    
    public String toJSON(){
        return "{"+
                "\"userId\":"+getUserId()+","+
                "\"nombre\":\""+getNombre()+"\","+
                "\"borrowedItems\":"+saveBooksToFile()+
                "}";
    }
    
    public String saveBooksToFile(){
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for(LibraryItem item : borrowedItems){
            jsonBuilder.append(item.toJSON()).append(",\n");
        }
        
        if(!borrowedItems.isEmpty()){
            jsonBuilder.setLength(jsonBuilder.length()-2);
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
    
    public void showBooksBorrowed(){
        if(!borrowedItems.isEmpty()){
            borrowedItems.forEach(b -> System.out.println(b.getTitulo()));
        }
        System.out.println("El Usuario no cuenta con libros preestados.");
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void displayDetails() {
        String librosDeUsuario = borrowedItems.stream().map(LibraryItem::getTitulo).collect(java.util.stream.Collectors.joining(", "));
        System.out.println("id: " +getUserId()+ " Nombre: " + getNombre() + " Libros Prestados: [" + librosDeUsuario + "]");
    }
}
