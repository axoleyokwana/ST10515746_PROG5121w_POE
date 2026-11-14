/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.myquickchat.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Part 2 message creation, validation, hashing, and sending,
 * and for the Part 3 array, search, delete, and report tests.
 *
 * @author AxoleYokwana
 */
public class MessageTest {

    @org.junit.jupiter.api.io.TempDir
    java.nio.file.Path tempDirectory;

    @BeforeEach
    public void setUp() {
        Message.resetTotalMessages();
        Message.setJsonFilePath(tempDirectory.resolve("messages.json").toString());
    }

    // message length is under 250 characters (success)
    @Test
    public void testMessageLengthSuccess() {
        Message msg = new Message();
        String validMessage = "Hi Mike, can you join us for dinner tonight?";
        String expected = "Message ready to send.";
        String actual = msg.checkMessageLength(validMessage);
        assertEquals(expected, actual);
    }

    // message length exceeds 250 characters (failure)
    @Test
    public void testMessageLengthFailure() {
        Message msg = new Message();
        // create a message with 260 characters (exceeds by 10)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            sb.append("a");
        }
        String longMessage = sb.toString();

        String expected = "Message exceeds 250 characters by 10; please reduce the size.";
        String actual = msg.checkMessageLength(longMessage);
        assertEquals(expected, actual);
    }

    // recipient number is correctly formatted (+27718693002)
    @Test
    public void testRecipientCellSuccess() {
        Message msg = new Message();
        String expected = "Cell phone number successfully captured.";
        String actual = msg.checkRecipientCell("+27718693002");
        assertEquals(expected, actual);
    }

    // recipient number is incorrectly formatted (08575975889)
    @Test
    public void testRecipientCellFailure() {
        Message msg = new Message();
        String expected = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        String actual = msg.checkRecipientCell("08575975889");
        assertEquals(expected, actual);
    }

    // message hash is correctly created for test case 1
    @Test
    public void testMessageHashTestCase1() {
        Message msg = new Message();
        String hash = msg.createMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight?");
        String expected = "00:0:HITONIGHT";
        assertEquals(expected, hash);
    }

    // test remainder of message hashes in a loop
    @Test
    public void testMessageHashesInLoop() {
        String[] ids = {"0012345678", "0098765432", "0055555555"};
        int[] counters = {0, 1, 2};
        String[] messages = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?",
            "Hello world thanks"
        };
        String[] expectedHashes = {
            "00:0:HITONIGHT",
            "00:1:HIPAYMENT",
            "00:2:HELLOTHANKS"
        };

        Message msg = new Message();
        for (int i = 0; i < messages.length; i++) {
            String actualHash = msg.createMessageHash(ids[i], counters[i], messages[i]);
            assertEquals(expectedHashes[i], actualHash);
        }
    }

    // message id is created and returns formatted string
    @Test
    public void testMessageIDIsCreated() {
        Message msg = new Message();
        String id = msg.generateMessageID();
        assertNotNull(id);
        assertEquals(10, id.length());
        assertTrue(msg.checkMessageID());

        String expected = "Message ID generated: " + id;
        String actual = msg.returnMessageIDStatus();
        assertEquals(expected, actual);
    }

    // check message ID length valid and invalid
    @Test
    public void testCheckMessageID() {
        Message msg = new Message();
        assertTrue(msg.checkMessageID("1234567890"));
        assertFalse(msg.checkMessageID("12345678901"));
    }

    // user selected 1: send message
    @Test
    public void testMessageSentSend() {
        Message msg = new Message();
        String expected = "Message successfully sent.";
        String actual = msg.SentMessage(1);
        assertEquals(expected, actual);
    }

    // user selected 2: disregard message
    @Test
    public void testMessageSentDisregard() {
        Message msg = new Message();
        String expected = "Press 0 to delete the message.";
        String actual = msg.SentMessage(2);
        assertEquals(expected, actual);
    }

    // user selected 3: store message
    @Test
    public void testMessageSentStore() {
        Message msg = new Message();
        String expected = "Message successfully stored.";
        String actual = msg.SentMessage(3);
        assertEquals(expected, actual);
    }

    // check total messages sent count
    @Test
    public void testReturnTotalMessagess() {
        Message msg1 = new Message();
        msg1.SentMessage(1); // send

        Message msg2 = new Message();
        msg2.SentMessage(2); // disregard

        Message msg3 = new Message();
        msg3.SentMessage(1); // send

        assertEquals(2, msg1.returnTotalMessagess());
    }

    // print messages details in correct order
    @Test
    public void testPrintMessagesOrder() {
        Message msg = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String details = msg.printMessages();

        assertTrue(details.contains("Message ID: 0012345678"));
        assertTrue(details.contains("Message Hash: 00:0:HITONIGHT"));
        assertTrue(details.contains("Recipient: +27718693002"));
        assertTrue(details.contains("Message: Hi Mike, can you join us for dinner tonight?"));

        int idIndex = details.indexOf("Message ID");
        int hashIndex = details.indexOf("Message Hash");
        int recipientIndex = details.indexOf("Recipient");
        int messageIndex = details.indexOf("Message:");

        assertTrue(idIndex < hashIndex);
        assertTrue(hashIndex < recipientIndex);
        assertTrue(recipientIndex < messageIndex);
    }

    // verify message is successfully stored in a JSON file
    @Test
    public void testStoreMessageInJsonFile() {
        String testFile = tempDirectory.resolve("test_messages.json").toString();
        java.io.File file = new java.io.File(testFile);

        Message msg = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg.setStatus("Stored");
        msg.storeMessage(testFile);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);

    }

    // Part 3 test data from the assignment
    private static final String MESSAGE_1 = "Did you get the cake?";
    private static final String MESSAGE_2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String MESSAGE_3 = "Yohoooo, I am at your gate.";
    private static final String MESSAGE_4 = "It is dinner time !";
    private static final String MESSAGE_5 = "Ok, I am leaving without you.";

    // add one test message by sending, storing, or disregarding it
    private Message addMessage(String messageID, int messageNumber, String recipient, String text, int option) {
        Message msg = new Message(messageID, messageNumber, recipient, text);
        msg.setSender("Axole Yokwana");
        msg.SentMessage(option);
        return msg;
    }

    // messages 1 to 4
    private void addMessagesOneToFour() {
        addMessage("1111111111", 0, "+27834557896", MESSAGE_1, 1);
        addMessage("2222222222", 1, "+27838884567", MESSAGE_2, 3);
        addMessage("3333333333", 2, "+27834484567", MESSAGE_3, 2);
        // the Developer value for message 4 is the message ID used by the search test
        addMessage("0838884567", 3, "0838884567", MESSAGE_4, 1);
    }

    // sent messages array contains the expected test data for messages 1 to 4
    @Test
    public void testSentMessagesArrayPopulated() {
        addMessagesOneToFour();

        Message msg = new Message();
        String expected = "\"Did you get the cake?\", \"It is dinner time !\"";
        String actual = msg.displaySentMessages();
        assertEquals(expected, actual);
        assertEquals("Did you get the cake?", Message.getSentMessages()[0]);
        assertEquals("It is dinner time !", Message.getSentMessages()[1]);
    }

    // longest message from messages 1 to 4
    @Test
    public void testDisplayLongestMessage() {
        addMessagesOneToFour();

        Message msg = new Message();
        String expected = "Where are you? You are late! I have asked you to be on time.";
        String actual = msg.displayLongestStoredMessage();
        assertEquals(expected, actual);
    }

    // search for message 4 using message ID 0838884567
    @Test
    public void testSearchForMessageID() {
        addMessagesOneToFour();

        Message msg = new Message();
        String expected = "It is dinner time !";
        String actual = msg.searchByMessageID("0838884567");
        assertEquals(expected, actual);
    }

    // messages sent or stored for recipient +27838884567
    @Test
    public void testSearchByRecipient() {
        addMessagesOneToFour();
        addMessage("5555555555", 4, "+27838884567", MESSAGE_5, 3);

        Message msg = new Message();
        String expected = "\"Where are you? You are late! I have asked you to be on time.\" \"Ok, I am leaving without you.\"";
        String actual = msg.searchByRecipient("+27838884567");
        assertEquals(expected, actual);
    }

    // delete test message 2 using its message hash
    @Test
    public void testDeleteMessageByHash() {
        Message message2 = addMessage("2222222222", 1, "+27838884567", MESSAGE_2, 3);

        Message msg = new Message();
        String expected = "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.";
        String actual = msg.deleteMessageByHash(message2.getMessageHash());
        assertEquals(expected, actual);
    }

    // report of all sent messages, including the hash, recipient, and message
    @Test
    public void testDisplayReport() {
        addMessagesOneToFour();

        Message msg = new Message();
        String expected = "Message Hash: 11:0:DIDCAKE\n"
                + "Recipient: +27834557896\n"
                + "Message: Did you get the cake?\n"
                + "\n"
                + "Message Hash: 08:3:IT\n"
                + "Recipient: 0838884567\n"
                + "Message: It is dinner time !";
        String actual = msg.displayReport();
        assertEquals(expected, actual);
    }
}
