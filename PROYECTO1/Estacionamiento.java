import java.util.Random; //Random es una clase permite usar herramientas para generar numeros aleatorios dentro del programa.

/**
 * Modelo y logica del estacionamiento: tablero 10x10, entrada/salida,
 * registro/liberacion de espacios, calculo de ruta e ingresos.
 * Solo utiliza arreglos nativos (unidimensionales y bidimensionales).
 */
public class Estacionamiento {

    public static final double TARIFA = 10.0;
    private static final int TAM_TABLERO = 10;
    private static final int TAM_PERIMETRO = 36;

    // Bordes (fila/columna 0 o 9) guardan "=", "E" o "S".
    // Interior (fila/columna 1-8) guarda null (libre) o la placa (ocupado).
    private String[][] tablero;
    //Declaracion de variables para generar la entrada y salida
    private int entradaFila, entradaCol;
    private int salidaFila, salidaCol;

    private int espaciosOcupados;
    private int vehiculosCobrados;
    private double totalRecaudado;
    //Un constructor es un metodo especial que se ejecuta automaticamente cuando creamos un objeto de una clase. 
    public Estacionamiento(Random rnd) {
        tablero = new String[TAM_TABLERO][TAM_TABLERO];
        for (int f = 0; f < TAM_TABLERO; f++) {
            for (int c = 0; c < TAM_TABLERO; c++) {
                //==(Igual a); si las columnas y filas son = 0 = 9 se muestra "="
                boolean esBorde = (f == 0 || f == TAM_TABLERO - 1 || c == 0 || c == TAM_TABLERO - 1);
                //Operador ternario= es una forma corta de escribir una condicion if-else
                //condicion ? valorSiVerdadero : valorSiFalso;
                tablero[f][c] = esBorde ? "=" : null;
            }
        }
        espaciosOcupados = 0;
        vehiculosCobrados = 0;
        totalRecaudado = 0.0;
        //Se llama al metodo dentro de esta misma clase
        generarEntradaSalida(rnd);
    }

    private void generarEntradaSalida(Random rnd) {
        int[] filasBorde = new int[32];
        int[] colsBorde = new int[32];
        int idx = 0;

        // Fila superior e inferior, columnas 1-8 (sin esquinas)
        for (int c = 1; c <= 8; c++) {
            filasBorde[idx] = 0;
            colsBorde[idx] = c;
            idx++;
        }
        for (int c = 1; c <= 8; c++) {
            filasBorde[idx] = TAM_TABLERO - 1;
            colsBorde[idx] = c;
            idx++;
        }
        // Columna izquierda y derecha, filas 1-8 (sin esquinas)
        for (int f = 1; f <= 8; f++) {
            filasBorde[idx] = f;
            colsBorde[idx] = 0;
            idx++;
        }
        for (int f = 1; f <= 8; f++) {
            filasBorde[idx] = f;
            colsBorde[idx] = TAM_TABLERO - 1;
            idx++;
        }
        //es para generar la entrada y salida aleatoriamente 
        //se declara que indiceEntrada es entero 
        //rnd.nexInt(32) genera un numero de 0 a 31 y lo guarda en indiceEntrada
        int indiceEntrada = rnd.nextInt(32);
        //Lo mismo para indiceSalida
        int indiceSalida = rnd.nextInt(32);
        while (indiceSalida == indiceEntrada) {
            indiceSalida = rnd.nextInt(32);
        }
        //filasBorde/colsBorde son las matrices declaradas que van de las columnas y filas exteriores sin contar las esquinas
        //indice entrada es la que genera aleatoriamente un numero de 0 a 31
        entradaFila = filasBorde[indiceEntrada];
        entradaCol = colsBorde[indiceEntrada];
        salidaFila = filasBorde[indiceSalida];
        salidaCol = colsBorde[indiceSalida];
        //se declara lo que tiene que mostrar en el array de Estacionamiento(Randon rnd)
        tablero[entradaFila][entradaCol] = "E";
        tablero[salidaFila][salidaCol] = "S";
    }
   //Metodo para verificar si la fila y la columna son validas; regresa un booleano
    public boolean filaColValida(int fila, int col) {
        return fila >= 1 && fila <= 8 && col >= 1 && col <= 8;
    }
    //si la casilla es distinto = ! a "null" 
    public boolean espacioOcupado(int fila, int col) {
        return tablero[fila][col] != null;
    }
    //Si la variable espaciosOcupados es mayor o igual a 64 retorna el valor true
    public boolean estaLleno() {
        return espaciosOcupados >= 64;
    }
    //recorre la matriz y compara la placa con la matriz del método estacionamiento  
    public boolean existePlaca(String placa) {
        for (int f = 1; f <= 8; f++) {
            for (int c = 1; c <= 8; c++) {
                //equal: compara una cadena de texto
                if (placa.equals(tablero[f][c])) {
                    //Si la placa existe retorna true 
                    return true;
                }
            }
        }
        return false;
    }

