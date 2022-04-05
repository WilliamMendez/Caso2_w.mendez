import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

/*
    Caso 2 InfraComp
    Autor:
        w.mendez - William Mendez - 202012662
*/

public class Caso2 {

    private static HashMap<Integer, Integer> memoriaFisica;

    public static void main(String[] args) throws Exception {
        imprimirLinea();
        System.out.println("Bienvenido al simulador del algoritmo de envejecimiento");
        System.out.println("Autor: w.mendez");
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

            } else if (option.equals("3")) { // Ejecutar opcion 3
                imprimirLinea();
                String nombre = input("Nombre del archivo en la carpeta data: ");
                File file = new File("data/" + nombre);
                if (file.exists()) {
                    int[] tiempos = { 3, 6, 18, 36 };
                    System.out.println("Ejecutando archivo...");
                    for (int i = 0; i < tiempos.length; i++) {
                        procesarArchivo(file, tiempos[i]);
                    }
                } else {
                    System.out.println("El archivo no existe");
                }
            } else if (option.equals("4")) { // Ejecutar opcion 4
                imprimirLinea();
                System.out.println("Generando archivos de ejemplo...");
                int[] tps = { 4, 8, 16, 32 };
                int[] tes = { 1, 2, 4 };
                int nf = 4;
                int nc = 4;
                int[] trs = { 1, 2 };

                for (int tr : trs) {
                    for (int te : tes) {
                        for (int tp : tps) {
                            System.out.println(generarArchivo(tp, te, nf, nc, tr));
                        }
                    }
                }
            } else if (option.equals("5")) { // Ejecutar opcion 5
                imprimirLinea();
                System.out.println("Ejecutando archivos de ejemplo...");
                int[] tps = { 4, 8, 16, 32 };
                int[] tes = { 1, 2, 4 };
                int nf = 4;
                int nc = 4;
                int[] trs = { 1, 2 };

                for (int te : tes) {
                    for (int tr : trs) {
                        for (int tp : tps) {
                            String nombre = tp + "-" + te + "-" + nf + "x" + nc + "-" + tr + ".txt";
                            File file = new File("data/" + nombre);
                            if (file.exists()) {
                                int[] tiempos = { 3, 6, 18, 36 };
                                System.out.println("Ejecutando archivo " + nombre + "...");
                                for (int i = 0; i < tiempos.length; i++) {
                                    procesarArchivo(file, tiempos[i]);
                                }
                                imprimirLinea();
                            } else {
                                System.out.println("El archivo " + nombre + " no existe");
                            }
                        }
                    }
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

    private static String generarArchivo(int tp, int te, int nf, int nc, int tr) {
        String nombre = tp + "-" + te + "-" + nf + "x" + nc + "-" + tr + ".txt";
        String salida = "";
        int intPorPag = tp / te;
        int nr = nf * nc * 3; // Numero de referencias
        int np = (int) Math.ceil(nr / (float) intPorPag); // Numero de paginas = tamanio de 3 matrices / tamanio de
                                                          // pagina

        try (FileWriter archivo = new FileWriter(".\\data\\" + nombre)) {
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
                        try {
                            matrices[matrizActual][filaActual][columnaActual] = letra + ":[" + filaActual + "-"
                                    + columnaActual + "]," + i + "," + j * te + "\n";
                        } catch (ArrayIndexOutOfBoundsException e) {

                        }
                        columnaActual++;
                        if (columnaActual == nc) {
                            columnaActual = 0;
                            filaActual++;
                            if (filaActual == nf) {
                                filaActual = 0;
                                matrizActual++;
                                if (matrizActual == 3) {
                                    break;
                                }
                            }
                        }
                    }
                }

                if (tr == 1) {
                    for (int i = 0; i < nf; i++) {
                        for (int j = 0; j < nc; j++) {
                            archivo.write(matrices[0][i][j]);
                            archivo.write(matrices[1][i][j]);
                            archivo.write(matrices[2][i][j]);
                        }
                    }
                } else if (tr == 2) {
                    for (int j = 0; j < nc; j++) {
                        for (int i = 0; i < nf; i++) {
                            archivo.write(matrices[0][i][j]);
                            archivo.write(matrices[1][i][j]);
                            archivo.write(matrices[2][i][j]);
                        }
                    }
                }

                salida = "Archivo generado correctamente en: data/" + nombre;

            } catch (IOException e) {
                salida = "Error al escribir en el archivo";
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                archivo.close();
            }
        } catch (IOException e) {
            salida = "Error al acceder al archivo";
            e.printStackTrace();
        }
        return salida;
    }

    private static void procesarArchivo(File file, int mp) {
        try {
            FileReader fileReader = new FileReader(file);
            try (Scanner scanner = new Scanner(fileReader)) {
                for (int i = 0; i < 6; i++) {
                    // scanner.nextLine();
                    System.out.println(scanner.nextLine());
                }
                int nr = Integer.valueOf(scanner.nextLine().split("=")[1]);

                // Crear memoria
                // memoriaFisica: Llave = pagina - Valor = envejecimiento
                memoriaFisica = new HashMap<Integer, Integer>();

                // Iniciar el proceso de envejecimiento
                Aging aging = new Aging(memoriaFisica, true);
                aging.start();

                // Recorrer archivo
                int fallos = 0;
                for (int i = 0; i < nr; i++) {
                    String linea = scanner.next();
                    String[] lineaSplit = linea.split(",");

                    int pag = Integer.parseInt(lineaSplit[1]);
                    // System.out.println(letras[k] + i + "-" + j + "] = " + pag);
                    synchronized (memoriaFisica) {
                        boolean existe = memoriaFisica.containsKey(pag);
                        if (existe) {
                            int envejecimiento = memoriaFisica.get(pag);
                            envejecimiento = envejecimiento | 0b1000000000000000000000000000000;
                            memoriaFisica.put(pag, envejecimiento);
                        } else {
                            fallos++;
                            int envejecimiento = 0b1000000000000000000000000000000;
                            if (memoriaFisica.size() < mp) {
                                // System.out.println("Agregando pagina " + pag);
                                memoriaFisica.put(pag, envejecimiento);
                            } else {
                                int aReemplazar = buscarReemplazo(memoriaFisica);

                                // if (pag - aReemplazar != 1) {
                                //     imprimirMemoria(memoriaFisica);
                                // }
                                // System.out.println("Reemplazando pagina " + aReemplazar + " por " + pag);

                                memoriaFisica.remove(aReemplazar);
                                memoriaFisica.put(pag, envejecimiento);
                            }
                        }
                    }
                    // imprimirMemoria(memoriaFisica);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Detener el proceso de envejecimiento
                aging.isAlive = false;

                // Imprimir resultados
                System.out.println(
                        "Cantidad de fallos: " + fallos + " con " + mp + " marcos de pagina");
                fileReader.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error al leer/cerrar el archivo");
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            System.err.println("Algo salio mal");
            e.printStackTrace();
        }
    }

    private static int buscarReemplazo(HashMap<Integer, Integer> memoriaFisica) {
        int aReemplazar = -1;
        int min = Integer.MAX_VALUE;
        for (int i : memoriaFisica.keySet()) {
            int envejecimiento = memoriaFisica.get(i);
            // System.out.println(Integer.toBinaryString(envejecimiento));
            if (envejecimiento < min) {
                min = envejecimiento;
                aReemplazar = i;
            }
        }
        return aReemplazar;
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

    private static void imprimirMemoria(HashMap<Integer, Integer> memoriaFisica) {
        int i = 0;
        imprimirLinea();
        for (Entry<Integer, Integer> entry : memoriaFisica.entrySet()) {
            System.out.println(i + "-" + entry.getKey() + " - " + entry.getValue() + " - "
                    + Integer.toBinaryString(entry.getValue()));
            i++;
        }
    }

    private static void imprimirMenu() {
        System.out.println("1) Generar Archivo");
        System.out.println("2) Simular ejecucion");
        System.out.println("3) Simulación grande");
        System.out.println("4) Generar archivos de ejemplo");
        System.out.println("5) Ejecutar archivos de ejemplo");
        System.out.println("0) Salir");
    }

    public static void imprimirLinea() {
        System.out.println("-----------------------------------------------------");
    }
}
