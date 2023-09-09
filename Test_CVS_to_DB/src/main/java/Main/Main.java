package Main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String driver = "com.mysql.cj.jdbc.Driver";



        if (fileExists("src/main/java/main/cvs/clientes.csv")) {
            System.out.println("El archivo 'cvs/clientes.csv' existe.");
        } else {
            System.out.println("El archivo 'cvs/clientes.csv' no existe.");
        }



        try {
            Class.forName(driver).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //Cadena de conexion JDBC para Apache Derby.
        // "jdbc:derby:MyDerbyDb" - Idica el protocolo JDBC para conectarse a una tabla llamada MyDerbyDb.
        // ";create=true;" Indica que si la tabla no existe la va a crear.
        // "territory=es_ES" Establece localizacion de la bbdd. En este caso: España;

        String uri = "jdbc:mysql://localhost:3306/exampleDB";

        try {
            Connection conn = DriverManager.getConnection(uri, "admin", "admin"); //Inicia conexion
            conn.setAutoCommit(false); //Es necesario setear el auto commit en falso para que no genere error.
            createTables(conn); //Crea tabla
            readTable(conn);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection conn = DriverManager.getConnection(uri, "admin", "admin");
            String select = "SELECT * FROM persona";
            PreparedStatement ps1 = conn.prepareStatement(select);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(1) + ", " + rs.getString(2) + ", " + rs.getInt(3));
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }






    private static void readTable(Connection conn) throws SQLException {
        String csvFile = "src/main/java/main/cvs/clientes.csv"; // ubicacion del archivo a leer
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");
                //se imprime la lilsta del cvs con | como separador
                for (String campo : campos) {
                    System.out.print(campo + " | ");
                }
                System.out.println(); // salto de linea
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addPerson(Connection conn, int id, String nombre, String email) throws SQLException {
        String insert =
                "INSERT INTO persona (id, nombre, email) VALUES (?,?,?)"; //Preparo la insercion SQL
        PreparedStatement ps = conn.prepareStatement(insert);//Creo el Statment
        ps.setInt(1, id); //Seteo los placeholders para evitar inyecciones maliciosas.
        ps.setString(2, nombre);
        ps.setString(3, email);
        ps.executeUpdate();
        ps.close();  //Cierro el Statment
        conn.commit();
    }

    private static void createTables(Connection conn) throws SQLException {
        String table =
                "CREATE TABLE test(" +
                        "id INT PRIMARY KEY," +  // Agregamos la definición de la clave primaria
                        "nombre VARCHAR(500)," +
                        "email VARCHAR(500)" +
                        ")";
        conn.prepareStatement(table).execute();
        conn.commit();
    }



    private static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

}
