package BaseDeDatos;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class App
{
    public static void main( String[] args ) {
        String driver = "ocom.mysql.jdbc.Driver";

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

        try{
            Connection conn = DriverManager.getConnection(uri, "admin", "admin"); //Inicia conexion
            createTables(conn); //Crea tabla
            addPerson(conn, 1, "Juan", 20); //Agrega "persona" a la tabla
            addPerson(conn, 2, "Paula", 23); //Agrega "persona" a la tabla
            conn.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        try{
            Connection conn = DriverManager.getConnection(uri);
            String select = "SELECT * FROM persona";
            PreparedStatement ps1 = conn.prepareStatement(select);
            ResultSet rs = ps1.executeQuery();
            while(rs.next()){
                System.out.println(rs.getInt(1) +", "+ rs.getString(2)+", "+ rs.getInt(3));
            }
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    private static void addPerson(Connection conn, int id, String nombre, int edad) throws SQLException {
        String insert =
                "INSERT INTO persona (id, nombre, edad) VALUES (?,?,?)"; //Preparo la insercion SQL
        PreparedStatement ps = conn.prepareStatement(insert);//Creo el Statment
        ps.setInt(1, id); //Seteo los placeholders para evitar inyecciones maliciosas.
        ps.setString(2, nombre);
        ps.setInt(3, edad);
        ps.executeUpdate();
        ps.close();  //Cierro el Statment
        conn.commit();
    }

    private static void createTables(Connection conn) throws SQLException {
        String table =
                "CREATE TABLE persona(" +
                        "id INT," +
                        "nombre VARCHAR(500)," +
                        "edad INT," +
                        "PRIMARY KEY(id))";
        conn.prepareStatement(table).execute();
        conn.commit();
    }
