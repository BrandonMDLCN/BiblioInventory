/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.dao.impl;

import com.cursos.biblioinventory.config.DatabaseConnection;
import com.cursos.biblioinventory.dao.PrestamoDAO;
import com.cursos.biblioinventory.dao.UserDAO;
import com.cursos.biblioinventory.model.LibraryItem;
import com.cursos.biblioinventory.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author brandon.delacruz
 */
public class UserDAOImpl implements UserDAO{
    private final Connection conn;

    public UserDAOImpl() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    @Override
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO Usuarios (nombre) values (?)";
        
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, user.getNombre());
            
            pstmt.executeUpdate();
        }
    }

    @Override
    public User findByIdUser(int id) throws SQLException {
        PrestamoDAO prestamoDao = new PrestamoDAOImpl();
        String sql = "SELECT * FROM Usuarios where id = ?";
        User user = null;
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            List<LibraryItem> libros = prestamoDao.obtenerLibrosPrestados(id);
            while(rs.next()){
                user = new User(rs.getInt("id"), rs.getString("nombre"), libros);
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() throws SQLException {
        PrestamoDAO prestamoDao = new PrestamoDAOImpl();
        List<User> usuarios = new ArrayList<>();
        User user;
        String sql = "SELECT * FROM Usuarios";
        
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                List<LibraryItem> libros = prestamoDao.obtenerLibrosPrestados(rs.getInt("id"));
                user = new User(rs.getInt("id"), rs.getString("nombre"), libros);
                usuarios.add(user);
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return usuarios;
    }

    @Override
    public void update(User user) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
