import java.util.Random;
import java.util.Scanner;

/**
 * Sistema de Estacionamiento - menu principal en consola.
 */
public class Main {

    public static void main(String[] args) {
        //"Scanner" es una clase que Java ya trae preparada para **leer datos de entrada**
        //Declara y crea un objeto de la clase Scanner llamado sc, que permite leer datos desde el teclado.
        Scanner sc = new Scanner(System.in);
        //Llama de clase Estacionamiento 
        Random rnd = new Random();
        Estacionamiento estacionamiento = new Estacionamiento(rnd);

        boolean salir = false;
        //repetir el menu mientras no se haya seleccionado salir
        while (!salir) {
            mostrarMenu(); 
            int opcion = leerEntero(sc, "Seleccione una opcion: ");

            switch (opcion) {
                case 1:
                    //Llama al metodo para ingresar vehiculos 
                    opcionIngresarVehiculo(sc, estacionamiento);
                    break;
                case 2:

                    opcionRetirarVehiculo(sc, estacionamiento);
                    break;
                case 3:
                    estacionamiento.imprimirTablero();
                    break;
                case 4:
                    opcionBuscarPlaca(sc, estacionamiento);
                    break;
                case 5:
                    estacionamiento.mostrarRuta();
                    break;
                case 6:
                    estacionamiento.mostrarIngresos();
                    break;
                case 7:
                    salir = true;
                    System.out.println("Finalizando el sistema de estacionamiento.");
                    break;
                default:
                    System.out.println("Opcion invalida, intente de nuevo.");
            }
            System.out.println();
        }

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("===== SISTEMA DE ESTACIONAMIENTO =====");
        System.out.println("1. Ingresar vehiculo");
        System.out.println("2. Retirar vehiculo");
        System.out.println("3. Mostrar estacionamiento");
        System.out.println("4. Buscar vehiculo por placa");
        System.out.println("5. Mostrar ruta mas corta entre entrada y salida");
        System.out.println("6. Mostrar ingresos");
        System.out.println("7. Salir");
        System.out.println(" ");
    }
        //int opcion = leerEntero(sc, "Seleccione una opcion: ");   
        private static int leerEntero(Scanner sc, String mensaje) {
            int valor;
            while (true) {
                System.out.print(mensaje);
                // hasNextInt() verifica si el siguiente dato es un entero
                if (sc.hasNextInt()) {
                    valor = sc.nextInt();
                    return valor;
                }
                System.out.println("Debe ingresar un numero entero valido.");
                sc.next();
            }
        }
    private static String leerPlacaValida(Scanner sc) {
        String placa;
        while (true) {  
            
            System.out.print("Ingrese la placa: ");
            //"Placa será la variable para guardar la placa"
            //sc.next() sirve para leer la palabra hasta encontrar un espacio
            //.thim() sirve para eliminar espacios que esten adelante o después de la palabra
            placa = sc.next().trim();
            //Se llama el metodo validarPlaca de el objeto vehiculos 
            if (Vehiculo.validarPlaca(placa)) {
                System.out.println("Placa valida");
                return placa;
            }
            System.out.println("Placa invalida");
        }
    }
    
