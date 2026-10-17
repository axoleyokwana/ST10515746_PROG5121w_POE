/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myquickchat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class handles message creation, validation, hashing, sending, and storage.
 *
 * @author AxoleYokwana
 */
public class Message {

    // message details
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String status;

    // track total messages sent and list of sent messages
    private static int totalSentMessages = 0;
    private static List<Message> sentMessagesList = new ArrayList<>();

    // default constructor
    public Message() {
        this.messageID = generateMessageID();
        this.messageNumber = 0;
        this.recipient = "";
        this.message = "";
        this.messageHash = "";
        this.status = "";
    }

    // constructor with details
    public Message(String messageID, int messageNumber, String recipient, String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
        this.status = "";
    }

    // generate a random ten digit message id using loop counter and string manipulation
    public String generateMessageID() {
        return generateMessageID(this.messageNumber);
    }

    // generate ten digit message id using loop counter and random numbers
    public String generateMessageID(int messageNumber) {
        String prefix = String.format("%02d", messageNumber % 100);
        Random random = new Random();
        long randomPart = 10000000L + (long) (random.nextDouble() * 90000000L);
        String generated = (prefix + randomPart).substring(0, 10);
        this.messageID = generated;
        return this.messageID;
    }

    // message id generated status message
    public String returnMessageIDStatus() {
        return "Message ID generated: " + this.messageID;
    }

    // alias for message id status
    public String getMessageIDStatus() {
        return returnMessageIDStatus();
    }

