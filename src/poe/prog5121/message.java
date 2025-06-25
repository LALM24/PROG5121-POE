/*
 * Chat System for Programming Class
 * Integrates with existing Login system
 * Enhanced with array functionality
 */
package poe.prog5121;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 
 * @author lucam
 */
public class message {
    
    private List<Message> messages;
    private int totalMessages;
    private int maxMessages;
    
    // NEW ARRAYS FOR ENHANCED FUNCTIONALITY
    private ArrayList<String> sentMessages;           // Contains all message contents
    private ArrayList<String> disregardedMessages;   // Contains disregarded messages
    private ArrayList<String> storedMessages;        // Contains stored messages from JSON
    private ArrayList<String> messageHashes;         // Contains all message hashes
    private ArrayList<String> messageIDs;            // Contains all message IDs
    
    public message() {
        this.messages = new ArrayList<>();
        this.totalMessages = 0;
        
        // Initialize the new arrays
        this.sentMessages = new ArrayList<>();
        this.disregardedMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        this.messageHashes = new ArrayList<>();
        this.messageIDs = new ArrayList<>();
        
        // Load stored messages when starting
        loadStoredMessages();
    }
    

    public class Message {
        private String messageId;
        private int messageNumber;
        private String recipient;
        private String messageContent;
        private String messageHash;
        
        public Message(String messageId, int messageNumber, String recipient, String messageContent, String messageHash) {
            this.messageId = messageId;
            this.messageNumber = messageNumber;
            this.recipient = recipient;
            this.messageContent = messageContent;
            this.messageHash = messageHash;
        }
        
        // GETTERS
        public String getMessageId() { return messageId; }
        public int getMessageNumber() { return messageNumber; }
        public String getRecipient() { return recipient; }
        public String getMessageContent() { return messageContent; }
        public String getMessageHash() { return messageHash; }
    }
    

    public boolean checkMessage(String messageId) {
        return messageId != null && messageId.length() <= 10;
    }
    

    public boolean checkRecipientCell(String cellNumber) {
        if (cellNumber == null) return false;
        
        // REMOVES SPACES AND CHECKS FOR LENGTH
        String cleanNumber = cellNumber.replaceAll("\\s+", "");
        
        // CHECK FOR INTERNATIONAL CODE
        if (cleanNumber.startsWith("+27")) {
            String numberPart = cleanNumber.substring(3);
            return numberPart.length() <= 10 && numberPart.matches("\\d+");
        } else if (cleanNumber.startsWith("27")) {
            String numberPart = cleanNumber.substring(2);
            return numberPart.length() <= 10 && numberPart.matches("\\d+");
        }
        
        // FOR LOCAL NUMBERS
        return cleanNumber.length() <= 10 && cleanNumber.matches("\\d+");
    }
    

    public String createMessageHash() {
        Random random = new Random();
        StringBuilder hash = new StringBuilder();
        
        // GENERATE 10 DIGIT RANDOM NUMBER
        for (int i = 0; i < 10; i++) {
            hash.append(random.nextInt(10));
        }
        
        return hash.toString();
    }
    

    public int sendMessage(String message, String recipient) {
        String[] options = {"Send Message", "Store Message", "Disregard Message"};
        
        String displayMessage = "Message: " + message + "\nRecipient: " + recipient + "\n\nWhat would you like to do?";
        
        int choice = JOptionPane.showOptionDialog(
            null,
            displayMessage,
            "Message Options",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        return choice + 1; 
    }
    
    // NEW METHOD: Display sender and recipient of all sent messages
    public void displaySentMessages() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages have been sent yet.");
            return;
        }
        
