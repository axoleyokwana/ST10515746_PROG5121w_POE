/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.myquickchat;

import java.util.Scanner;

/**
 * Part 1: the user registers and then logs in.
 *
 * @author AxoleYokwana
 */
public class MyQuickChat {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();

        System.out.println("Registration");

        // user enters first name
        System.out.print("Enter first name: ");
        String firstName = input.nextLine();
        login.setFirstName(firstName);

        // user enters last name 
        System.out.print("Enter last name: ");
        String lastName = input.nextLine();
        login.setLastName(lastName);

        // user enters username
        System.out.print("Enter username: ");
        String username = input.nextLine();

        // user enters password
        System.out.print("Enter password: ");
        String password = input.nextLine();

        // user enters cell phone number
        System.out.print("Enter cell phone number: ");
        String cellPhoneNumber = input.nextLine();

        // show the registration messages
        System.out.println(login.registerUser(username, password, cellPhoneNumber));

        System.out.println();
        System.out.println("Login");

        // user enters username
        System.out.print("Enter username: ");
        String loginUsername = input.nextLine();

        // user enters password
        System.out.print("Enter password: ");
        String loginPassword = input.nextLine();

        // show if the login worked
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));

        input.close(); 
    }
}
