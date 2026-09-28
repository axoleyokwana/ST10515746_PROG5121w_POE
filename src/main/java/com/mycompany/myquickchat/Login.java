/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.myquickchat;

/**
 * This class checks the username, password, and cell phone number.
 * It also logs the user in.
 *
 * @author AxoleYokwana
 */
public class Login {
 
    // saved account details
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;
    private boolean registered;

    // save the first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // save the last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // check if the username has an underscore and is no more than 5 characters
    public boolean checkUserName(String username) {
        if (username.contains("_") && username.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }

    // check if the password is long enough and has a capital, a number, and a special character
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false; 

        // password must be at least 8 characters long
        if (password.length() < 8) {
            return false;
        }

        // look at each character in the password
        for (int i = 0; i < password.length(); i++) {
            char letter = password.charAt(i);
            boolean isCapitalLetter = letter >= 'A' && letter <= 'Z';
            boolean isSmallLetter = letter >= 'a' && letter <= 'z';
            boolean isNumber = letter >= '0' && letter <= '9';

            // capital letter
            if (isCapitalLetter == true) {
                hasCapitalLetter = true;
            }

            // number
            if (isNumber == true) {
                hasNumber = true;
            }

            // special character is not a letter and not a number
            if (isCapitalLetter == false && isSmallLetter == false && isNumber == false) {
                hasSpecialCharacter = true;
            }
        }

        // all the password rules must be true
        if (hasCapitalLetter == true && hasNumber == true && hasSpecialCharacter == true) {
            return true;
        } else {
            return false;
        }
    }

    // check the cell phone number
    // the number must start with +27 and then have no more than 10 digits
    //
    // Reference for the regular expression:
    // Oracle, "Regular Expressions," The Java Tutorials. [Online].
    // Available: https://docs.oracle.com/javase/tutorial/essential/regex/
    // Accessed: 28 September 2026.
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        // ^ means the start
        // \\+27 means the text must start with +27
        // [0-9]{1,10} means 1 to 10 digits
        // $ means the end
        String pattern = "^\\+27[0-9]{1,10}$";

        if (cellPhoneNumber.matches(pattern)) {
            return true;
        } else {
            return false;
        }
    }

    // message for the username
    public String usernameMessage(String username) {
        if (checkUserName(username) == true) {
            return "Username successfully captured.";
        } else {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    }

    // message for the password
    public String passwordMessage(String password) {
        if (checkPasswordComplexity(password) == true) {
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    // message for the cell phone number
    public String cellPhoneMessage(String cellPhoneNumber) {
        if (checkCellPhoneNumber(cellPhoneNumber) == true) {
            return "Cell number successfully captured.";
        } else {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
    }

    // register the user and return the messages
    public String registerUser(String username, String password, String cellPhoneNumber) {
        String message;

        message = usernameMessage(username) + "\n";
        message = message + passwordMessage(password) + "\n";
        message = message + cellPhoneMessage(cellPhoneNumber);

        // save the account only when every check is true
        if (checkUserName(username) == true && checkPasswordComplexity(password) == true && checkCellPhoneNumber(cellPhoneNumber) == true) {
            this.username = username;
            this.password = password;
            this.cellPhoneNumber = cellPhoneNumber;
            this.registered = true;
            message = message + "\n" + "The user has been registered successfully.";
        } else {
            this.registered = false;
        }

        return message;
    }

    // check if the login username and password match the saved ones
    public boolean loginUser(String username, String password) {
        // user must register first
        if (registered == false) {
            return false;
        }

        // user enters username and user enters password
        if (this.username.equals(username) && this.password.equals(password)) {
            return true;
        } else {
            return false;
        }
    }

    // message for a successful login or a failed login
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password) == true) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
