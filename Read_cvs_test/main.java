import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class main {
    public static void main(String[] args) {
        String csvFile = "cvs/clientes.csv"; // ubicacion del archivo a leer
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
}