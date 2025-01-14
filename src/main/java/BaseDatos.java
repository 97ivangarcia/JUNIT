import java.sql.*;
import java.util.Scanner;

public class BaseDatos {
    private static final String DB_URL = "jdbc:sqlite:gastos_ingresos.db";
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUserTable() {
        String createUserTable = "CREATE TABLE IF NOT EXISTS users (dni TEXT PRIMARY KEY, saldo REAL DEFAULT 0.0);";
        try (PreparedStatement pstmt = connection.prepareStatement(createUserTable)) {
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateUser(String dni) {
        String query = "SELECT dni FROM users WHERE dni = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUserWithDni(String dni) {
        if (!authenticateUser(dni)) {  // Verifica si el DNI ya está registrado.
            String insertUser = "INSERT INTO users (dni) VALUES (?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
                pstmt.setString(1, dni);
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;  // Si el DNI ya existe, no lo registra de nuevo.
    }

    public double getSaldo(String dni) {
        String query = "SELECT saldo FROM users WHERE dni = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, dni);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;  // Si no encuentra el usuario, el saldo es 0 por defecto.
    }


    public void updateSaldo(String dni, double saldo) {
        String updateSaldo = "UPDATE users SET saldo = ? WHERE dni = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSaldo)) {
            pstmt.setDouble(1, saldo);
            pstmt.setString(2, dni);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
