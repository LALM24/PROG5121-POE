/*
 * Chat System for Programming Class
 * Integrates with existing Login system
 */
package poe.prog5121;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Chat system class that handles messaging functionality
 * @author lucam
 */
public class message {
    
    private List<Message> messages;
    private int totalMessages;
    private int maxMessages;
    
    public message() {
        this.messages = new ArrayList<>();
        this.totalMessages = 0;
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
        JOptionPane.showMessageDialog(null, "Welcome " + userName + " " + userSurname + " to the Chat Platform!");
        
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
            String[] menuOptions = {"Send Message", "Show Recently Sent Messages", "Quit"};
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "Select an option:\n1. Send Message\n2. Show Recently Sent Messages\n3. Quit",
                "Chat Platform Menu",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                menuOptions,
                menuOptions[0]
            );
            
            switch (choice) {
               //SEND MESSAGE
                case 0: 
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
                    
                //SHOW SENT MESSAGE    
                case 1: 
                    JOptionPane.showMessageDialog(null, "Coming Soon");
                    break;
                    
                //QUIT    
                case 2:
                //CLOSED USER DIALOG    
                case -1: 
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
            JOptionPane.showMessageDialog(null, "Recipient cannot be empty.");
            return false;
        }
        
        // VALIDATE RECIPIENT 
        if (!checkRecipientCell(recipient)) {
            JOptionPane.showMessageDialog(null, 
                "Invalid recipient cell number. Please ensure it has international code and is no more than 10 digits after the code.");
            return false;
        }
        
        // GET MESSAGE CONTENT
        String messageContent = JOptionPane.showInputDialog("Enter your message (max 50 words):");
        
        if (messageContent == null || messageContent.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Message cannot be empty.");
            return false;
        }
        
        //CHECK MESSAGE LENGTH
        String[] words = messageContent.trim().split("\\s+");
        if (words.length > 50) {
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
            //SEND MESSAGE
            case 1: 
                totalMessages++;
                Message newMessage = new Message(messageId, totalMessages, recipient, messageContent, messageHash);
                messages.add(newMessage);
                
                // DISPLAY FULL MESSAGE DETAILS 
                String messageDetails = "MESSAGE SENT SUCCESSFULLY!\n\n" +
                    "Message ID: " + messageId + "\n" +
                    "Message Hash: " + messageHash + "\n" +
                    "Recipient: " + recipient + "\n" +
                    "Message: " + messageContent + "\n" +
                    "Message Number: " + totalMessages;
                
                JOptionPane.showMessageDialog(null, messageDetails);
                return true;
            
            //STORE MESSAGE    
            case 2: 
                JOptionPane.showMessageDialog(null, "Message stored for later sending.");
                return false;
                
            //DISREGARD MESSAGE    
            case 3: 
                JOptionPane.showMessageDialog(null, "Message discarded.");
                return false;
                
            default:
                return false;
        }
    }
}