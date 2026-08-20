/**
 * Representa un vehiculo y valida el formato de su placa.
 * Formato exigido: P###LLL (P mayuscula literal, 3 digitos, 3 letras mayusculas).
 */
public class Vehiculo {

    public static boolean validarPlaca(String placa) {
        if (placa == null) {
            return false;
        }
        return placa.matches("P[0-9]{3}[A-Z]{3}");
    }
}