    private static void opcionIngresarVehiculo(Scanner sc, Estacionamiento estacionamiento) {
        String placa = leerPlacaValida(sc);
        //Se llama el metodo procedente de la clase Placa
        //Si si existe la placa muestra el mensaje
        if (estacionamiento.existePlaca(placa)) {
            System.out.println("Placa ya existente");
            //Retorna el mensaje 
            return;
        }
        //Esta llamando el metodo esta lleno de el objeto Estacionamiento
        if (estacionamiento.estaLleno()) {
            System.out.println("Estacionamiento lleno");
            return;
        }
        //vale -1 porque el valor de 0 si existe como indice real de la matriz, por lo tanto 
        //El valor -1 nunca se va a confundir con un valor real
        int fila = -1;
        int col = -1;
        boolean posicionValida = false;
        //Mientras posicion valida sea distinto de falso
        while (!posicionValida) {
            fila = leerEntero(sc, "Ingrese la fila (1-8): ");
            col = leerEntero(sc, "Ingrese la columna (1-8): ");
            //Se llama el metodo filaColValida de la clase estacionamiento
            if (!estacionamiento.filaColValida(fila, col)) {
                System.out.println("Fila/Columna invalida");
                //Llama el metodo espacioOcupado de la clase estacionamiento
            } else if (estacionamiento.espacioOcupado(fila, col)) {
                System.out.println("Espacio ocupado");
            } else {
                posicionValida = true;
            }
        }

        double monto = -1;
        double cambio = 0;
        boolean pagoValido = false;
        while (!pagoValido) {
            //Ingresa el valor del monto
            monto = leerDouble(sc, "Ingrese el monto entregado: Q");
            if (monto < 0) {
                //Si el monto es menor a 0 el pago es invalido   
                System.out.println("Valor invalido");
                //En "Estacionamiento.TARIFA" se llama la variable que pertenece a la clase Estacionemiento llamada TARIFA
            } else if (monto < Estacionamiento.TARIFA) {
                si el pago es menor a la tarifa del estacionamiento el pago es insuficiente
                System.out.println("Pago insuficiente");
            } else {
                //Si no es ninguno de los casos se da vuelto y se guarda como valido
                cambio = monto - Estacionamiento.TARIFA;
                pagoValido = true;
            }
        }

        estacionamiento.registrarVehiculo(placa, fila, col);

        System.out.println("Placa: " + placa);
        System.out.println("Fila: " + fila);
        System.out.println("Columna: " + col);
        System.out.printf("Tarifa: Q%.2f%n", Estacionamiento.TARIFA);
        System.out.printf("Monto entregado: Q%.2f%n", monto);
        System.out.printf("Cambio: Q%.2f%n", cambio);
        System.out.println("Vehiculo ingresado correctamente.");
    }

    private static void opcionRetirarVehiculo(Scanner sc, Estacionamiento estacionamiento) {
        //Se valida si la placa es valida 
        String placa = leerPlacaValida(sc);
        //Se llama al método de liberar espacio en la calse estacionamiento, verificando si hay una placa o no
        int[] posicion = estacionamiento.liberarEspacio(placa);
        //si no encuentra la placa
        if (posicion == null) {
            System.out.println("Placa no encontrada");
        } else {
            //Si si encuentra la placa desplega la posición y muestra el mensaje
            System.out.println("Fila: " + posicion[0]);
            System.out.println("Columna: " + posicion[1]);
            System.out.println("Vehiculo retirado correctamente.");
        }
    }
    //Atributos 
    private static void opcionBuscarPlaca(Scanner sc, Estacionamiento estacionamiento) {
        //se verifica si la placa es valida y se guarda en placa
        String placa = leerPlacaValida(sc);
        //se llama al metodo para buscar la placa de la clase estacionamiento 
        int[] posicion = estacionamiento.buscarPlaca(placa);
        //si el metodo de busqueda no encuentra ninguna coincidencia 
        if (posicion == null) {
            System.out.println("No fue encontrada");
        } else {
            //Si si muestra el mensaje y la posicion en la matriz
            System.out.println("Vehiculo encontrado.");
            System.out.println("Fila: " + posicion[0]);
            System.out.println("Columna: " + posicion[1]);
        }
    }
        //Lee numeros decimales
        //leerDouble(sc, "Seleccione una opcion: ");   
    private static double leerDouble(Scanner sc, String mensaje) {
        double valor;
        while (true) {
            System.out.print(mensaje);
            //verifica si el dato es double = hasNextDouble
            if (sc.hasNextDouble()) {
                //nextDouble = lee y guarda el double 
                valor = sc.nextDouble();
                return valor;
            }
            System.out.println("");
            System.out.println("Debe ingresar un valor numerico valido.");
            sc.next();
        }
    }
}
