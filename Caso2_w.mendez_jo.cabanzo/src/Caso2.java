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
        try {
            FileReader fileReader = new FileReader(file);
            try (Scanner scanner = new Scanner(fileReader)) {
                String tp = scanner.nextLine();
                String te = scanner.nextLine();
                int nf = Integer.valueOf(scanner.nextLine().split("=")[1]);
                int nc = Integer.valueOf(scanner.nextLine().split("=")[1]);
                int tr = Integer.valueOf(scanner.nextLine().split("=")[1]);
                String np = scanner.nextLine();
                int nr = Integer.valueOf(scanner.nextLine().split("=")[1]);

                // Crear memoria

                // memoriaVirtual: Llave = matriz:[fila-columna] - Valor = pagina
                HashMap<String, Integer> memoriaVirtual = new HashMap<>();

                for (int i = 0; i < nr; i++) {
                    String linea = scanner.next();
                    String[] lineaSplit = linea.split(",");
                    memoriaVirtual.put(lineaSplit[0], Integer.parseInt(lineaSplit[1]));
                }

                // memoriaFisica: Llave = pagina - Valor = envejecimiento
                memoriaFisica = new HashMap<Integer, Integer>();

                // Iniciar el proceso de envejecimiento
                Aging aging = new Aging(memoriaFisica, true);
                aging.start();

                // Recorrer memoria según el tipo de recorrido
                int cantidadInterrupciones = -1;
                if (tr == 1) {
                    cantidadInterrupciones = recorrido1(memoriaVirtual, memoriaFisica, nf, nc, mp);

                } else if (tr == 2) {
                    cantidadInterrupciones = recorrido2(memoriaVirtual, memoriaFisica, nf, nc, mp);
                } else {
                    System.err.println("Tipo de recorrido no valido");
                }

                // Detener el proceso de envejecimiento
                aging.isAlive = false;

                // Imprimir resultados
                imprimirLinea();
                System.out.println("Cantidad de interrupciones: " + cantidadInterrupciones);
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

    private static int recorrido1(HashMap<String, Integer> memoriaVirtual, HashMap<Integer, Integer> memoriaFisica,
            int nf, int nc, int mp) {

                String[] letras = { "A:[", "B:[", "C:[" };
                int fallos = 0;
        for (int i = 0; i < nf; i++) {
            for (int j = 0; j < nc; j++) {

                for (int k = 0; k < 3; k++) {
                    int pag = memoriaVirtual.get(letras[k] + i + "-" + j + "]");
                    // System.out.println(letras[k] + i + "-" + j + "] = " + pag);
                    synchronized (memoriaFisica) {
                        boolean existe = memoriaFisica.containsKey(pag);
                        if (existe) {
                            int envejecimiento = memoriaFisica.get(pag);
                            envejecimiento = envejecimiento | 0b10000000000000000000000000000000;
                            memoriaFisica.put(pag, envejecimiento);
                        } else {
                            fallos++;
                            int envejecimiento = 0b10000000000000000000000000000000;
                            if (memoriaFisica.size() <= mp) {
                                memoriaFisica.put(pag, envejecimiento);
                            } else {
                                int aReemplazar = buscarReemplazo(memoriaFisica);
                                memoriaFisica.remove(aReemplazar);
                                memoriaFisica.put(pag, envejecimiento);
                            }
                        }
                    }

                }

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return fallos;
    }

    private static int recorrido2(HashMap<String, Integer> memoriaVirtual, HashMap<Integer, Integer> memoriaFisica,
            int nf, int nc, int mp) {

                String[] letras = { "A:[", "B:[", "C:[" };
        int fallos = 0;
        for (int j = 0; j < nc; j++) {
            for (int i = 0; i < nf; i++) {

                for (int k = 0; k < 3; k++) {
                    int pag = memoriaVirtual.get(letras[k] + i + "-" + j + "]");
                    synchronized (memoriaFisica) {
                        boolean existe = memoriaFisica.containsKey(pag);
                        if (existe) {
                            int envejecimiento = memoriaFisica.get(pag);
                            envejecimiento = envejecimiento | 0b10000000000000000000000000000000;
                            memoriaFisica.put(pag, envejecimiento);
                        } else {
                            fallos++;
                            int envejecimiento = 0b10000000000000000000000000000000;
                            // System.out.println(memoriaFisica.size() + "-" + mp);
                            if (memoriaFisica.size() < mp) {
                                memoriaFisica.put(pag, envejecimiento);
                            } else {
                                int aReemplazar = buscarReemplazo(memoriaFisica);
                                System.out.println("Reemplazando pagina " + aReemplazar + " por " + pag);
                                memoriaFisica.remove(aReemplazar);
                                memoriaFisica.put(pag, envejecimiento);
                            }
                        }
                    }
                    // imprimirMemoria(memoriaFisica);
                }

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return fallos;
    }

    private static void imprimirMemoria(HashMap<Integer, Integer> memoriaFisica) {
        int i = 0;
        imprimirLinea();
        for (Entry<Integer, Integer> entry : memoriaFisica.entrySet()) {
            System.out.println(i + "-" + entry.getKey() + " - " + Integer.toBinaryString(entry.getValue()));
            i++;
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

    private static String generarArchivo(int tp, int te, int nf, int nc, int tr) {
        String nombre = tp + "-" + te + "-" + nf + "x" + nc + "-" + tr + ".txt";
        String salida = "";
        int intPorPag = tp / te;
        int nr = nf * nc * 3; // Numero de referencias
        int np = (int) Math.ceil(nr /(float) intPorPag); // Numero de paginas = tamanio de 3 matrices / tamanio de pagina

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

                for (int i = 0; i < nf; i++) {
                    for (int j = 0; j < nc; j++) {
                        archivo.write(matrices[0][i][j]);
                        archivo.write(matrices[1][i][j]);
                        archivo.write(matrices[2][i][j]);
                    }
                }

                salida = "Archivo generado correctamente en: data/" + nombre;

            } catch (IOException e) {
                salida = "Error al escribir en el archivo";
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
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

    private static void imprimirMenu() {
        System.out.println("1) Generar Archivo");
        System.out.println("2) Simular ejecucion");
        System.out.println("0) Salir");
    }

    public static void imprimirLinea() {
        System.out.println("-----------------------------------------------------");
    }
}
