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
 * Part 3 also keeps parallel arrays of sent, disregarded, and stored messages.
 *
 * @author AxoleYokwana
 */
public class Message {

    // how many messages the arrays can hold
    private static final int MAX_MESSAGES = 100;

    // message details
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private String status;
    private String sender;

    // track total messages sent and list of sent messages
    private static int totalSentMessages = 0;
    private static List<Message> sentMessagesList = new ArrayList<>();

    // file used when a message is stored for later
    private static String jsonFilePath = "messages.json";

    // parallel arrays: the same index is the same message
    // example: messageIDs[0], messageHashes[0], and messageTexts[0] belong together
    private static String[] messageTexts = new String[MAX_MESSAGES];
    private static String[] messageIDs = new String[MAX_MESSAGES];
    private static String[] messageHashes = new String[MAX_MESSAGES];
    private static String[] messageRecipients = new String[MAX_MESSAGES];
    private static String[] messageSenders = new String[MAX_MESSAGES];
    private static String[] messageFlags = new String[MAX_MESSAGES];
    private static int messageCount = 0;

    // sent messages only
    private static String[] sentMessages = new String[MAX_MESSAGES];
    private static int sentCount = 0;

    // disregarded messages only
    private static String[] disregardedMessages = new String[MAX_MESSAGES];
    private static int disregardedCount = 0;

    // stored messages are filled by reading the JSON file
    private static String[] storedMessages = new String[MAX_MESSAGES];
    private static String[] storedMessageIDs = new String[MAX_MESSAGES];
    private static String[] storedMessageHashes = new String[MAX_MESSAGES];
    private static String[] storedRecipients = new String[MAX_MESSAGES];
    private static String[] storedSenders = new String[MAX_MESSAGES];
    private static int storedCount = 0;

    // default constructor
    public Message() {
        this.messageID = generateMessageID();
        this.messageNumber = 0;
        this.recipient = "";
        this.message = "";
        this.messageHash = "";
        this.status = "";
        this.sender = "";
    }

