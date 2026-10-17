/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.myquickchat.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Part 2 message creation, validation, hashing, and sending.
 *
 * @author AxoleYokwana
 */
public class MessageTest {

    @org.junit.jupiter.api.io.TempDir
    java.nio.file.Path tempDirectory;

    @BeforeEach
    public void setUp() {
        Message.resetTotalMessages();
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
        String actual = msg.SentMessage(3, tempDirectory.resolve("messages.json").toString());
        assertEquals(expected, actual);
        assertTrue(java.nio.file.Files.exists(tempDirectory.resolve("messages.json")));
    }

    // check total messages sent count
    @Test
    public void testReturnTotalMessagess() {
        Message msg1 = new Message();
        msg1.SentMessage(1);

        Message msg2 = new Message();
        msg2.SentMessage(2);

        Message msg3 = new Message();
        msg3.SentMessage(1);

        assertEquals(2, msg1.returnTotalMessagess());
    }

    // print messages details in correct order
    @Test
    public void testPrintMessagesOrder() {
        Message msg = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        String details = msg.printMessageDetails();

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

    @Test
    public void testPrintMessagesReturnsAllSentMessages() {
        Message first = new Message("0012345678", 0, "+27718693002", "First message");
        Message second = new Message("0112345678", 1, "+27718693003", "Second message");
        first.SentMessage(1);
        second.SentMessage(1);

        String messages = first.printMessages();

        assertTrue(messages.contains("Message ID: 0012345678"));
        assertTrue(messages.contains("Message ID: 0112345678"));
        assertTrue(messages.indexOf("First message") < messages.indexOf("Second message"));
    }

    // verify message is successfully stored in a JSON file
    @Test
    public void testStoreMessageInJsonFile() throws java.io.IOException {
        java.nio.file.Path testFile = tempDirectory.resolve("direct_store.json");

        Message msg = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg.setStatus("Stored");
        msg.storeMessage(testFile.toString());

        assertTrue(java.nio.file.Files.exists(testFile));
        assertTrue(java.nio.file.Files.size(testFile) > 0);
    }
}