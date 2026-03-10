/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cursos.biblioinventory.dao;

import com.cursos.biblioinventory.model.LibraryItem;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author brandon.delacruz
 */
public interface PrestamoDAO {
    void registrarPrestamo(int userId, int libroId) throws SQLException;
    boolean devolverLibro(int libroId, int usuarioId) throws SQLException;
    List<LibraryItem> obtenerLibrosPrestados(int userId) throws SQLException;
}
