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
        Scanner scanIn = new Scanner(System.in);

        do {
            printUsage();

            try {
                userOption = Integer.parseInt(scanIn.nextLine());
            } catch (NumberFormatException e) {
                userOption = IO_ERROR;
            }

            if (userOption == SELF_OPT || userOption == BROADCASTING_OPT) {
                byte[] buf = new byte[256];

                DatagramPacket packet = (userOption == SELF_OPT) ? getTime(buf) : broadcastTime(buf);
                String receivedTime = new String(packet.getData());
                System.out.println("Received time: "+receivedTime+"\n");

            } else if (userOption >= IO_ERROR) {
                System.out.println("Incorrect option! Try again!\n");
            }
        } while (userOption != EXIT_OPT);
    }

    public static void printUsage() {
        System.out.printf("0 - require time for self\n1 - require time for all\n2 - exit\n");
    }

    private static DatagramPacket getTime(byte[] buffer) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress address = InetAddress.getByName("192.168.100.1");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 8888);
        socket.send(packet);

        packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        socket.close();

        return  packet;
    }

    private static DatagramPacket broadcastTime(byte[] buffer) throws IOException {
        MulticastSocket socket = new MulticastSocket(8888);
        InetAddress group = InetAddress.getByName("192.168.100.255");
        socket.joinGroup(group);

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);
        socket.leaveGroup(group);
        socket.close();

        return packet;
    }
}




