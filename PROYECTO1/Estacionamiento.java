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

        entradaFila = filasBorde[indiceEntrada];
        entradaCol = colsBorde[indiceEntrada];
        salidaFila = filasBorde[indiceSalida];
        salidaCol = colsBorde[indiceSalida];

        tablero[entradaFila][entradaCol] = "E";
        tablero[salidaFila][salidaCol] = "S";
    }

}
