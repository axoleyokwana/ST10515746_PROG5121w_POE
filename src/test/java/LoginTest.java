/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.myquickchat.Login;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Part 1 registration and login.
 *
 * @author Axole Yokwana
 */
public class LoginTest {

    // username kyl_1 is correct, so the user can log in
    @Test
    public void testUsernameCorrectlyFormatted() {
        Login login = new Login();

        // user first name and user last name
        login.setFirstName("Kyle");
        login.setLastName("Smith");

        // register the user
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");

        String expected = "Welcome Kyle, Smith it is great to see you again.";
        String actual = login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!");
        assertEquals(expected, actual);
        assertEquals("Username successfully captured.", login.usernameMessage("kyl_1"));
    }

    // username kyle!!!!!!! is not correct
    @Test
    public void testUsernameIncorrectlyFormatted() {
        Login login = new Login();
        String expected = "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        String actual = login.usernameMessage("kyle!!!!!!!");
        assertEquals(expected, actual);
    }

    // password Ch&&sec@ke99! meets the rules
    @Test
    public void testPasswordMeetsComplexity() {
        Login login = new Login();
        String expected = "Password successfully captured.";
        String actual = login.passwordMessage("Ch&&sec@ke99!");
        assertEquals(expected, actual);
    }

    // password password does not meet the rules
    @Test
    public void testPasswordDoesNotMeetComplexity() {
        Login login = new Login();
        String expected = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        String actual = login.passwordMessage("password");
        assertEquals(expected, actual);
    }

    // cell phone +27838968976 is correct
    @Test
    public void testCellPhoneCorrectlyFormatted() {
        Login login = new Login();
        String expected = "Cell number successfully captured.";
        String actual = login.cellPhoneMessage("+27838968976");
        assertEquals(expected, actual);
    }

    // cell phone 08966553 is not correct
    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        Login login = new Login();
        String expected = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        String actual = login.cellPhoneMessage("08966553");
        assertEquals(expected, actual);
    }

    // login works
    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");

        boolean actual = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue(actual);
    }

    // login does not work 
    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.setFirstName("Kyle");
        login.setLastName("Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");

        boolean actual = login.loginUser("kyl_1", "password");
        assertFalse(actual);
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus("kyl_1", "password"));
    }

    // username check returns true
    @Test
    public void testUsernameCheckTrue() {
        Login login = new Login();
        boolean actual = login.checkUserName("kyl_1");
        assertTrue(actual);
    }

    // username check returns false
    @Test
    public void testUsernameCheckFalse() {
        Login login = new Login();
        boolean actual = login.checkUserName("kyle!!!!!!!");
        assertFalse(actual);
    }

    // password check returns true
    @Test
    public void testPasswordCheckTrue() {
        Login login = new Login();
        boolean actual = login.checkPasswordComplexity("Ch&&sec@ke99!");
        assertTrue(actual);
    }

    // password check returns false
    @Test
    public void testPasswordCheckFalse() {
        Login login = new Login();
        boolean actual = login.checkPasswordComplexity("password");
        assertFalse(actual);
    }

    // cell phone check returns true
    @Test
    public void testCellPhoneCheckTrue() {
        Login login = new Login();
        boolean actual = login.checkCellPhoneNumber("+27838968976");
        assertTrue(actual);
    }

    // cell phone check returns false
    @Test
    public void testCellPhoneCheckFalse() {
        Login login = new Login();
        boolean actual = login.checkCellPhoneNumber("08966553");
        assertFalse(actual);
    }
}
