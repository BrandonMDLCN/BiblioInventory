/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.dao.impl;

import com.cursos.biblioinventory.config.DatabaseConnection;
import com.cursos.biblioinventory.dao.BookDAO;
import com.cursos.biblioinventory.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brandon.delacruz
 */
public class BookDAOImpl implements BookDAO{
    private final Connection conn;
    
    public BookDAOImpl() throws SQLException{
        this.conn = DatabaseConnection.getConnection();
        
        // Verificación de seguridad (Opcional pero recomendada)
        if (this.conn == null) {
            throw new SQLException("No se pudo establecer la conexión a la base de datos.");
        }
    }
    
    @Override
    public void save(Book book) throws SQLException {
        String sql = "INSERT INTO libros (titulo, autor, ano, is_available, isbn) values (?, ?, ?, ?, ?)";
        
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, book.getTitulo());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getAno().toString());
            pstmt.setBoolean(4, book.isIsAvailable());
            pstmt.setInt(5, book.getIsbn());
            
            pstmt.executeUpdate();
        }
    }

    @Override
    public Book findById(int id) throws SQLException {
        Book book = null;
        String sql = "SELECT * FROM libros WHERE id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                book = new Book(rs.getString("autor"), rs.getInt("isbn"), rs.getInt("id"), rs.getString("titulo"), LocalDate.parse(rs.getString("ano")), rs.getBoolean("is_available"));
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        List<Book> todosLosLibros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                Book book = new Book(rs.getString("autor"), rs.getInt("isbn"), rs.getInt("id"), rs.getString("titulo"), LocalDate.parse(rs.getString("ano")), rs.getBoolean("is_available"));
                todosLosLibros.add(book);
            }
        }
        return todosLosLibros;
    }

    @Override
    public void update(Book book) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
