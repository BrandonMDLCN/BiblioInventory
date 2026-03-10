/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.controller;

import com.cursos.biblioinventory.dao.BookDAO;
import com.cursos.biblioinventory.dao.PrestamoDAO;
import com.cursos.biblioinventory.dao.UserDAO;
import com.cursos.biblioinventory.dao.impl.BookDAOImpl;
import com.cursos.biblioinventory.dao.impl.PrestamoDAOImpl;
import com.cursos.biblioinventory.dao.impl.UserDAOImpl;
import com.cursos.biblioinventory.model.ActionRecord;
import com.cursos.biblioinventory.model.Book;
import com.cursos.biblioinventory.model.LibraryItem;
import com.cursos.biblioinventory.model.User;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author brandon.delacruz
 */
public class LibraryManager {
    private static final String USUARIO_NO_EXISTE = "Usuario no existe";
    
    private final Map<Integer, LibraryItem> inventory;
    private final BookDAO bookDao;
    private final UserDAO userDao;
    private final PrestamoDAO prestamoDao;
    private final Deque<ActionRecord> history;
    
    
    public LibraryManager() throws SQLException{
        this.inventory = new HashMap<>();
        this.history = new ArrayDeque<>();
        this.bookDao = new BookDAOImpl();
        this.userDao = new UserDAOImpl();
        this.prestamoDao = new PrestamoDAOImpl();
    }
    
    public void showHistoryRecords(){
        if(history.isEmpty()){
            System.out.println("Nada que deshacer");
            return;
        }
        for (ActionRecord actionRecord : history) {
            System.out.println(actionRecord.toString());
        }
    }
    
    public void undoLastAction() throws SQLException{
        if(history.isEmpty()){
            System.out.println("Nada que deshacer");
            return;
        }
        
        ActionRecord last = history.pop();
        
        switch (last.getType()){
            case LOAN:
                processDevolution(last.getItemId(), last.getUserId());
                break;
            case RETURN:
                processLoan(last.getUserId(), last.getItemId());
                break;
            default: break;
        }
    }
    
    public void addActionRecord(ActionRecord.ActionType type, Integer itemId, Integer userId){
        history.push(new ActionRecord(type, itemId, userId));
    }
    
    public void addItem(LibraryItem item){
        inventory.put(item.getId(), item);
    }
    
    public void addBook(Book book) throws SQLException{
        bookDao.save(book);
    }
    
    public void addUser(User user) throws SQLException{
        userDao.save(user);
    }
    
    public boolean findItem(int id){
        try{
            LibraryItem book = bookDao.findById(id);
            if(book != null){
                book.displayDetails();
                return true;
            }
            System.out.println("Libro no encontrado");
        }catch (SQLException ex) {
            System.getLogger(LibraryManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
    
    public boolean findUser(int id){
        try {
            User usuario = userDao.findByIdUser(id);
            if(usuario != null){
                usuario.displayDetails();
                return true;
            }
            System.err.println("Usuario no encontrado");
        } catch (SQLException ex) {
            System.getLogger(LibraryManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
    
    public void listAllItems(){
        System.out.println("\nTodos los Libros:");
        List<Book> books = new ArrayList<>();
        try {
            books = bookDao.findAll();
        } catch (SQLException ex) {
            System.getLogger(LibraryManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        for(LibraryItem item : books){
            item.displayDetails();
        }
    }
    
    public void listAllUsers(){
        System.out.println("\nTodos los Usuarios:");
        List<User> usuarios = new ArrayList<>();
        try {
            usuarios = userDao.findAllUsers();
        } catch (SQLException ex) {
            System.getLogger(LibraryManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        for(User usuario : usuarios){
            usuario.displayDetails();
        }
    }
    
    public boolean processLoan(int userId, int itemId) throws SQLException{
        Book libroAPrestar = bookDao.findById(itemId);
        User usuarioPrestamo = userDao.findByIdUser(userId);
        if(libroAPrestar == null){
            System.out.println("Libro no existe");
            return false;
        }
        if(usuarioPrestamo == null){
            System.err.println(USUARIO_NO_EXISTE);
            return false;
        }
        
        if(libroAPrestar.isIsAvailable()){
            System.out.println("Prestando libro: " + libroAPrestar.getTitulo() + " a: " + usuarioPrestamo.getNombre());
            prestamoDao.registrarPrestamo(userId, itemId);
            return true;
        }else{
            System.out.println("El ítem no está disponible.");
            return false;
        }
    }
    
    public boolean processDevolution(int itemId, int userId){
        try {
            Book libroADevolver = bookDao.findById(itemId);
            User usuarioDevolucion = userDao.findByIdUser(userId);
            if(libroADevolver == null){
                System.out.println("Libro no existe");
                return false;
            }
            if(usuarioDevolucion == null){
                System.err.println(USUARIO_NO_EXISTE);
                return false;
            }
            if(!libroADevolver.isIsAvailable()){
                System.out.println("Devolviendo libro: " + libroADevolver.getTitulo() + " de: " + usuarioDevolucion.getNombre());
                return prestamoDao.devolverLibro(itemId, userId);
            }
        } catch (SQLException ex) {
            System.getLogger(LibraryManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }
    
    public void showUsersBooks(Integer userId){
        try {
            if(userDao.findByIdUser(userId) == null){
                System.err.println(USUARIO_NO_EXISTE);
                return;
            }
            List<LibraryItem> librosPrestados = prestamoDao.obtenerLibrosPrestados(userId);
            librosPrestados.forEach(b -> System.out.println(b.getTitulo()));
        } catch (SQLException ex) {
            System.getLogger(LibraryManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }    
}
