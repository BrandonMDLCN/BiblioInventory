package com.cursos.biblioinventory;

import com.cursos.biblioinventory.controller.ActionRecord;
import com.cursos.biblioinventory.controller.LibraryManager;
import com.cursos.biblioinventory.model.Book;
import com.cursos.biblioinventory.model.User;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author brandon.delacruz
 */
public class BiblioInventory {
    private static final String OPCION_INCORRECTO = "\n\nOpción Incorrecta favor de seleccionar una opción existente";
    private static final String SELECCIONAR_OPCION = "Seleccionar una opción:";
    private static final String REGRESAR_A_MENU = "\nRegresando a menu principal";
    
    public static void main(String[] args) {        
        LibraryManager manager = new LibraryManager();
        
        try {
            manager.uploadFileToBooks();
            manager.uploadFileToUsers();
        } catch (IOException ex) {
            System.getLogger(BiblioInventory.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        System.out.println("\nBienvenido Administrador de Biblioteca");
        
            
        int opcion = 1;
        do {
            
            System.out.println("\n/*----------Selecciona una opción relacionado:----------*/");
            System.out.println("1.- Libros (Añadir, Listar, Modificar)");
            System.out.println("2.- Usuarios (Añadir, Prestamos, Devoluciones)");
            System.out.println("3.- Historial (Mostrar Historial, Deshacer movimientos)");
            System.out.println("0.- Salir del sistema");
            
            Scanner scanner = new Scanner(System.in);
            String eleccion = scanner.nextLine();
            if(Character.isDigit(eleccion.charAt(0))){
                opcion = Integer.parseInt(eleccion);
            }
            
            switch (opcion) {
                case 1 -> opcionLibro(manager);
                case 2 -> opcionUsuario(manager);
                case 3 -> opcionHistorial(manager);
                case 0 -> System.out.println("\n\nSaliendo del Sistema de Administración, Bye...");
                default -> System.out.println(OPCION_INCORRECTO);
            }
            
        } while (opcion != 0);
    }
    
    public static void opcionLibro(LibraryManager manager){
        System.out.println("\n----------Sección Libros----------");
        System.out.println(SELECCIONAR_OPCION);
        System.out.println("1.- Añadir Libro");
        System.out.println("2.- Listar todos los libros");
        System.out.println("3.- Mostrar libros de un Usuario");
        System.out.println("0.- Regresar");
        
        Scanner scanner = new Scanner(System.in);
        String eleccion = scanner.nextLine();
        int opcion = 0;
        if(Character.isDigit(eleccion.charAt(0))){
            opcion = Integer.parseInt(eleccion);
        }
        switch (opcion) {
            case 1 -> anadirLibros(scanner, manager);
            case 2 -> {
                System.out.println("\nMostrando Todos los Libros");
                manager.listAllItems();
            }
            case 3 -> {
                System.out.println("\nMostrando Todos los Libros de un Usuario");
                System.out.println("Ingresa Id de Usuario a mostrar:");
                String idUser = scanner.nextLine();
                manager.showUsersBooks(idUser);
            }
            case 0 -> System.out.println(REGRESAR_A_MENU);
            default -> System.out.println(OPCION_INCORRECTO);
        }
    }
    
    public static void opcionUsuario(LibraryManager manager){
        System.out.println("\n----------Sección Usuario----------");
        System.out.println(SELECCIONAR_OPCION);
        System.out.println("1.- Registrar Usuario");
        System.out.println("2.- Mostrar todos los Usuarios");
        System.out.println("3.- Procesar un Prestamo");
        System.out.println("4.- Regresar un Libro");
        
        Scanner scanner = new Scanner(System.in);
        String eleccion = scanner.nextLine();
        int opcion = 0;
        if(Character.isDigit(eleccion.charAt(0))){
            opcion = Integer.parseInt(eleccion);
        }
        switch (opcion) {
            case 1 -> anadirUsuario(scanner, manager);
            case 2 -> {
                System.out.println("\nMostrando Todos los Libros");
                manager.listAllUsers();
            }
            case 3 -> {
                System.out.println("\nProcesando un Prestamo");
                System.out.println("Ingresa Id de Usuario a prestar:");
                String idUserr = scanner.nextLine();
                System.out.println("Ingresa Id de Libro a prestar:");
                String idLibro = scanner.nextLine();
                if(manager.processLoan(idUserr, idLibro)){
                    manager.addActionRecord(ActionRecord.ActionType.LOAN, idLibro, idUserr);
                    manager.guardarAJSONItemUsuario(manager);
                }                                
            }
            case 4 -> {
                System.out.println("\nRegresando Libro");
                System.out.println("Ingresa Id de Usuario a regresar:");
                String idUser = scanner.nextLine();
                System.out.println("Ingresa Id de Libro a regresar:");
                String idLibro = scanner.nextLine();
                if(manager.processDevolution(idUser, idLibro)){
                    manager.addActionRecord(ActionRecord.ActionType.RETURN, idLibro, idUser);
                    manager.guardarAJSONItemUsuario(manager);
                }                                
            }
            case 0 -> System.out.println(REGRESAR_A_MENU);
            default -> System.out.println(OPCION_INCORRECTO);
        }
    }
    
    public static void opcionHistorial(LibraryManager manager){
        System.out.println("\n----------Sección Historial----------");
        System.out.println(SELECCIONAR_OPCION);
        System.out.println("1.- Mostrar el historial de movimientos");
        System.out.println("2.- Deshacer ultimo movimiento");
        
        Scanner scanner = new Scanner(System.in);
        String eleccion = scanner.nextLine();
        int opcion = 0;
        if(Character.isDigit(eleccion.charAt(0))){
            opcion = Integer.parseInt(eleccion);
        }
        switch (opcion) {
            case 1 -> manager.showHistoryRecords();
            case 2 -> {
                System.out.println("\n Deshaciendo ultimo movimiento");
                manager.undoLastAction();
                manager.guardarAJSONItemUsuario(manager);
            }
            case 0 -> System.out.println(REGRESAR_A_MENU);
            default -> System.out.println(OPCION_INCORRECTO);
        }
    }
    
    public static void anadirLibros(Scanner scanner, LibraryManager manager){
        System.out.println("\nAñadiendo Libro");
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        int nextID = manager.consecutivoItemId();
        System.out.println("Ingresa Nombre de Autor del Libro");
        String autorLibro = scanner.nextLine();
        System.out.println("Ingresa ISBN del Libro");
        String isbn = scanner.nextLine();
        int isbnParsed = Integer.parseInt(isbn);
        System.out.println("Ingresa Titulo del Libro");
        String nombreLibro = scanner.nextLine();
        System.out.println("Ingresa Año de lanzamiento DD/MM/YYYY (Ejemplo: 30/01/2024):");
        LocalDate fechaFinal = null;
            while (fechaFinal == null) {
                System.out.print("Fecha (dd/MM/yyyy): ");
                String input = scanner.nextLine();
                try {
                    fechaFinal = LocalDate.parse(input, formato);
                } catch (DateTimeParseException e) {
                    System.out.println("Formato inválido, intente de nuevo.");
                }
            }
        manager.addItem(new Book(autorLibro, isbnParsed, "B"+nextID, nombreLibro, fechaFinal));
        try {
            manager.saveBooksToFile();
        } catch (IOException ex) {
            System.getLogger(BiblioInventory.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    public static  void anadirUsuario(Scanner scanner, LibraryManager manager){
        System.out.println("\nAñadiendo Usuario");
        int nextID = manager.consecutivoUserId();
        System.out.println("Ingresa Nombre del Usuario");
        String nombreUsuario = scanner.nextLine();
        manager.addUser(new User("U"+nextID, nombreUsuario));
        try {
            manager.saveUsersToFile();
        } catch (IOException ex) {
            System.getLogger(BiblioInventory.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
