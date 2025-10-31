package client;

import model.Shift;

import java.io.*;
import java.net.*;
import java.util.List;

public class SchedulerClient {
    private final String host;
    private final int port;

    public SchedulerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public List<Shift> getAllShifts() {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("GET_ALL");
            out.flush();

            return (List<Shift>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String addShift(Shift shift) {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("ADD");
            out.writeObject(shift);
            out.flush();

            return (String) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return "⚠️ Error adding shift!";
        }
    }
}
