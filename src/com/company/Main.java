package com.company;

import java.io.*;

class CopyFile implements Runnable {
    @Override
    public void run(){
        copyFile();
    }

    public char[] copyFile() {
        char[] array = new char[100];
        BufferedReader reader = null;
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
    }
}


class WriteFile implements Runnable {
    @Override
    public void run() {
        writeFile();
    }

    public void writeFile() {
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
    }
}

public class Main {

    public static void main(String[] args) {
        // Start read thread
        Thread readFile = new Thread(new CopyFile());
        readFile.start();
        // Start write thread
        Thread writeFile = new Thread(new WriteFile());
        writeFile.start();
        // copy();
    }
}