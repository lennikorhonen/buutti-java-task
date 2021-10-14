package com.company;

import java.io.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Data data = new Data();
        // Start read thread
        Thread readFile = new Thread(new CopyFile(data));
        readFile.start();
        // Start write thread
        Thread writeFile = new Thread(new WriteFile(data));
        writeFile.start();
    }
}

class CopyFile implements Runnable {
    private final Data data;

    char[] array;

    public CopyFile(Data data) {
        this.data = data;
    }
    @Override
    public void run(){
        array = new char[100];

        BufferedReader reader;
        try {
            System.out.println("Thread " + Thread.currentThread().getId() + " is running");
            // Read file
            File file = new File("inputfile.txt");
            reader = new BufferedReader(new FileReader(file));
            reader.read(array);

            System.out.println("Array:" + Arrays.toString(array));

            // Send data to other thread
            for (char array : array) {
                data.send(array);
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Exception is caught " + e);
        }
    }
}

class WriteFile implements Runnable {
    private final Data load;

    public WriteFile(Data data) {
        this.load = data;
    }

    @Override
    public void run() {
        try {
            System.out.println(
                    "Thread " + Thread.currentThread().getId() + " is running"
            );

            // Load data from other thread
            for(char receive = load.receive(); ' ' != 0; receive = load.receive()) {
                // Write to outputfile
                BufferedWriter writer =
                        new BufferedWriter(new FileWriter("outputfile.txt", true));
                writer.write(receive);

                writer.close();
                System.out.println("Got:" + receive);
            }
        } catch (Exception e) {
            System.out.println("Exception is caught " + e);
        }
    }
}

class Data {
    private char array;

    // To check if read or write should wait
    // True for read, false for write
    private boolean transfer = true;

    public synchronized void send(char array) {
        while (!transfer) {
            try {
                System.out.println("Read wait");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted" + e);
            }
        }
        transfer = false;

        this.array = array;
        notifyAll();
    }

    public synchronized char receive() {
        while (transfer) {
            try {
                System.out.println("Write wait");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Thread interrupted" + e);
            }
        }
        transfer = true;

        notifyAll();
        return array;
    }
}

