package server;

import model.Shift;
import database.DBConnection;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SchedulerServer {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("✅ Server started on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("📡 Client connected");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            String command = (String) in.readObject();

            if (command.equals("GET_ALL")) {
                List<Shift> shifts = getAllShifts();
                out.writeObject(shifts);
                out.flush();

            } else if (command.equals("ADD")) {
                Shift s = (Shift) in.readObject();
                addShift(s);
                out.writeObject("✅ Shift added successfully!");
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private List<Shift> getAllShifts() {
        List<Shift> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM shifts")) {

            while (rs.next()) {
                list.add(new Shift(
                        rs.getInt("id"),
                        rs.getString("employee_name"),
                        rs.getString("shift_time"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void addShift(Shift s) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO shifts(employee_name, shift_time, role) VALUES(?, ?, ?)")) {

            ps.setString(1, s.getEmployeeName());
            ps.setString(2, s.getShiftTime());
            ps.setString(3, s.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
