import java.util.Scanner;

class Ingresos {
    public double realizarIngreso(Scanner scanner, double saldo) {
        System.out.println("¿De dónde proviene el ingreso? (1. Nómina, 2. Venta de órganos):");
        int tipoIngreso = Integer.parseInt(scanner.nextLine());
        double monto;
        switch (tipoIngreso) {
            case 1:
                monto = obtenerGasto(scanner, "Nómina");
                break;
            case 2:
                monto = obtenerGasto(scanner, "Venta de órganos");
                break;
            default:
                System.out.println("Tipo de ingreso no válido.");
                return saldo;
        }
        saldo += monto;
        System.out.println("Se ha registrado este ingreso. Su saldo final es: " + saldo);
        return saldo;
    }

    private double obtenerGasto(Scanner scanner, String concepto) {
        System.out.println("Ingrese el monto para " + concepto + ":");
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Monto no válido. Intente de nuevo:");
            }
        }
    }
}
