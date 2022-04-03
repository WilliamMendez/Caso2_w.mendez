import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/*
    Caso 2 InfraComp
    Autores:
        w.mendez - William Mendez - 202012662
        jo.cabanzo - Obed Cabanzo - 201911749
*/

public class Caso2 {
    public static void main(String[] args) throws Exception {
        imprimirLinea();
        System.out.println("Bienvenido al simulador del algoritmo de envejecimiento");
        System.out.println("Autores: w.mendez - jo.cabanzo");
        System.out.println("Caso 2 InfraComp - 2022-1");
        boolean salir = false;
        while (!salir) {
            imprimirLinea();
            imprimirMenu();
            String option = input("Seleccione una opcion: ");
            if (option.equals("1")) { // Ejecutar opcion 1
                imprimirLinea();
                System.out.println("Generacion de Archivo:");
                int tp = intput("Tamanio de pagina:");
                int te = intput("Tamanio de un entero:");
                int nf = intput("Numero de filas de las matrices:");
                int nc = intput("Numero de columnas de las matrices:");
                int tr = intput("Tipo de recorrido (1)/(2):");
                System.out.println("Generando archivo...");
                System.out.println(generarArchivo(tp, te, nf, nc, tr));
            } else if (option.equals("2")) { // Ejecutar opcion 2
                imprimirLinea();
                String file = input("Nombre del archivo de la carpeta data: ");

            } else if (option.equals("0")) { // Ejecutar opcion 0
                System.out.println("Saliendo...");
                salir = true;
            } else {
                imprimirLinea();
                System.err.println("Opcion no valida");
            }
        }
    }

    private static void imprimirMenu() {
        System.out.println("1) Generar Archivo");
        System.out.println("2) Simular ejecucion");
        System.out.println("0) Salir");
    }

    public static void imprimirLinea() {
        System.out.println("-----------------------------------------------------");
    }

    private static String generarArchivo(int tp, int te, int nf, int nc, int tr) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm"));
        String salida = "";
        int nr = nf * nc * 3; // Numero de referencias
        int np = (int) Math.ceil(nr * te / (float) tp); // Numero de paginas = tamanio de 3 matrices / tamanio de pagina

        try (FileWriter archivo = new FileWriter(".\\data\\" + fecha + ".txt")) {
            try {
                // Escritura de los parametros
                archivo.write("TP=" + tp + "\n"); // Tamanio de pagina
                archivo.write("TE=" + te + "\n"); // Tamanio de un entero
                archivo.write("NF=" + nf + "\n"); // Numero de filas
                archivo.write("NC=" + nc + "\n"); // Numero de columnas
                archivo.write("TR=" + tr + "\n"); // Tipo de recorrido
                // Escritura del numero de paginas virtuales necesarias para almacenar las 3
                // matrices
                archivo.write("NP=" + np + "\n");
                // Escritura del numero de referencias de pagina que el proceso generara en
                // ejecucion
                archivo.write("NR=" + nr + "\n");
                // Escritura de las referencias de pagina
                int inicio1 = 0;
                int inicio2 = np / 3;
                int inicio3 = np * 2 / 3;

                for (int i = 0; i < nf; i++) {
                    for (int j = 0; j < nc; j++) {
                        archivo.write("A:[" + i + "-" + j + "]," + (inicio1 + i) + "," + j * te + "\n");
                        archivo.write("B:[" + i + "-" + j + "]," + (inicio2 + i) + "," + j * te + "\n");
                        archivo.write("C:[" + i + "-" + j + "]," + (inicio3 + i) + "," + j * te + "\n");
                    }
                }

                salida = "Archivo generado correctamente en: data/" + fecha + ".txt";

            } catch (IOException e) {
                salida = "Error al escribir en el archivo";
            } finally {
                archivo.close();
            }
        } catch (IOException e) {
            salida = "Error al acceder al archivo";
            e.printStackTrace();
        }
        return salida;
    }

    public static String input(String text) {
        System.out.println(text);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static int intput(String text) {
        System.out.println(text);
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
}
