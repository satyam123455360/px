package com.uniq.phoenix.interfacecli;

import com.uniq.phoenix.core.PhoenixEngine;
import java.util.Scanner;

public class PhoenixCLI {

    public static void main(String[] args) {

        Scanner ip = new Scanner(System.in);
        PhoenixEngine pe = new PhoenixEngine();

        System.out.println("Phoenix v0 initialized.");
        System.out.println("Type 'help' to see commands.");

        while (true) {

            System.out.print("\nYou: ");
            String ninp = ip.nextLine();

            String response = pe.process(ninp);

            System.out.println("Phoenix: " + response);

            // Exit condition
            if (ninp.trim().equalsIgnoreCase("exit")) {
                break;
            }
        }

        ip.close();
        System.out.println("Program terminated.");
    }
}