    // check if the message id is not more than ten characters
    public boolean checkMessageID() {
        if (this.messageID != null && this.messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    // check if a given message id is not more than ten characters
    public boolean checkMessageID(String messageID) {
        if (messageID != null && messageID.length() <= 10) {
            return true;
        } else {
            return false;
        }
    }

    // check if the cell phone number is valid
    // number must start with international code and be followed by digits
    //
    // Reference for regular expression:
    // [1] Oracle, "Regular Expressions," The Java Tutorials, 2022. [Online].
    // Available: https://docs.oracle.com/javase/tutorial/essential/regex/ [Accessed: 28 Sep. 2026].
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        String pattern = "^\\+27[0-9]{1,10}$";
        if (cellPhoneNumber != null && cellPhoneNumber.matches(pattern)) {
            return true;
        } else {
            return false;
        }
    }

    // check recipient cell phone number and return the status message
    public String checkRecipientCell() {
        return checkRecipientCell(this.recipient);
    }

    // check recipient cell phone number with parameter and return status message
    public String checkRecipientCell(String recipientCell) {
        if (checkCellPhoneNumber(recipientCell) == true) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // check if the message length does not exceed 250 characters
    public String checkMessageLength() {
        return checkMessageLength(this.message);
    }

    // check if a given message length does not exceed 250 characters
    public String checkMessageLength(String message) {
        if (message == null) {
            return "Message ready to send.";
        }

        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    // create and return the message hash
    // contains first two numbers of message ID, a colon (:), message number, colon (:),
    // and first and last words in all caps
    public String createMessageHash() {
        return createMessageHash(this.messageID, this.messageNumber, this.message);
    }

    // create message hash with parameters using string manipulation
    public String createMessageHash(String messageID, int messageNumber, String message) {
        // first two characters of message id using substring
        String idPrefix = "00";
        if (messageID != null && messageID.length() >= 2) {
            idPrefix = messageID.substring(0, 2);
        } else if (messageID != null) {
            idPrefix = messageID;
        }

        // clean and split message into words
        String wordPart = "";
        if (message != null && !message.trim().isEmpty()) {
            String[] words = message.trim().split("\\s+");
            String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
            String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");

            wordPart = (firstWord + lastWord).toUpperCase();
        }

        String hash = idPrefix + ":" + messageNumber + ":" + wordPart;
        this.messageHash = hash;
        return hash;
    }

    // allow user to choose to send, disregard, or store the message
    public String SentMessage() {
        return SentMessage(1);
    }

    // allow user to choose to send, disregard, or store the message by option number
    public String SentMessage(int option) {
        return SentMessage(option, "messages.json");
    }

    // allow callers to choose a storage path when storing a message
    public String SentMessage(int option, String filePath) {
        if (option == 1) {
            this.status = "Sent";
            totalSentMessages++;
            sentMessagesList.add(this);
            return "Message successfully sent.";
        } else if (option == 2) {
            this.status = "Disregard";
            return "Press 0 to delete the message.";
        } else if (option == 3) {
            this.status = "Stored";
            storeMessage(filePath);
            return "Message successfully stored.";
        } else {
            return "Invalid option selected.";
        }
    }

    // allow user to choose option using string text
    public String SentMessage(String option) {
        if (option == null) {
            return "Invalid option selected.";
        }
        String opt = option.trim().toLowerCase();
        if (opt.equals("1") || opt.contains("send")) {
            return SentMessage(1);
        } else if (opt.equals("2") || opt.contains("disregard") || opt.contains("discard") || opt.contains("delete")) {
            return SentMessage(2);
        } else if (opt.equals("3") || opt.contains("store")) {
            return SentMessage(3);
        }
        return "Invalid option selected.";
    }

    // alias for lowercase sentMessage
    public String sentMessage(int option) {
        return SentMessage(option);
    }

    // alias for lowercase sentMessage with string option
    public String sentMessage(String option) {
        return SentMessage(option);
    }

    // return details of this message in the required order
    public String printMessageDetails() {
        return "Message ID: " + this.messageID + "\n"
                + "Message Hash: " + this.messageHash + "\n"
                + "Recipient: " + this.recipient + "\n"
                + "Message: " + this.message;
    }

    // print details of all messages sent while running
    public String printMessages() {
        return printAllSentMessages();
    }

    // print details of all messages sent while running
    public static String printAllSentMessages() {
        if (sentMessagesList.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessagesList.size(); i++) {
            Message m = sentMessagesList.get(i);
            sb.append("Message ID: ").append(m.getMessageID()).append("\n")
              .append("Message Hash: ").append(m.getMessageHash()).append("\n")
              .append("Recipient: ").append(m.getRecipient()).append("\n")
              .append("Message: ").append(m.getMessage()).append("\n\n");
        }
        return sb.toString().trim();
    }

    // return the total number of messages sent
    public int returnTotalMessagess() {
        return totalSentMessages;
    }

    // alias with standard spelling
    public int returnTotalMessages() {
        return totalSentMessages;
    }

    // reset the total count of sent messages (useful for unit tests)
    public static void resetTotalMessages() {
        totalSentMessages = 0;
        sentMessagesList.clear();
    }

    // store the message in a json file
    //
    // Reference for writing JSON file in Java:
    // [1] Oracle, "Reading, Writing, and Creating Files," The Java Tutorials, 2022. [Online].
    // Available: https://docs.oracle.com/javase/tutorial/essential/io/file.html [Accessed: 28 Sep. 2026].
    //
    // Reference for JSON syntax:
    // [2] ECMA International, "The JSON Data Interchange Syntax," Standard ECMA-404, 2nd ed., Dec. 2017.
    // [Online]. Available: https://www.ecma-international.org/publications-and-standards/standards/ecma-404/ [Accessed: 28 Sep. 2026].
    public void storeMessage() {
        storeMessage("messages.json");
    }

    // store the message to a specific json file path as a json array
    public void storeMessage(String filePath) {
        java.io.File file = new java.io.File(filePath);
        String jsonEntry = "  {\n"
                + "    \"messageID\": \"" + escapeJson(messageID) + "\",\n"
                + "    \"messageNumber\": " + messageNumber + ",\n"
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(message) + "\",\n"
                + "    \"status\": \"" + escapeJson(status) + "\"\n"
                + "  }";

        try {
            if (!file.exists() || file.length() == 0) {
                try (FileWriter writer = new FileWriter(file, false)) {
                    writer.write("[\n" + jsonEntry + "\n]");
                }
            } else {
                String content = new String(java.nio.file.Files.readAllBytes(file.toPath())).trim();
                if (content.endsWith("]")) {
                    content = content.substring(0, content.length() - 1).trim();
                    if (!content.equals("[")) {
                        content = content + ",\n";
                    }
                    content = content + jsonEntry + "\n]";
                } else {
                    content = "[\n" + jsonEntry + "\n]";
                }
                try (FileWriter writer = new FileWriter(file, false)) {
                    writer.write(content);
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
        }
    }

    // helper method to escape json characters
    private String escapeJson(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // getters and setters
    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public void setMessageHash(String messageHash) {
        this.messageHash = messageHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<Message> getSentMessagesList() {
        return sentMessagesList;
    }
}