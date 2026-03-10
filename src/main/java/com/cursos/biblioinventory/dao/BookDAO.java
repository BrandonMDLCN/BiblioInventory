/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cursos.biblioinventory.dao;

import com.cursos.biblioinventory.model.Book;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author brandon.delacruz
 */
public interface BookDAO {
    void save(Book book) throws SQLException;
    Book findById(int id) throws SQLException;
    List<Book> findAll() throws SQLException;
    void update(Book book) throws SQLException;
    void delete(String id) throws SQLException;
}