        StringBuilder display = new StringBuilder();
        display.append("ALL SENT MESSAGES:\n");
        display.append("==================\n");
        
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = messages.get(i);
            display.append("Message ").append(i + 1).append(":\n");
            display.append("Sender: You\n");
            display.append("Recipient: ").append(msg.getRecipient()).append("\n");
            display.append("Message: ").append(sentMessages.get(i)).append("\n");
            display.append("-------------------\n");
        }
        
        JOptionPane.showMessageDialog(null, display.toString());
    }
    
    // NEW METHOD: Display the longest sent message
    public void displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages have been sent yet.");
            return;
        }
        
        String longestMessage = "";
        int longestIndex = 0;
        
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).length() > longestMessage.length()) {
                longestMessage = sentMessages.get(i);
                longestIndex = i;
            }
        }
        
        Message msg = messages.get(longestIndex);
        String display = "LONGEST MESSAGE:\n" +
                        "Length: " + longestMessage.length() + " characters\n" +
                        "Recipient: " + msg.getRecipient() + "\n" +
                        "Message: " + longestMessage;
        
        JOptionPane.showMessageDialog(null, display);
    }
    
    // NEW METHOD: Search for message by ID
    public void searchByMessageID() {
        String searchID = JOptionPane.showInputDialog("Enter Message ID to search:");
        if (searchID == null || searchID.trim().isEmpty()) {
            return;
        }
        
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(searchID)) {
                Message msg = messages.get(i);
                String display = "MESSAGE FOUND:\n" +
                               "Message ID: " + msg.getMessageId() + "\n" +
                               "Recipient: " + msg.getRecipient() + "\n" +
                               "Message: " + msg.getMessageContent();
                
                JOptionPane.showMessageDialog(null, display);
                return;
            }
        }
        
        JOptionPane.showMessageDialog(null, "Message ID not found: " + searchID);
    }
    
    // NEW METHOD: Search for messages by recipient
    public void searchByRecipient() {
        String searchRecipient = JOptionPane.showInputDialog("Enter recipient to search:");
        if (searchRecipient == null || searchRecipient.trim().isEmpty()) {
            return;
        }
        
        StringBuilder results = new StringBuilder();
        results.append("MESSAGES TO: ").append(searchRecipient).append("\n");
        results.append("========================\n");
        
        boolean found = false;
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            if (msg.getRecipient().equals(searchRecipient)) {
                found = true;
                results.append("Message ID: ").append(msg.getMessageId()).append("\n");
                results.append("Message: ").append(msg.getMessageContent()).append("\n");
                results.append("-------------------\n");
            }
        }
        
        if (!found) {
            results.append("No messages found for recipient: ").append(searchRecipient);
        }
        
        JOptionPane.showMessageDialog(null, results.toString());
    }
    
    // NEW METHOD: Delete message by hash
    public void deleteMessageByHash() {
        String searchHash = JOptionPane.showInputDialog("Enter message hash to delete:");
        if (searchHash == null || searchHash.trim().isEmpty()) {
            return;
        }
        
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(searchHash)) {
                Message msg = messages.get(i);
                
                int confirm = JOptionPane.showConfirmDialog(null, 
                    "Delete this message?\n\nRecipient: " + msg.getRecipient() + 
                    "\nMessage: " + msg.getMessageContent(),
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Remove from all arrays
                    messages.remove(i);
                    sentMessages.remove(i);
                    messageHashes.remove(i);
                    messageIDs.remove(i);
                    totalMessages--;
                    
                    JOptionPane.showMessageDialog(null, "Message deleted successfully!");
                }
                return;
            }
        }
        
        JOptionPane.showMessageDialog(null, "Message hash not found: " + searchHash);
    }
    
    // NEW METHOD: Display full report
    public void displayFullReport() {
        if (messages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages to display in report.");
            return;
        }
        
        StringBuilder report = new StringBuilder();
        report.append("FULL MESSAGE REPORT\n");
        report.append("===================\n");
        report.append("Total Messages Sent: ").append(totalMessages).append("\n");
        report.append("Total Disregarded: ").append(disregardedMessages.size()).append("\n\n");
        
        for (int i = 0; i < messages.size(); i++) {
            Message msg = messages.get(i);
            report.append("Message ").append(i + 1).append(":\n");
            report.append("ID: ").append(msg.getMessageId()).append("\n");
            report.append("Hash: ").append(msg.getMessageHash()).append("\n");
            report.append("Recipient: ").append(msg.getRecipient()).append("\n");
            report.append("Content: ").append(msg.getMessageContent()).append("\n");
            report.append("Number: ").append(msg.getMessageNumber()).append("\n");
            report.append("-------------------\n");
        }
        
        JOptionPane.showMessageDialog(null, report.toString());
    }
    
    // Load stored messages from JSON file (simple version)
    private void loadStoredMessages() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("messages.json"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("messageContent")) {
                    // Extract message content (simple parsing)
                    String content = line.substring(line.indexOf(":") + 1).trim();
                    content = content.replace("\"", "").replace(",", "");
                    storedMessages.add(content);
                }
            }
            reader.close();
        } catch (IOException e) {
            // File doesn't exist yet, that's okay
        }
    }
    

    public String printMessages() {
        if (messages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        
        StringBuilder messageList = new StringBuilder();
        messageList.append("=== ALL SENT MESSAGES ===\n\n");
        
        for (Message msg : messages) {
            messageList.append("Message ID: ").append(msg.getMessageId()).append("\n");
            messageList.append("Message Hash: ").append(msg.getMessageHash()).append("\n");
            messageList.append("Recipient: ").append(msg.getRecipient()).append("\n");
            messageList.append("Message: ").append(msg.getMessageContent()).append("\n");
            messageList.append("Message Number: ").append(msg.getMessageNumber()).append("\n");
            messageList.append("------------------------\n");
        }
        
        return messageList.toString();
    }
    

    public int returnTotalMessages() {
        return totalMessages;
    }
    

    public void storeMessagesInJSON() {
        try {
            FileWriter writer = new FileWriter("messages.json");
            writer.write("{\n");
            writer.write("  \"totalMessages\": " + totalMessages + ",\n");
            writer.write("  \"messages\": [\n");
            
            for (int i = 0; i < messages.size(); i++) {
                Message msg = messages.get(i);
                writer.write("    {\n");
                writer.write("      \"messageId\": \"" + msg.getMessageId() + "\",\n");
                writer.write("      \"messageNumber\": " + msg.getMessageNumber() + ",\n");
                writer.write("      \"recipient\": \"" + msg.getRecipient() + "\",\n");
                writer.write("      \"messageContent\": \"" + msg.getMessageContent().replaceAll("\"", "\\\\\"") + "\",\n");
                writer.write("      \"messageHash\": \"" + msg.getMessageHash() + "\"\n");
                writer.write("    }");
                
                if (i < messages.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            
            writer.write("  ]\n");
            writer.write("}\n");
            writer.close();
            
            JOptionPane.showMessageDialog(null, "Messages successfully saved to messages.json");
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving messages: " + e.getMessage());
        }
    }
    
    public void startChatApplication(String userName, String userSurname) {
        // DISPLAY WELCOME MESSAGE
        JOptionPane.showMessageDialog(null, "Welcome " + userName + " " + userSurname + " to the Enhanced Chat Platform!");
        
        // GET NUMBER
        String maxMessagesStr = JOptionPane.showInputDialog("How many messages would you like to send?");
        try {
            maxMessages = Integer.parseInt(maxMessagesStr);
            if (maxMessages <= 0) {
                JOptionPane.showMessageDialog(null, "Invalid number. Setting to 1 message.");
                maxMessages = 1;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Setting to 1 message.");
            maxMessages = 1;
        }
        
        // MAIN APPLICATION LOOP
        boolean running = true;
        int messagesSent = 0;
        
        while (running && messagesSent < maxMessages) {
            String[] menuOptions = {
                "Send Message", 
                "Show All Sent Messages", 
                "Show Longest Message",
                "Search by Message ID",
                "Search by Recipient",
                "Delete by Hash",
                "Full Report",
                "Quit"
            };
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "Select an option:",
                "Enhanced Chat Platform Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                menuOptions,
                menuOptions[0]
            );
            
            switch (choice) {
                case 0: // Send Message
                    if (messagesSent < maxMessages) {
                        boolean messageSent = handleSendMessage();
                        if (messageSent) {
                            messagesSent++;
                            JOptionPane.showMessageDialog(null, 
                                "Messages sent: " + messagesSent + "/" + maxMessages);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "You have reached your message limit of " + maxMessages + " messages.");
                    }
                    break;
                    
                case 1: // Show All Sent Messages
                    displaySentMessages();
                    break;
                    
                case 2: // Show Longest Message
                    displayLongestMessage();
                    break;
                    
                case 3: // Search by Message ID
                    searchByMessageID();
                    break;
                    
                case 4: // Search by Recipient
                    searchByRecipient();
                    break;
                    
                case 5: // Delete by Hash
                    deleteMessageByHash();
                    break;
                    
                case 6: // Full Report
                    displayFullReport();
                    break;
                    
                case 7: // Quit
                case -1: // User closed dialog
                    running = false;
                    break;
            }
        }
        
        // DISPLAY FINAL SUMMARY
        JOptionPane.showMessageDialog(null, 
            "Chat session ended.\nTotal messages sent: " + returnTotalMessages());
        
        // SAVE SENT MESSAGES TO JSON
        if (totalMessages > 0) {
            int saveChoice = JOptionPane.showConfirmDialog(null, 
                "Would you like to save your messages to a JSON file?", 
                "Save Messages", JOptionPane.YES_NO_OPTION);
            
            if (saveChoice == JOptionPane.YES_OPTION) {
                storeMessagesInJSON();
            }
        }
    }
    
    private boolean handleSendMessage() {
        
        // GET RECIPIENT
        String recipient = JOptionPane.showInputDialog("Enter recipient's cell phone number (with international code +27):");
        
        if (recipient == null || recipient.trim().isEmpty()) {
            disregardedMessages.add("Empty recipient - " + new java.util.Date());
            JOptionPane.showMessageDialog(null, "Recipient cannot be empty.");
            return false;
        }
        
        // VALIDATE RECIPIENT 
        if (!checkRecipientCell(recipient)) {
            disregardedMessages.add("Invalid recipient: " + recipient + " - " + new java.util.Date());
            JOptionPane.showMessageDialog(null, 
                "Invalid recipient cell number. Please ensure it has international code and is no more than 10 digits after the code.");
            return false;
        }
        
        // GET MESSAGE CONTENT
        String messageContent = JOptionPane.showInputDialog("Enter your message (max 50 words):");
        
        if (messageContent == null || messageContent.trim().isEmpty()) {
            disregardedMessages.add("Empty message content - " + new java.util.Date());
            JOptionPane.showMessageDialog(null, "Message cannot be empty.");
            return false;
        }
        
        //CHECK MESSAGE LENGTH
        String[] words = messageContent.trim().split("\\s+");
        if (words.length > 50) {
            disregardedMessages.add("Message too long: " + messageContent + " - " + new java.util.Date());
            JOptionPane.showMessageDialog(null, 
                "Message is too long. Please limit to 50 words. Current word count: " + words.length);
            return false;
        }
        
        // GENERATE MESSAGE ID AND HASH
        String messageId = createMessageHash();
        String messageHash = createMessageHash();
        
        // VALIDATE MESSAGE ID
        if (!checkMessage(messageId)) {
            messageId = messageId.substring(0, 10);
        }
        
        // ASK USER FOR WHAT MUST BE DONE WITH THE MESSAGE
        int userChoice = sendMessage(messageContent, recipient);
        
        switch (userChoice) {
            case 1: // SEND MESSAGE
                totalMessages++;
                Message newMessage = new Message(messageId, totalMessages, recipient, messageContent, messageHash);
                messages.add(newMessage);
                
                // ADD TO ARRAYS
                sentMessages.add(messageContent);
                messageIDs.add(messageId);
                messageHashes.add(messageHash);
                
                // DISPLAY FULL MESSAGE DETAILS 
                String messageDetails = "MESSAGE SENT SUCCESSFULLY!\n\n" +
                    "Message ID: " + messageId + "\n" +
                    "Message Hash: " + messageHash + "\n" +
                    "Recipient: " + recipient + "\n" +
                    "Message: " + messageContent + "\n" +
                    "Message Number: " + totalMessages;
                
                JOptionPane.showMessageDialog(null, messageDetails);
                return true;
            
            case 2: // STORE MESSAGE
                JOptionPane.showMessageDialog(null, "Message stored for later sending.");
                return false;
                
            case 3: // DISREGARD MESSAGE
                disregardedMessages.add(messageContent + " - " + new java.util.Date());
                JOptionPane.showMessageDialog(null, "Message discarded.");
                return false;
                
            default:
                return false;
        }
    }
}