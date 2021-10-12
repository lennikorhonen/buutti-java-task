package com.company;

import java.io.*;
import java.util.concurrent.Exchanger;

public class Main {

    public static void main(String[] args) {
        Exchanger<char[]> exchanger = new Exchanger<>();
        // Start read thread
        Thread readFile = new Thread(new CopyFile(exchanger));
        readFile.start();
        // Start write thread
        Thread writeFile = new Thread(new WriteFile(exchanger));
        writeFile.start();
    }
}

class CopyFile implements Runnable {
    /* private Exchanger<char[]> exchanger;
    char[] array;

    public CopyFile(char[] array, Exchanger<char[]> exchanger) {
        this.exchanger = exchanger;
        this.array = array;
    }*/

    Exchanger ex;
    CopyFile(Exchanger<char[]> ex) { this.ex = ex; }
    char[] array = new char[50];

    @Override
    public void run(){
        for(int i = 0; i < 2; i++) {
            BufferedReader reader;
            try {
                System.out.println(
                        "Thread " + Thread.currentThread().getId() + " is running"
                );
                File file = new File("testfile.txt");
                reader = new BufferedReader(new FileReader(file));
                reader.read(array);
                System.out.println("Array:" + array);
                array = (char[]) ex.exchange(array);
            } catch (Exception e) {
                System.out.println("Exception is caught " + e);
            }
        }
    }

    /*public char[] copyFile() {
        BufferedReader reader;
        try {
            System.out.println(
                    "Thread " + Thread.currentThread().getId() + " is running"
            );
            File file = new File("testfile.txt");

            reader = new BufferedReader(new FileReader(file));
            reader.read(array);
            System.out.println(array);
        }
        catch (Exception e) {
            System.out.println("Exception is caught");
        }
        return array;
    }*/
}

class WriteFile implements Runnable {
    /*private Exchanger<char[]> exchanger;
    char[] array;

    public WriteFile(char[] array, Exchanger<char[]> exchanger) {
        this.array = array;
        this.exchanger = exchanger;
    }*/
    Exchanger<char[]> ex;
    char[] array;

    WriteFile(Exchanger<char[]> ex) { this.ex = ex; }


    @Override
    public void run() {
        for(int i = 0; i < 2; i++) {
            try {
                System.out.println(
                        "Thread " + Thread.currentThread().getId() + " is running"
                );
                array = ex.exchange(new char[50]);
                BufferedWriter writer = new BufferedWriter(new FileWriter("outputfile.txt", true));
                writer.write(array);
                writer.append(' ');

                writer.close();
                System.out.println("Got:" + array);
            } catch (Exception e) {
                System.out.println("Exception is caught " + e);
            }
        }
    }

    /*public void writeFile() {
        try {
            System.out.println(
                    "Thread " + Thread.currentThread().getId() + " is running"
            );
            CopyFile reader = new CopyFile();
            char[] array = reader.copyFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter("outputfile.txt", true));
            writer.write(array);
            writer.append(' ');

            writer.close();
        }  catch (Exception e) {
            System.out.println("Exception is caught" + e);
        }
    }*/
}
