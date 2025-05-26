/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poe.prog5121;

import javax.swing.JOptionPane;

/**
 *
 * @author lucam
 */
public class Login {
    
    //USERNAME VALIDATION
    public boolean checkUserName(String uName){
        
        if (uName == null || uName.trim().isEmpty()) {
            return false;
        }
        
        boolean length = false;
        boolean contains = false;
        
        // Check if username is 5 characters or less
        if (uName.length() <= 5) {
            length = true;
        }
        
        // Check if username contains underscore
        if (uName.contains("_")){
            contains = true;
        }
        
        return length && contains;
    }
    
    //PASSWORD VALIDATION
    public boolean checkPasswordComplexity(String pWord){
        
        if (pWord == null || pWord.trim().isEmpty()) {
            return false;
        }
        
        boolean hasMinLength = false;
        boolean hasUppercase = false;
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        
        // Check password is at least 8 characters
        if (pWord.length() >= 8){
            hasMinLength = true;
        }
        
        // Loop through password to check for uppercase, number and special character
        for (int i = 0; i < pWord.length(); i++) {
            
            char c = pWord.charAt(i);
            
            // Check for uppercase letter
            if (Character.isUpperCase(c)){
                hasUppercase = true;
            }
            
            // Check for number
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            
            // Check for special character
            if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        
        // Return true only if all conditions are met
        return hasMinLength && hasUppercase && hasNumber && hasSpecialChar;
    }
    
    //PHONE NUMBER VALIDATION
    public boolean checkCellPhoneNumber(String userInput) {
        
        if (userInput == null || userInput.trim().isEmpty()) {
            return false;
        }
        
        // Remove any spaces from the input
        String cleanInput = userInput.replaceAll("\\s+", "");
        
        // Check if it's exactly 12 characters (like +27xxxxxxxxx)
        if (cleanInput.length() == 12 && cleanInput.startsWith("+27")) {
            // Check if the part after +27 contains only digits
            String numberPart = cleanInput.substring(3);
            return numberPart.matches("\\d+");
        }
        
        return false;
    }
    
    //REGISTER USER
    public String registerUser(boolean checkUserName, boolean checkPasswordComplexity, boolean checkCellPhoneNumber){
        
        StringBuilder errorMessage = new StringBuilder();
        boolean hasErrors = false;
        
        // Check each validation result and build error message
        if (!checkUserName) {
            errorMessage.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.\n");
            hasErrors = true;
        }
        
        if (!checkPasswordComplexity) {
            if (hasErrors) {
                errorMessage.append("\n");
            }
            errorMessage.append("Password is not correctly formatted, please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.\n");
            hasErrors = true;
        }
        
        if (!checkCellPhoneNumber) {
            if (hasErrors) {
                errorMessage.append("\n");
            }
            errorMessage.append("Cell number is incorrectly formatted, please correct the number and try again");
            hasErrors = true;
        }
        
        // Return appropriate message
        if (!hasErrors) {
            return "Registration successful";
        } else {
            return errorMessage.toString();
        }
    }
    
    //RETURN LOGIN STATUS
    public String returnLoginStatus(String returnValue) {
        
        String status = "";
        
        // Use .equals() for string comparison, not ==
        if ("Registration successful".equals(returnValue)){
            status = "A successful login";
        } else {
            status = "A failed login";
        }
        
        return status;
    }
    
    //LOGIN USER
    public boolean loginUser(String uName, String pWord, String status){
        
        // Check if registration was successful
        if ("Registration successful".equals(status)){
            
            String userName = JOptionPane.showInputDialog("Enter your Username:");
            String password = JOptionPane.showInputDialog("Enter your Password:");
            
            // Handle case where user cancels the dialog
            if (userName == null || password == null) {
                JOptionPane.showMessageDialog(null, "Login cancelled");
                return false;
            }
            
            // Check if entered credentials match registered credentials
            if (uName.equals(userName) && pWord.equals(password)){
                JOptionPane.showMessageDialog(null, "Successful login");
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Unsuccessful login - Incorrect username or password");
                return false;
            }
            
        } else {
            JOptionPane.showMessageDialog(null, "User is not registered. Please register first.");
            return false;
        }
    }
}