/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.dao.impl;

import com.cursos.biblioinventory.config.DatabaseConnection;
import com.cursos.biblioinventory.dao.PrestamoDAO;
import com.cursos.biblioinventory.model.Book;
import com.cursos.biblioinventory.model.LibraryItem;
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
public class PrestamoDAOImpl implements PrestamoDAO{
    private final Connection conn;

    public PrestamoDAOImpl() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public void registrarPrestamo(int userId, int libroId) throws SQLException {
        String sqlPrestamo = "INSERT INTO prestamos(fecha_Prestamo, id_usuario, id_libro) values (?, ?, ?)";
        String sqlUpdateLibro = "UPDATE libros SET is_available = 0 WHERE id = ?";
        
        try(PreparedStatement pstmt1 = conn.prepareStatement(sqlPrestamo)){
            pstmt1.setString(1, LocalDate.now().toString());
            pstmt1.setInt(2, userId);
            pstmt1.setInt(3, libroId);
            pstmt1.executeUpdate();
        }
        
        try(PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdateLibro)){
            pstmt2.setInt(1, libroId);
            pstmt2.executeUpdate();
        }
        System.out.println("Prestamo resgistrado con exito.");
    }

    @Override
    public boolean devolverLibro(int libroId, int usuarioId) throws SQLException {
        String sqlDevolucion = "DELETE FROM prestamos WHERE id_libro = ? AND id_usuario = ?";
        String sqlUpdateLibro = "UPDATE libros SET is_available = 1 WHERE id = ?";
        
        try(PreparedStatement pstmt1 = conn.prepareStatement(sqlDevolucion)){
            pstmt1.setInt(1, libroId);
            pstmt1.setInt(2, usuarioId);
            pstmt1.executeUpdate();
        }
        
        try(PreparedStatement pstmt2 = conn.prepareStatement(sqlUpdateLibro)){
            pstmt2.setInt(1, libroId);
            pstmt2.executeUpdate();
        }
        System.out.println("Devolución resgistrado con exito.");
        return true;
    }

    @Override
    public List<LibraryItem> obtenerLibrosPrestados(int userId) throws SQLException {
        List<LibraryItem> librosPrestados = new ArrayList<>();
        String sql = "SELECT l.* FROM libros l JOIN prestamos p ON l.id = p.id_libro WHERE p.id_usuario = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {                
                Book libro = new Book(rs.getString("autor"), rs.getInt("isbn"), rs.getInt("id"), rs.getString("titulo"), LocalDate.parse(rs.getString("ano")), rs.getBoolean("is_available"));
                librosPrestados.add(libro);
            }
        } catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return librosPrestados;
    }
    
}
