import java.io.File;
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
                String nombre = input("Nombre del archivo en la carpeta data: ");
                File file = new File("data/" + nombre);
                if (file.exists()) {
                    int mp = intput("Numero de marcos de pagina:");
                    System.out.println("Ejecutando archivo...");
                    procesarArchivo(file, mp);
                } else {
                    System.out.println("El archivo no existe");
                }

            } else if (option.equals("0")) { // Ejecutar opcion 0
                imprimirLinea();
                System.out.println("Saliendo...");
                salir = true;
            } else {
                imprimirLinea();
                System.err.println("Opcion no valida");
            }
        }
    }

    private static void procesarArchivo(File file, int mp) {
    }

    private static String generarArchivo(int tp, int te, int nf, int nc, int tr) {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm"));
        String salida = "";
        int intPorPag = tp / te;
        int nr = nf * nc * 3; // Numero de referencias
        int np = nr / intPorPag; // Numero de paginas = tamanio de 3 matrices / tamanio de pagina

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

                String[][] matriz1 = new String[nf][nc];
                String[][] matriz2 = new String[nf][nc];
                String[][] matriz3 = new String[nf][nc];
                String[][][] matrices = { matriz1, matriz2, matriz3 };

                int matrizActual = 0;
                int filaActual = 0;
                int columnaActual = 0;

                for (int i = 0; i < np; i++) {
                    for (int j = 0; j < intPorPag; j++) {
                        String letra = "";
                        if (matrizActual == 0) {
                            letra = "A";
                        } else if (matrizActual == 1) {
                            letra = "B";
                        } else if (matrizActual == 2) {
                            letra = "C";
                        }
                        matrices[matrizActual][filaActual][columnaActual] = letra + ":[" + filaActual + ","
                                + columnaActual + "]," + i + "," + j * te + "\n";
                        columnaActual++;
                        if (columnaActual == nc) {
                            columnaActual = 0;
                            filaActual++;
                            if (filaActual == nf) {
                                filaActual = 0;
                                matrizActual++;
                            }
                        }
                    }
                }

                for (int i = 0; i < nf; i++) {
                    for (int j = 0; j < nc; j++) {
                        archivo.write(matrices[0][i][j]);
                        archivo.write(matrices[1][i][j]);
                        archivo.write(matrices[2][i][j]);
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
        System.out.print(text);
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    private static void imprimirMenu() {
        System.out.println("1) Generar Archivo");
        System.out.println("2) Simular ejecucion");
        System.out.println("0) Salir");
    }

    public static void imprimirLinea() {
        System.out.println("-----------------------------------------------------");
    }
}
