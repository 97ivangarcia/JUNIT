import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class interfaz {
    private static BaseDatos dbManager = new BaseDatos();
    private static double saldo = 0.0;
    private static String dni = null;

    public static void main(String[] args) {
        dbManager.connect();
        dbManager.createUserTable();

        JFrame frame = new JFrame("Gestión de Gastos e Ingresos.");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 1));

        JLabel saldoLabel = new JLabel("Saldo actual: " + saldo, SwingConstants.CENTER);
        frame.add(saldoLabel);

        if (dni == null) {
            dni = solicitarDNI(frame);  // Solicita el DNI solo al inicio y lo registra si no existe.
        } else {
            JOptionPane.showMessageDialog(frame, "Bienvenido nuevamente.");
        }

        // Obtener el saldo desde la base de datos y mostrarlo en el label
        saldo = dbManager.getSaldo(dni);  // Obtiene el saldo desde la base de datos.
        saldoLabel.setText("Saldo actual: " + saldo);  // Muestra el saldo inicial.

        JButton gastoButton = new JButton("Registrar Gasto");
        gastoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarGasto(frame, saldoLabel);
            }
        });
        frame.add(gastoButton);

        JButton ingresoButton = new JButton("Registrar Ingreso");
        ingresoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarIngreso(frame, saldoLabel);
            }
        });
        frame.add(ingresoButton);

        JButton salirButton = new JButton("Salir");
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dbManager.disconnect();
                JOptionPane.showMessageDialog(frame, "Saldo final: " + saldo);
                System.exit(0);
            }
        });
        frame.add(salirButton);

        frame.setVisible(true);
    }

    private static String solicitarDNI(JFrame frame) {
        String dniInput = JOptionPane.showInputDialog(frame, "Por favor, introduzca su DNI:");

        if (dniInput == null || dniInput.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "DNI inválido. Intente nuevamente.");
            return solicitarDNI(frame);
        }

        if (!esDniValido(dniInput)) {
            JOptionPane.showMessageDialog(frame, "DNI no válido. Por favor, ingrese un DNI en formato correcto (ej: 12345678A).");
            return solicitarDNI(frame);
        }

        if (dbManager.registerUserWithDni(dniInput)) {
            JOptionPane.showMessageDialog(frame, "Usuario registrado exitosamente.");
        } else {
            JOptionPane.showMessageDialog(frame, "El usuario ya está registrado.");
        }

        // Cargar el saldo inicial desde la base de datos
        saldo = dbManager.getSaldo(dniInput);
        return dniInput;
    }

    private static boolean esDniValido(String dni) {
        if (dni == null || dni.length() != 9) {
            return false;  // El DNI debe tener 9 caracteres (8 números + 1 letra)
        }

        // Extraer la parte numérica y la letra
        String numeros = dni.substring(0, 8);
        String letra = dni.substring(8);

        try {
            int numero = Integer.parseInt(numeros);  // Verificar que los 8 primeros caracteres son números
        } catch (NumberFormatException e) {
            return false;  // Si no es válido numéricamente
        }

        // Verificar que la última letra sea válida (calculada con el resto)
        char letraCalculada = calcularLetraDni(Integer.parseInt(numeros));
        return letraCalculada == letra.charAt(0);
    }

    private static char calcularLetraDni(int numero) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        return letras.charAt(numero % 23);
    }

    private static void realizarGasto(JFrame frame, JLabel saldoLabel) {
        String[] opciones = {"Vacaciones", "Alquiler", "IRPF (15%)", "Vicios"};
        String opcion = (String) JOptionPane.showInputDialog(frame, "Seleccione el tipo de gasto:",
                "Registrar Gasto", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (opcion != null) {
            String montoStr = JOptionPane.showInputDialog(frame, "Ingrese el monto del gasto para " + opcion + ":");
            try {
                double gasto = Double.parseDouble(montoStr);
                saldo -= gasto;
                saldoLabel.setText("Saldo actual: " + saldo);  // Actualiza el saldo en la interfaz
                JOptionPane.showMessageDialog(frame, "Gasto registrado correctamente.");
                dbManager.updateSaldo(dni, saldo);  // Actualiza el saldo en la base de datos.
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Monto inválido. Intente de nuevo.");
            }
        }
    }

    private static void realizarIngreso(JFrame frame, JLabel saldoLabel) {
        String[] opciones = {"Nómina", "Venta de órganos"};
        String opcion = (String) JOptionPane.showInputDialog(frame, "Seleccione el tipo de ingreso:",
                "Registrar Ingreso", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        if (opcion != null) {
            String montoStr = JOptionPane.showInputDialog(frame, "Ingrese el monto del ingreso para " + opcion + ":");
            try {
                double ingreso = Double.parseDouble(montoStr);
                saldo += ingreso;
                saldoLabel.setText("Saldo actual: " + saldo);  // Actualiza el saldo en la interfaz
                JOptionPane.showMessageDialog(frame, "Ingreso registrado correctamente.");
                dbManager.updateSaldo(dni, saldo);  // Actualiza el saldo en la base de datos.
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Monto inválido. Intente de nuevo.");
            }
        }
    }
}
