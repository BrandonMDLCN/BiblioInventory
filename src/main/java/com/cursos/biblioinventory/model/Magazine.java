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
public class Magazine extends LibraryItem{
    private String author;
    private int isbn;
    private int issueNumber;
    
    public Magazine(String author, int isbn, int issueNumber, int id, String titulo, LocalDate ano) {
        super(id, titulo, ano);
        this.author = author;
        this.isbn = isbn;
        this.issueNumber = issueNumber;
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

    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }  
    

    @Override
    public void displayDetails() {
        System.out.println("id: "+getId()+" Titulo: "+getTitulo() + " Author: " + getAuthor() + " Año: " + getAno() + " ISBN: " + getIsbn() + " Issue Number: " + getIssueNumber());
    }

    @Override
    public void checkOut() {
        setIsAvailable(false);
    }

    @Override
    public void returnItem() {
        setIsAvailable(true);
    }

    @Override
    public String toJSON() {
        return "{" +
                "\"id\":\""+getId()+"\","+
                "\"titulo\":\""+getTitulo()+"\","+
                "\"ano\":\""+getAno()+"\","+
                "\"isAvailable\":"+isIsAvailable()+","+
                "\"author\":\""+getAuthor()+"\","+
                "\"isbn\":"+getIsbn()+
                "\"issueNumber\":"+getIssueNumber()+
                "}";
    }
}
