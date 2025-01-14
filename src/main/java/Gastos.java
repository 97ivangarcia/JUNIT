import java.util.Scanner;

class Gastos {
    public double realizarGasto(Scanner scanner, double saldo) {
        System.out.println("¿En qué desea gastar? (1. Vacaciones, 2. Alquiler, 3. IRPF, 4. Vicios guarros):");
        int tipoGasto = Integer.parseInt(scanner.nextLine());
        double gasto;
        switch (tipoGasto) {
            case 1:
                gasto = obtenerGasto(scanner, "Vacaciones");
                break;
            case 2:
                gasto = obtenerGasto(scanner, "Alquiler");
                break;
            case 3:
                gasto = obtenerGasto(scanner, "IRPF (15% de la nómina)");
                break;
            case 4:
                gasto = obtenerGasto(scanner, "Vicios guarros");
                break;
            default:
                System.out.println("Tipo de gasto no válido.");
                return saldo;
        }
        saldo -= gasto;
        System.out.println("Se ha realizado este gasto. Su saldo final es: " + saldo);
        return saldo;
    }

    private double obtenerGasto(Scanner scanner, String concepto) {
        System.out.println("Ingrese el gasto para " + concepto + ":");
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Gasto no válido. Intente de nuevo:");
            }
        }
    }
}
