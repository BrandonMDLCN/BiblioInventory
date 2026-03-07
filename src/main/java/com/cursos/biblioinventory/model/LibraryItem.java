/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.model;

import java.time.LocalDate;

/**
 *
 * @author brandon.delacruz
 */
public abstract class LibraryItem {
    private String id;
    private String titulo;
    private LocalDate ano;
    private boolean isAvailable;

    protected LibraryItem(String id, String titulo, LocalDate ano) {
        this.id = id;
        this.titulo = titulo;
        this.ano = ano;
        this.isAvailable = true;
    }
    
    public abstract void displayDetails();
    
    public abstract void checkOut();
    
    public abstract void returnItem();
    
    public abstract String toJSON();

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the año
     */
    public LocalDate getAno() {
        return ano;
    }

    /**
     * @param ano the año to set
     */
    public void setAno(LocalDate ano) {
        this.ano = ano;
    }

    /**
     * @return the isAvailable
     */
    public boolean isIsAvailable() {
        return isAvailable;
    }

    /**
     * @param isAvailable the isAvailable to set
     */
    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }    
}
