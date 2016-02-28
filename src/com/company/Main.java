package com.company;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    private static final int SELF_OPT = 0;
    private static final int BROADCASTING_OPT = 1;
    private static final int EXIT_OPT = 2;
    private static final int IO_ERROR = 3;

    public static void main(String[] args) throws IOException{
        int userOption;
        String receivedTime;
        Scanner scanIn = new Scanner(System.in);

        do {
            printUsage();

            try {
                userOption = Integer.parseInt(scanIn.nextLine());
            } catch (NumberFormatException e) {
                userOption = IO_ERROR;
            }

            switch (userOption) {
                case SELF_OPT:
                    receivedTime = getTime();
                    System.out.println("Received time "+receivedTime+"\n");
                    break;
                case BROADCASTING_OPT:
                    //broadcastTime();
                    break;
                case EXIT_OPT:
                    break;
                default:
                    System.out.println("Incorrect option! Try again\n");
                    break;
            }

        } while (userOption != EXIT_OPT);
    }

    public static void printUsage() {
        System.out.printf("0 - require time for self\n1 - require time for all\n2 - exit\n");
    }

    public static String getTime() throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] buf = new byte[256];
        InetAddress address = InetAddress.getByName("192.168.100.1");
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8888);

        socket.send(packet);
        packet = new DatagramPacket(buf, buf.length);

        socket.receive(packet);
        String receivedTime = new String(packet.getData(), 0, packet.getLength());
        socket.close();

        return receivedTime;
    }
}




