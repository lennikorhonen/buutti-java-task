package com.company;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        BufferedReader reader;
        try {

            FileReader file = new FileReader("testfile.txt");

            String strCurrentLine;
            reader = new BufferedReader(file);

            while ((strCurrentLine = reader.readLine()) != null) {
                System.out.println(strCurrentLine);

                BufferedWriter writer = new BufferedWriter(new FileWriter("outputfile.txt", true));
                writer.append(' ');
                writer.append(strCurrentLine);

                writer.close();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error occurred");
            e.printStackTrace();
        }
    }
}