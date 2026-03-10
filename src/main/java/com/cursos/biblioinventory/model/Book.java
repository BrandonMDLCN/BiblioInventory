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
public class Book extends LibraryItem{
    private String author;
    private int isbn;

   /*
    * Constructor para ingresar datos al sistema desde la BD
    */
    public Book(String author, int isbn, int id, String titulo, LocalDate ano, boolean isAvailable) {
        super(id, titulo, ano, isAvailable);
        this.author = author;
        this.isbn = isbn;
    }
    
   /*
    * Constructor para enviar datos a la BD
    */
    public Book(String author, int isbn, String titulo, LocalDate ano) {
        super(titulo, ano);
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public void displayDetails() {
        System.out.println("id: "+getId()+" Titulo: " + getTitulo() + " Autor: " + author + " Año: " + getAno() + " ISBN: "+getIsbn()+" Disponible: " + isIsAvailable());
    }
    
    @Override
    public String toJSON(){
        return "{" +
                "\"id\":"+getId()+","+
                "\"titulo\":\""+getTitulo()+"\","+
                "\"ano\":\""+getAno()+"\","+
                "\"isAvailable\":"+isIsAvailable()+","+
                "\"author\":\""+getAuthor()+"\","+
                "\"isbn\":"+getIsbn()+
                "}";
    }

    @Override
    public void checkOut() {
        setIsAvailable(false);
    }

    @Override
    public void returnItem() {
        setIsAvailable(true);
    }
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
}
