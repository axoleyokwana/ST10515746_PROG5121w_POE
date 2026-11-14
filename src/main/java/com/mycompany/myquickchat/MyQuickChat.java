/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.myquickchat;

import java.util.Scanner;

/**
 * Main application for QuickChat.
 * Part 1 handles user registration and login.
 * Part 2 handles sending messages, hashing, validating, and the numeric menu.
 * Part 3 stores messages in arrays and lets the user search, delete, and view a report.
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

        // Part 2: user can only send messages if login was successful
        if (login.loginUser(loginUsername, loginPassword) == true) {
            System.out.println();
            System.out.println("Welcome to QuickChat.");

            boolean running = true;
            Message messageHandler = new Message();

            // while loop runs until the user selects quit
            while (running == true) {
                System.out.println();
                System.out.println("Please choose an option:");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");
                System.out.print("Enter choice (1-4): ");

                String choice = input.nextLine().trim();

                if (choice.equals("1")) {
                    // user defines how many messages to enter
                    System.out.print("How many messages would you like to enter? ");
                    int numMessages = 0;
                    try {
                        numMessages = Integer.parseInt(input.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Returning to menu.");
                        continue;
                    }

                    // for loop to enter the set number of messages
                    for (int i = 0; i < numMessages; i++) {
                        System.out.println();
                        System.out.println("--- Entering Message " + (i + 1) + " of " + numMessages + " ---");

                        Message msg = new Message();
                        // message number created in loop using loop counter
                        msg.setMessageNumber(i);

                        // autogenerate message id and display status
                        String messageID = msg.generateMessageID();
                        System.out.println(msg.returnMessageIDStatus());

                        // prompt for recipient cell number and validate
                        String recipient = "";
                        boolean validRecipient = false;
                        while (validRecipient == false) {
                            System.out.print("Enter recipient cell number (e.g. +27...): ");
                            recipient = input.nextLine().trim();
                            String recipientStatus = msg.checkRecipientCell(recipient);
                            System.out.println(recipientStatus);
                            if (msg.checkCellPhoneNumber(recipient) == true) {
                                validRecipient = true;
                                msg.setRecipient(recipient);
                            }
                        }

                        // prompt for message and validate length
                        String text = "";
                        boolean validMessage = false;
                        while (validMessage == false) {
                            System.out.print("Enter message (max 250 characters): ");
                            text = input.nextLine();
                            String lengthStatus = msg.checkMessageLength(text);
                            System.out.println(lengthStatus);
                            if (text.length() <= 250) {
                                validMessage = true;
                                msg.setMessage(text);
                                // the logged in person is the sender
                                msg.setSender(firstName + " " + lastName);
                            }
                        }

                        // autogenerate message hash
                        String hash = msg.createMessageHash();
                        System.out.println("Generated Message Hash: " + hash);

                        // prompt user to send, disregard, or store
                        System.out.println("Choose an action for this message:");
                        System.out.println("1) Send Message");
                        System.out.println("2) Disregard Message");
                        System.out.println("3) Store Message");
                        System.out.print("Enter choice (1-3): ");
                        String actionChoice = input.nextLine().trim();

                        int action = 1;
                        try {
                            action = Integer.parseInt(actionChoice);
                        } catch (NumberFormatException e) {
                            action = 1;
                        }

                        // handle sending option
                        String sentStatus = msg.SentMessage(action);
                        System.out.println(sentStatus);

                        // display full details of the message in required order:
                        // Message ID, Message Hash, Recipient, Message
                        System.out.println();
                        System.out.println("Message Details:");
                        System.out.println(msg.printMessages());
                    }

                    // total messages sent accumulated and displayed after loop
                    System.out.println();
                    System.out.println("Total messages sent: " + messageHandler.returnTotalMessagess());

                } else if (choice.equals("2")) {
                    // feature still in development
                    System.out.println("Coming Soon.");
                } else if (choice.equals("3")) {
                    // quit the application
                    System.out.println("Goodbye!");
                    running = false;
                } else if (choice.equals("4")) {
                    // Part 3: search, delete, and report stored messages
                    showStoredMessagesMenu(input, messageHandler);
                } else {
                    System.out.println("Invalid option. Please enter 1, 2, 3, or 4.");
                }
            }
        }

        input.close();
    }

    // Part 3 menu. The user can view, search, and delete stored messages.
    private static void showStoredMessagesMenu(Scanner input, Message messageHandler) {
        // read the JSON file into the stored messages array
        Message.loadStoredMessagesFromJson();

        boolean inMenu = true;
        while (inMenu == true) {
            System.out.println();
            System.out.println("Stored Messages");
            System.out.println("1) Display the sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all the messages stored for a particular recipient");
            System.out.println("5) Delete a message using the message hash");
            System.out.println("6) Display a report that lists the full details of all the stored messages");
            System.out.println("7) Back");
            System.out.print("Enter choice (1-7): ");

            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                System.out.println(messageHandler.displaySenderAndRecipient());
            } else if (choice.equals("2")) {
                System.out.println(messageHandler.displayLongestStoredMessage());
            } else if (choice.equals("3")) {
                System.out.print("Enter the message ID: ");
                String messageID = input.nextLine().trim();
                System.out.println(messageHandler.searchByMessageIDDetails(messageID));
            } else if (choice.equals("4")) {
                System.out.print("Enter the recipient cell number: ");
                String recipient = input.nextLine().trim();
                System.out.println(messageHandler.searchByRecipient(recipient));
            } else if (choice.equals("5")) {
                System.out.print("Enter the message hash: ");
                String hash = input.nextLine().trim();
                System.out.println(messageHandler.deleteMessageByHash(hash));
            } else if (choice.equals("6")) {
                System.out.println(messageHandler.displayStoredReport());
            } else if (choice.equals("7")) {
                inMenu = false;
            } else {
                System.out.println("Invalid option. Please enter a number from 1 to 7.");
            }
        }
    }
}