    /** Retorna {fila, columna} si la encuentra, o null si no existe. */
    public int[] buscarPlaca(String placa) {
        for (int f = 1; f <= 8; f++) {
            for (int c = 1; c <= 8; c++) {
                if (placa.equals(tablero[f][c])) {
                    return new int[] { f, c };
                }
            }
        }
        return null;
    }
    //El contador de espacios ocupados, cantidad de vehiculos y total de racudados va aumentando 
    public void registrarVehiculo(String placa, int fila, int col) {
        tablero[fila][col] = placa;
        espaciosOcupados++;
        vehiculosCobrados++;
        //TARIFA=10
        totalRecaudado += TARIFA;
    }

    /** Libera el espacio de la placa dada. Retorna {fila, columna} liberada o null si no existia. */
    public int[] liberarEspacio(String placa) {
        //Se llama al metodo buscarplaca y se guarda en posicion
        int[] posicion = buscarPlaca(placa);
        if (posicion == null) {
            return null;
        }
        //si encuentra la placa lo quita 
        tablero[posicion[0]][posicion[1]] = null;
        //resta el numero de espacios ocupados
        espaciosOcupados--;
        return posicion;
    }

    public void imprimirTablero() {
        System.out.println("");
        System.out.println("E = Entrada   S = Salida   = = Via exterior");
        System.out.println("L = Lugar libre   A = Automovil");
        System.out.println();

        System.out.print("   ");
        for (int c = 1; c <= 8; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        //Recorre fila 0 a 9
        for (int f = 0; f < TAM_TABLERO; f++) {
            if (f == 0 || f == TAM_TABLERO - 1) {
                System.out.print("  ");
            } else {
                //Imprime los numeros de las filas
                System.out.printf("%2d ", f);
            }
            for (int c = 0; c < TAM_TABLERO; c++) {
                String celda = tablero[f][c];
                //Imprime L de libre
                if (celda == null) {
                    System.out.print("L ");
                    //La celda imprime =, E o S
                } else if (celda.equals("=") || celda.equals("E") || celda.equals("S")) {
                    System.out.print(celda + " ");
                } else {
                    //Si la matriz tiene guardada una placa imprime A=Automovil
                    System.out.print("A ");
                }
            }
            System.out.println();
        }
        //Escribe los espacios libres y ocupados
        System.out.println();
        System.out.println("Espacios libres: " + (64 - espaciosOcupados));
        System.out.println("Espacios ocupados: " + espaciosOcupados);
    }

    /** Indice 0-35 de una celda de borde recorriendo en sentido horario desde (0,0). */
    private int perimetroIndice(int fila, int col) {
        if (fila == 0) {
            return col;
        }
        if (col == TAM_TABLERO - 1) {
            return 9 + fila;
        }
        if (fila == TAM_TABLERO - 1) {
            return 19 + (8 - col);
        }
        return 28 + (8 - fila);
    }

    public void mostrarRuta() {
        System.out.println("Entrada: fila " + (entradaFila + 1) + ", columna " + (entradaCol + 1));
        System.out.println("Salida: fila " + (salidaFila + 1) + ", columna " + (salidaCol + 1));

        int indiceEntrada = perimetroIndice(entradaFila, entradaCol);
        int indiceSalida = perimetroIndice(salidaFila, salidaCol);

        int horario = ((indiceSalida - indiceEntrada) + TAM_PERIMETRO) % TAM_PERIMETRO;
        int antihorario = TAM_PERIMETRO - horario;

        System.out.println("Distancia sentido horario: " + horario + " posiciones");
        System.out.println("Distancia sentido antihorario: " + antihorario + " posiciones");

        if (horario < antihorario) {
            System.out.println("Ruta recomendada: sentido horario (" + horario + " posiciones)");
        } else if (antihorario < horario) {
            System.out.println("Ruta recomendada: sentido antihorario (" + antihorario + " posiciones)");
        } else {
            System.out.println("Ambas rutas tienen la misma distancia, cualquiera puede utilizarse (" + horario + " posiciones)");
        }
    }

    public void mostrarIngresos() {
        System.out.println("===== INGRESOS =====");
        System.out.println("Vehiculos cobrados: " + vehiculosCobrados);
        System.out.printf("Tarifa por vehiculo: Q%.2f%n", TARIFA);
        System.out.printf("Total recaudado: Q%.2f%n", totalRecaudado);
    }
}
