package com.company;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    private static final int SELF_OPT = 0;
    private static final int BROADCASTING_OPT = 1;
    private static final int EXIT_OPT = 2;
    private static final int IO_ERROR = 3;
    private static final Scanner scanIn = new Scanner(System.in);

    public static void main(String[] args) {
        UDPClient client = new UDPClient();
        client.start();

        int userOption;
        System.out.printf("Usage: 0 - request for self; 1 - broadcasting; 2 - exit\n");
        do {
            try {
                userOption = Integer.parseInt(scanIn.nextLine());
                if (userOption == SELF_OPT || userOption == BROADCASTING_OPT)
                    client.sendPackage(userOption);
            } catch (NumberFormatException e) {
                userOption = IO_ERROR;
                System.err.println(e);
            }
        } while (userOption != EXIT_OPT);
    }
}

class UDPClient extends Thread {
    protected DatagramSocket socket;
    protected byte[] buffer;
    protected InetAddress address;
    private static final int PORT = 5555;

    public UDPClient() {
        super("UDP CLIENT THREAD");
        buffer = new byte[256];

        try {
            address = InetAddress.getByName("10.211.55.2");
        } catch (UnknownHostException e) {
            System.err.println(e);
        }

        try {
            socket = new DatagramSocket(PORT);
            socket.setBroadcast(true);
        } catch (SocketException e) {
            System.err.println(e);
        }
    }

    public void sendPackage(int option) {
        buffer[0] = (byte) option;
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void run() {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (true) {
            try {
                socket.receive(packet);
                String receivedTime = new String(packet.getData());
                System.out.println("Received data: " + receivedTime);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}


