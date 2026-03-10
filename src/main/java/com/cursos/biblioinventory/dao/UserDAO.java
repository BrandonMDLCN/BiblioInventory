/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.cursos.biblioinventory.dao;

import com.cursos.biblioinventory.model.User;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author brandon.delacruz
 */
public interface UserDAO {
    void save(User user) throws SQLException;
    User findByIdUser(int id) throws SQLException;
    List<User> findAllUsers()throws SQLException;
    void update(User user) throws SQLException;
    void delete (String id) throws SQLException;
}