    // constructor with details
    public Message(String messageID, int messageNumber, String recipient, String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
        this.status = "";
        this.sender = "";
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
        if (option == 1) {
            this.status = "Sent";
            totalSentMessages++;
            sentMessagesList.add(this);
            addToParallelArrays();
            return "Message successfully sent.";
        } else if (option == 2) {
            this.status = "Disregard";
            addToParallelArrays();
            return "Press 0 to delete the message.";
        } else if (option == 3) {
            this.status = "Stored";
            addToParallelArrays();
            // save to the JSON file, then read that file back into the stored array
            storeMessage();
            loadStoredMessagesFromJson(jsonFilePath);
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

    // print the details of this message in required order:
    // Message ID, Message Hash, Recipient, Message
    public String printMessages() {
        if (this.messageID != null && !this.messageID.isEmpty()) {
            String details = "Message ID: " + this.messageID + "\n"
                    + "Message Hash: " + this.messageHash + "\n"
                    + "Recipient: " + this.recipient + "\n"
                    + "Message: " + this.message;
            return details;
        }
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
        messageCount = 0;
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
        jsonFilePath = "messages.json";
        clearArray(messageTexts);
        clearArray(messageIDs);
        clearArray(messageHashes);
        clearArray(messageRecipients);
        clearArray(messageSenders);
        clearArray(messageFlags);
        clearArray(sentMessages);
        clearArray(disregardedMessages);
        clearArray(storedMessages);
        clearArray(storedMessageIDs);
        clearArray(storedMessageHashes);
        clearArray(storedRecipients);
        clearArray(storedSenders);
    }

    // store the message in a json file
    //
    // Reference for writing JSON file in Java:
    // [1] Oracle, "Reading, Writing, and Creating Files," The Java Tutorials, 2022. [Online].
    // Available: https://docs.oracle.com/javase/tutorial/essential/io/file.html [Accessed: 28 Sep. 2026].
    //
    // Reference for JSON syntax:
    // [2] ECMA International, "The JSON Data Interchange Syntax," Standard ECMA-404, 2nd ed., Dec. 2017. [Online].
    // Available: https://www.ecma-international.org/publications-and-standards/standards/ecma-404/ [Accessed: 28 Sep. 2026].
    public void storeMessage() {
        storeMessage(jsonFilePath);
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
                + "    \"status\": \"" + escapeJson(status) + "\",\n"
                + "    \"sender\": \"" + escapeJson(sender) + "\"\n"
                + "  }";

        try {
            if (!file.exists() || file.length() == 0) {
                try (FileWriter writer = new FileWriter(file, false)) {
                    writer.write("[\n" + jsonEntry + "\n]");
                }
            } else {
                String content = new String(java.nio.file.Files.readAllBytes(file.toPath())).trim();
                // an empty list is written again as a new list
                if (content.equals("[]")) {
                    content = "[\n" + jsonEntry + "\n]";
                } else if (content.endsWith("]")) {
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
    private static String escapeJson(String text) {
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public static List<Message> getSentMessagesList() {
        return sentMessagesList;
    }

    public static void setJsonFilePath(String filePath) {
        jsonFilePath = filePath;
    }

    public static String[] getSentMessages() {
        return sentMessages;
    }

    public static int getSentCount() {
        return sentCount;
    }

    public static String[] getDisregardedMessages() {
        return disregardedMessages;
    }

    public static int getDisregardedCount() {
        return disregardedCount;
    }

    public static String[] getStoredMessages() {
        return storedMessages;
    }

    public static int getStoredCount() {
        return storedCount;
    }

    public static String[] getMessageIDs() {
        return messageIDs;
    }

    public static String[] getMessageHashes() {
        return messageHashes;
    }

    public static int getMessageCount() {
        return messageCount;
    }

    // put one empty string into every slot
    private static void clearArray(String[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    // copy this message into the parallel arrays
    private void addToParallelArrays() {
        if (messageCount >= MAX_MESSAGES) {
            System.out.println("The message list is full.");
            return;
        }

        if (this.messageHash == null || this.messageHash.equals("")) {
            createMessageHash();
        }

        messageTexts[messageCount] = this.message;
        messageIDs[messageCount] = this.messageID;
        messageHashes[messageCount] = this.messageHash;
        messageRecipients[messageCount] = this.recipient;
        if (this.sender == null) {
            messageSenders[messageCount] = "";
        } else {
            messageSenders[messageCount] = this.sender;
        }
        messageFlags[messageCount] = this.status;
        messageCount++;

        if (this.status.equals("Sent")) {
            if (sentCount < MAX_MESSAGES) {
                sentMessages[sentCount] = this.message;
                sentCount++;
            }
        } else if (this.status.equals("Disregard")) {
            if (disregardedCount < MAX_MESSAGES) {
                disregardedMessages[disregardedCount] = this.message;
                disregardedCount++;
            }
        }
    }

    // add a stored message that was read from the JSON file
    private static void addLoadedMessage(String id, String hash, String recipient, String text, String messageSender) {
        if (id == null) {
            id = "";
        }

        // do not add the same message twice
        for (int i = 0; i < messageCount; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(id) && id.equals("") == false) {
                return;
            }
        }

        if (messageCount >= MAX_MESSAGES) {
            return;
        }

        messageTexts[messageCount] = text;
        messageIDs[messageCount] = id;
        messageHashes[messageCount] = hash;
        messageRecipients[messageCount] = recipient;
        if (messageSender == null) {
            messageSenders[messageCount] = "";
        } else {
            messageSenders[messageCount] = messageSender;
        }
        messageFlags[messageCount] = "Stored";
        messageCount++;
    }

    // read one text value from a JSON object
    // example: "recipient": "+27838884567"
    private static String readJsonString(String block, String key) {
        String keyText = "\"" + key + "\"";
        int keyPos = block.indexOf(keyText);
        if (keyPos == -1) {
            return "";
        }

        int colon = block.indexOf(":", keyPos + keyText.length());
        if (colon == -1) {
            return "";
        }

        int firstQuote = block.indexOf("\"", colon + 1);
        if (firstQuote == -1) {
            return "";
        }

        // walk through the value and stop at the closing quote
        StringBuilder value = new StringBuilder();
        for (int i = firstQuote + 1; i < block.length(); i++) {
            char letter = block.charAt(i);
            if (letter == '\\' && i + 1 < block.length()) {
                char next = block.charAt(i + 1);
                value.append(next);
                i++;
            } else if (letter == '"') {
                break;
            } else {
                value.append(letter);
            }
        }
        return value.toString();
    }

    private static int findJsonObjectEnd(String content, int objectStart) {
        boolean inString = false;
        boolean escaped = false;

        for (int i = objectStart; i < content.length(); i++) {
            char character = content.charAt(i);
            if (inString) {
                if (escaped) {
                    escaped = false;
                } else if (character == '\\') {
                    escaped = true;
                } else if (character == '"') {
                    inString = false;
                }
            } else if (character == '"') {
                inString = true;
            } else if (character == '}') {
                return i;
            }
        }
        return -1;
    }

    // read the JSON file and copy each stored message into the stored arrays
    //
    // Reference for reading a file in Java:
    // [1] Oracle, "Reading, Writing, and Creating Files," The Java Tutorials, 2022. [Online].
    // Available: https://docs.oracle.com/javase/tutorial/essential/io/file.html [Accessed: 14 Nov. 2026].
    //
    // Reference for JSON syntax:
    // [2] ECMA International, "The JSON Data Interchange Syntax," Standard ECMA-404, 2nd ed., Dec. 2017. [Online].
    // Available: https://www.ecma-international.org/publications-and-standards/standards/ecma-404/ [Accessed: 14 Nov. 2026].
    public static void loadStoredMessagesFromJson() {
        loadStoredMessagesFromJson(jsonFilePath);
    }

    public static void loadStoredMessagesFromJson(String filePath) {
        clearArray(storedMessages);
        clearArray(storedMessageIDs);
        clearArray(storedMessageHashes);
        clearArray(storedRecipients);
        clearArray(storedSenders);
        storedCount = 0;

        java.io.File file = new java.io.File(filePath);
        if (file.exists() == false || file.length() == 0) {
            return;
        }

        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            int searchFrom = 0;

            // each stored message is one JSON object
            while (searchFrom < content.length()) {
                int idPos = content.indexOf("\"messageID\"", searchFrom);
                if (idPos == -1) {
                    break;
                }

                int blockStart = content.lastIndexOf("{", idPos);
                if (blockStart == -1) {
                    break;
                }
                int blockEnd = findJsonObjectEnd(content, blockStart);
                if (blockEnd == -1) {
                    break;
                }

                String block = content.substring(blockStart, blockEnd + 1);
                String id = readJsonString(block, "messageID");
                String hash = readJsonString(block, "messageHash");
                String recipientCell = readJsonString(block, "recipient");
                String text = readJsonString(block, "message");
                String messageSender = readJsonString(block, "sender");
                String messageStatus = readJsonString(block, "status");

                if (storedCount < MAX_MESSAGES) {
                    storedMessages[storedCount] = text;
                    storedMessageIDs[storedCount] = id;
                    storedMessageHashes[storedCount] = hash;
                    storedRecipients[storedCount] = recipientCell;
                    storedSenders[storedCount] = messageSender;
                    storedCount++;
                }

                // keep the parallel arrays in step with the file
                if (messageStatus.equals("Stored") || messageStatus.equals("")) {
                    addLoadedMessage(id, hash, recipientCell, text, messageSender);
                }

                searchFrom = blockEnd + 1;
            }
        } catch (IOException e) {
            System.out.println("Error reading stored messages: " + e.getMessage());
        }
    }

    // write every stored message back to the JSON file
    private static void saveAllStoredMessages() {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < storedCount; i++) {
            if (i > 0) {
                json.append(",\n");
            }
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(escapeJson(storedMessageIDs[i])).append("\",\n");
            json.append("    \"messageNumber\": 0,\n");
            json.append("    \"messageHash\": \"").append(escapeJson(storedMessageHashes[i])).append("\",\n");
            json.append("    \"recipient\": \"").append(escapeJson(storedRecipients[i])).append("\",\n");
            json.append("    \"message\": \"").append(escapeJson(storedMessages[i])).append("\",\n");
            json.append("    \"status\": \"Stored\",\n");
            json.append("    \"sender\": \"").append(escapeJson(storedSenders[i])).append("\"\n");
            json.append("  }");
        }

        json.append("\n]");

        try (FileWriter writer = new FileWriter(jsonFilePath, false)) {
            writer.write(json.toString());
        } catch (IOException e) {
            System.out.println("Error saving messages to JSON: " + e.getMessage());
        }
    }

    // show the sender and recipient of every stored message
    public String displaySenderAndRecipient() {
        if (storedCount == 0) {
            return "No stored messages.";
        }

        String text = "";
        for (int i = 0; i < storedCount; i++) {
            String senderName = storedSenders[i];
            if (senderName == null || senderName.equals("")) {
                senderName = "Unknown";
            }
            text = text + "Sender: " + senderName + "\n";
            text = text + "Recipient: " + storedRecipients[i] + "\n\n";
        }
        return text.trim();
    }

    // return the stored message with the most characters
    public String displayLongestStoredMessage() {
        if (storedCount == 0) {
            return "No stored messages.";
        }

        String longest = storedMessages[0];
        if (longest == null) {
            longest = "";
        }

        for (int i = 1; i < storedCount; i++) {
            if (storedMessages[i] != null && storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        return longest;
    }

    // find the index of a message ID, or -1 when it is not there
    private int findMessageIndexByID(String messageID) {
        if (messageID == null) {
            return -1;
        }
        String id = messageID.trim();
        for (int i = 0; i < messageCount; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(id)) {
                return i;
            }
        }
        return -1;
    }

    // search for a message ID and return the message text
    public String searchByMessageID(String messageID) {
        int index = findMessageIndexByID(messageID);
        if (index == -1) {
            return "Message ID not found.";
        }
        return messageTexts[index];
    }

    // search for a message ID and show the recipient and the message
    public String searchByMessageIDDetails(String messageID) {
        int index = findMessageIndexByID(messageID);
        if (index == -1) {
            return "Message ID not found.";
        }
        return "Recipient: " + messageRecipients[index] + "\nMessage: " + messageTexts[index];
    }

    // find every sent or stored message for one recipient
    public String searchByRecipient(String recipient) {
        if (recipient == null) {
            return "No messages found for that recipient.";
        }

        String cell = recipient.trim();
        String result = "";

        for (int i = 0; i < messageCount; i++) {
            boolean sentOrStored = messageFlags[i] != null
                    && (messageFlags[i].equals("Sent") || messageFlags[i].equals("Stored"));
            if (sentOrStored && messageRecipients[i] != null && messageRecipients[i].equals(cell)) {
                if (result.equals("") == false) {
                    result = result + " ";
                }
                result = result + "\"" + messageTexts[i] + "\"";
            }
        }

        if (result.equals("")) {
            return "No messages found for that recipient.";
        }
        return result;
    }

    // delete one message by its hash and remove it from the arrays
    public String deleteMessageByHash(String hash) {
        if (hash == null) {
            return "Message hash not found.";
        }

        String hashText = hash.trim();
        for (int i = 0; i < messageCount; i++) {
            if (hashText.equals(messageHashes[i])) {
                String deletedMessage = messageTexts[i];
                String flag = messageFlags[i];
                removeMainAt(i);

                if (flag != null && flag.equals("Sent")) {
                    sentCount = removeFirstText(sentMessages, sentCount, deletedMessage);
                    for (int n = 0; n < sentMessagesList.size(); n++) {
                        if (hashText.equals(sentMessagesList.get(n).getMessageHash())) {
                            sentMessagesList.remove(n);
                            break;
                        }
                    }
                } else if (flag != null && flag.equals("Disregard")) {
                    disregardedCount = removeFirstText(disregardedMessages, disregardedCount, deletedMessage);
                } else if (flag != null && flag.equals("Stored")) {
                    removeStoredByHash(hashText);
                    saveAllStoredMessages();
                }

                return "Message: \"" + deletedMessage + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // shift the parallel arrays left to remove one index
    private static void removeMainAt(int index) {
        for (int j = index; j < messageCount - 1; j++) {
            messageTexts[j] = messageTexts[j + 1];
            messageIDs[j] = messageIDs[j + 1];
            messageHashes[j] = messageHashes[j + 1];
            messageRecipients[j] = messageRecipients[j + 1];
            messageSenders[j] = messageSenders[j + 1];
            messageFlags[j] = messageFlags[j + 1];
        }

        int last = messageCount - 1;
        messageTexts[last] = null;
        messageIDs[last] = null;
        messageHashes[last] = null;
        messageRecipients[last] = null;
        messageSenders[last] = null;
        messageFlags[last] = null;
        messageCount--;
    }

    // remove the first matching text from a simple array
    private static int removeFirstText(String[] list, int count, String value) {
        for (int i = 0; i < count; i++) {
            if (value != null && value.equals(list[i])) {
                for (int j = i; j < count - 1; j++) {
                    list[j] = list[j + 1];
                }
                list[count - 1] = null;
                return count - 1;
            }
        }
        return count;
    }

    // remove one stored message by hash, then the caller rewrites the JSON file
    private static void removeStoredByHash(String hash) {
        for (int i = 0; i < storedCount; i++) {
            if (hash.equals(storedMessageHashes[i])) {
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                    storedMessageIDs[j] = storedMessageIDs[j + 1];
                    storedMessageHashes[j] = storedMessageHashes[j + 1];
                    storedRecipients[j] = storedRecipients[j + 1];
                    storedSenders[j] = storedSenders[j + 1];
                }
                int last = storedCount - 1;
                storedMessages[last] = null;
                storedMessageIDs[last] = null;
                storedMessageHashes[last] = null;
                storedRecipients[last] = null;
                storedSenders[last] = null;
                storedCount--;
                return;
            }
        }
    }

    // sent messages joined together for the unit test
    public String displaySentMessages() {
        if (sentCount == 0) {
            return "No sent messages.";
        }

        String result = "";
        for (int i = 0; i < sentCount; i++) {
            if (i > 0) {
                result = result + ", ";
            }
            result = result + "\"" + sentMessages[i] + "\"";
        }
        return result;
    }

    // report of every sent message: hash, recipient, and message
    public String displayReport() {
        String text = "";
        for (int i = 0; i < messageCount; i++) {
            if (messageFlags[i] != null && messageFlags[i].equals("Sent")) {
                text = text + "Message Hash: " + messageHashes[i] + "\n";
                text = text + "Recipient: " + messageRecipients[i] + "\n";
                text = text + "Message: " + messageTexts[i] + "\n\n";
            }
        }

        if (text.equals("")) {
            return "No sent messages.";
        }
        return text.trim();
    }

    // full details of every stored message
    public String displayStoredReport() {
        if (storedCount == 0) {
            return "No stored messages.";
        }

        String text = "";
        for (int i = 0; i < storedCount; i++) {
            text = text + "Message ID: " + storedMessageIDs[i] + "\n";
            text = text + "Message Hash: " + storedMessageHashes[i] + "\n";
            text = text + "Recipient: " + storedRecipients[i] + "\n";
            text = text + "Message: " + storedMessages[i] + "\n\n";
        }
        return text.trim();
    }
}
