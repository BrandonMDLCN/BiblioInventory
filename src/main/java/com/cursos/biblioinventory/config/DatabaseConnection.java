/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author brandon.delacruz
 */
public class DatabaseConnection {
    //URL de conexion "jdbc:sqlite" es el protocolo seguido del nombre del archivo
    private static final String URL = "jdbc:sqlite:C:\\Users\\brandon.delacruz\\Downloads\\sql\\library.db";
    private static Connection connection = null;
    
    private DatabaseConnection(){}
    
    public static Connection getConnection() throws SQLException{
        if(connection == null || connection.isClosed()){
            try{
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
            } catch(ClassNotFoundException e){
                throw new SQLException("Driver SQLite no encontrado",e);
            }
        }
        return connection;
    }
}
