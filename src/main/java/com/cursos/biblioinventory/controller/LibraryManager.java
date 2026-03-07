/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cursos.biblioinventory.controller;

import com.cursos.biblioinventory.exceptions.ItemNotFoundException;
import com.cursos.biblioinventory.exceptions.UserNotFoundException;
import com.cursos.biblioinventory.model.Book;
import com.cursos.biblioinventory.model.LibraryItem;
import com.cursos.biblioinventory.model.User;
import com.cursos.biblioinventory.utils.VerificarOCrearArchivo;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
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
    private static final String HOME = System.getProperty("user.home");
    private static final String RUTA_ARCHIVO_BOOKS = HOME + File.separator + "books.json";
    private static final String RUTA_ARCHIVO_USERS = HOME + File.separator + "users.json";
    
    private final Map<String, LibraryItem> inventory;
    private final Map<String, User> usuarios;
    private final Deque<ActionRecord> history;
    
    
    public LibraryManager(){
        this.inventory = new HashMap<>();
        this.usuarios = new HashMap<>();
        this.history = new ArrayDeque<>();
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
    
    public void undoLastAction(){
        if(history.isEmpty()){
            System.out.println("Nada que deshacer");
            return;
        }
        
        ActionRecord last = history.pop();
        
        switch (last.getType()){
            case LOAN:
                processDevolution(last.getUserId(), last.getItemId());
                break;
            case RETURN:
                processLoan(last.getUserId(), last.getItemId());
                break;
            default: break;
        }
    }
    
    public void addActionRecord(ActionRecord.ActionType type, String itemId, String userId){
        history.push(new ActionRecord(type, itemId, userId));
    }
    
    public void guardarAJSONItemUsuario(LibraryManager manager){
        try {
            manager.saveBooksToFile();
            manager.saveUsersToFile();
        } catch (IOException ex) {
            System.getLogger(LibraryManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public void saveBooksToFile() throws IOException{
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for(LibraryItem item : inventory.values()){
            jsonBuilder.append(item.toJSON()).append(",\n");
        }
        
        if(!inventory.isEmpty()){
            jsonBuilder.setLength(jsonBuilder.length()-2);
        }
        jsonBuilder.append("]");
        
        VerificarOCrearArchivo.buscarOCrear(RUTA_ARCHIVO_BOOKS);
        try (FileWriter json = new FileWriter(RUTA_ARCHIVO_BOOKS)){
            json.write(jsonBuilder.toString());
        } catch (IOException e) {
            throw new IOException("Error al escribir archivo");
        }
    }
    
    public void uploadFileToBooks() throws IOException{
        File booksFile = new File(RUTA_ARCHIVO_BOOKS);
        String jsontext;
        try(Reader reader = new FileReader(booksFile)){
            jsontext = reader.readAllAsString();
        } catch(IOException e){
            throw new IOException("Error al leer archivo");
        }
        List<LibraryItem> libros = parseItems(jsontext);
        for (LibraryItem libro : libros) {
            this.inventory.put(libro.getId(), libro);
        }
    }
    
    public List<LibraryItem> parseItems(String jsonContent){
        List<LibraryItem> libros = new ArrayList<>();
        String content = jsonContent.trim();
        if(content.startsWith("[")){
            content = content.substring(1, content.length()-1);
        }
        //Divide por objetos (asumiento que teminan en "}"
        String[] objects = content.split("(?<=\\}),");
        for(String objStr : objects){
            String cleanObj = objStr.trim().replace("{", "").replace("}", "");
            String[] fields = cleanObj.split(",");
            
            String id= ""; 
            String title = "";
            String author = "";
            String ano="";
            boolean isAvailable = true;
            int isbn = 0;
            for(String field : fields){
                String[] keyValue = field.split(":");
                String key = keyValue[0].replace("\"", "").trim();
                String value = keyValue[1].replace("\"", "").trim();
                
                switch (key) {
                    case "id": id = value; break;
                    case "titulo": title = value; break;
                    case "ano": ano = value; break;
                    case "author":author = value; break;
                    case "isAvailable": isAvailable = Boolean.parseBoolean(value); break; 
                    case "isbn": isbn = Integer.parseInt(value); break;
                    default: break;
                }
            }
            Book book = new Book(author, isbn, id, title, LocalDate.parse(ano));
            book.setIsAvailable(isAvailable);
            libros.add(book);
        }
        return libros;
    }
    
    public void uploadFileToUsers() throws IOException{
        File booksFile = new File(RUTA_ARCHIVO_USERS);
        String jsontext;
        try(Reader reader = new FileReader(booksFile)){
            jsontext = reader.readAllAsString();
        } catch(IOException e){
            throw new IOException("Error al leer archivo");
        }
        parseUsers(jsontext);
    }
    
    public void parseUsers(String jsonContent){
        String content = jsonContent.trim();
        if(content.startsWith("[")){
            content = content.substring(1, content.length()-1);
        }
        //Divide por objetos (asumiento que teminan en "}"
        String[] objects = content.split("(?<=\\}),");
        for(String objStr : objects){
            String cleanObj = objStr.trim().replace("{", "").replace("}", "");
              String[] fields = cleanObj.split(",(?=[^\\]]*(?:\\[|$))");
            
            String userId= "";
            String nombre = "";
            List<LibraryItem> borrowedItems = new ArrayList<>();
            for(String field : fields){
                String[] keyValue = field.split(":",2);
                if (keyValue.length < 2) continue;
                String key = keyValue[0].replace("\"", "").trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "userId": userId = value.replace("\"", ""); break;
                    case "nombre": nombre = value.replace("\"", ""); break;
                    case "borrowedItems": 
                        if(!value.equals("[]")){
                            borrowedItems = parseItems(value);
                        }
                        break;
                    default: break;
                }
            }
            this.usuarios.put(userId, new User(userId, nombre, borrowedItems));
        }
    }
    
    public void saveUsersToFile() throws IOException{
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[");
        for(User item : usuarios.values()){
            jsonBuilder.append(item.toJSON()).append(",\n");
        }
        
        if(!usuarios.isEmpty()){
            jsonBuilder.setLength(jsonBuilder.length()-2);
        }
        jsonBuilder.append("]");
        
        VerificarOCrearArchivo.buscarOCrear(RUTA_ARCHIVO_USERS);
        try (FileWriter json = new FileWriter(RUTA_ARCHIVO_USERS)){
            json.write(jsonBuilder.toString());
        } catch (IOException e) {
            throw new IOException("Error al escribir archivo");
        }
    }
    
    public void addUser(User usuario){
        usuarios.put(usuario.getUserId(), usuario);
    }
    
    public User findUser(String id) throws UserNotFoundException{
        User usuario = usuarios.get(id);
        if(usuario == null){
            throw new UserNotFoundException("El Usuario con ID: " + id + " no se encuentra registrado.");
        }
        return usuario;
    }
    
    public void addItem(LibraryItem item){
        inventory.put(item.getId(), item);
    }
    
    public LibraryItem findItem(String id) throws ItemNotFoundException{
        LibraryItem item = inventory.get(id);
        if(item == null){
            throw new ItemNotFoundException("El item con ID: " + id + " no existe en la biblioteca.");
        }
        return item;
    }
    
    public void listAllItems(){
        System.out.println("\nTodos los Libros:");
        for(LibraryItem item : inventory.values()){
            item.displayDetails();
        }
    }
    
    public void listAllUsers(){
        System.out.println("\nTodos los Usuarios:");
        for(User usuario : usuarios.values()){
            usuario.displayDetails();
        }
    }
    
    public boolean processLoan(String userId, String itemId){
        if(!inventory.containsKey(itemId)){
            System.out.println("Libro no existe");
            return false;
        }
        if(!usuarios.containsKey(userId)){
            System.err.println(USUARIO_NO_EXISTE);
            return false;
        }
                
        LibraryItem libroAPrestar = inventory.get(itemId);
        User usuarioPrestamo = usuarios.get(userId);
        
        if(libroAPrestar.isIsAvailable()){
            System.out.println("Prestando libro: " + libroAPrestar.getTitulo() + " a: " + usuarioPrestamo.getNombre());
            libroAPrestar.checkOut();
            usuarioPrestamo.addLoan(libroAPrestar); 
            return true;
        }else{
            System.out.println("El ítem no está disponible.");
            return false;
        }
    }
    
    public boolean processDevolution(String userId, String itemId){
        if(!inventory.containsKey(itemId)){
            System.out.println("Libro no existe");
            return false;
        }
        if(!usuarios.containsKey(userId)){
            System.err.println(USUARIO_NO_EXISTE);
            return false;
        }
                
        LibraryItem libroADevolver = inventory.get(itemId);
        User usuarioDevolucion = usuarios.get(userId);
        
        if(!libroADevolver.isIsAvailable()){
            System.out.println("Devolviendo libro: " + libroADevolver.getTitulo() + " de: " + usuarioDevolucion.getNombre());
            libroADevolver.returnItem();
            usuarioDevolucion.returnLibraryItemById(itemId);   
            return true;
        }else{
            System.out.println("El ítem no está disponible.");
            return false;
        }
    }
    
    public void showUsersBooks(String userId){
        if(!usuarios.containsKey(userId)){
            System.err.println(USUARIO_NO_EXISTE);
            return;
        }
        
        User usuarioAMostrar = usuarios.get(userId);
        usuarioAMostrar.showBooksBorrowed();
    }
    
    /**
     * Metodo para buscar entre los items existentes y retorna el consecutivo siguiente.
     * @return the next consecutive ID for Items
     */
    public int consecutivoItemId() {
        int nextID = 1;
        if(!inventory.isEmpty()){
            nextID = inventory.values().stream().mapToInt(i -> {
                try{
                    return  Integer.parseInt(i.getId().replaceAll("[^0-9]", ""));
                } catch(NumberFormatException e){
                    return 0;
                }
            })
            .max().getAsInt()+1;
        }
        return nextID;
    }
    
    /**
     * @return the next consecutive ID for Users
     */
    public int consecutivoUserId() {
        int nextID = 1;
        if(!usuarios.isEmpty()){
            nextID = usuarios.values().stream().mapToInt(i -> {
                try{
                    return  Integer.parseInt(i.getUserId().replaceAll("[^0-9]", ""));
                } catch(NumberFormatException e){
                    return 0;
                }
            })
            .max().getAsInt()+1;
        }
        return nextID;
    }
}
