
package poe.prog5121;

import org.junit.Test;
import static org.junit.Assert.*;

public class messageTest {
    
    @Test
    public void testCheckRecipientCell() {
        message msgHandler = new message();
        
        // Test valid international numbers
        assertTrue(msgHandler.checkRecipientCell("+27834557896"));
        assertTrue(msgHandler.checkRecipientCell("27838884567"));
        
        // Test valid local number
        assertTrue(msgHandler.checkRecipientCell("0838884567"));
        
        // Test invalid numbers
        assertFalse(msgHandler.checkRecipientCell("12345")); // Too short
        assertFalse(msgHandler.checkRecipientCell("+27834557896123")); // Too long
        assertFalse(msgHandler.checkRecipientCell("+2783455789A")); // Contains letter
    }
    
    @Test
    public void testCheckMessage() {
        message msgHandler = new message();
        
        // Test valid message IDs
        assertTrue(msgHandler.checkMessage("1234567890"));
        assertTrue(msgHandler.checkMessage("1"));
        
        // Test invalid message IDs
        assertFalse(msgHandler.checkMessage("12345678901234567890")); // Too long
        assertFalse(msgHandler.checkMessage(null)); // Null
    }
    
    @Test
    public void testSendMessageWorkflow() {
        message msgHandler = new message();
        
        // Test case 1: Send message
        String recipient1 = "+27834557896";
        String message1 = "did you get the cake";
        // You'll need to mock the JOptionPane responses for this to work
        
        // Test case 4: Local number
        String recipient4 = "0838884567";
        String message4 = "its dinner time";
    }
    
    @Test
    public void testCreateMessageHash() {
        message msgHandler = new message();
        String hash = msgHandler.createMessageHash();
        assertNotNull(hash);
        assertEquals(10, hash.length());
        assertTrue(hash.matches("\\d+")); // Should be all digits
    }
}