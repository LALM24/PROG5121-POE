/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package poe.prog5121;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lucam
 */
public class LoginTest {
    
    public LoginTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of checkUserName method, of class Login.
     */
    @Test
    public void testCheckUserName_Valid() {
        Login login = new Login();
        assertTrue(login.checkUserName("abc_"));
    }

    @Test
    public void testCheckUserName_Invalid() {
        Login login = new Login();
        assertFalse(login.checkUserName("abcdef"));
    }

    /**
     * Test of checkPasswordComplexity method, of class Login.
     */
    @Test
    public void testCheckPasswordComplexity_Valid() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Abcdef1!"));
    }

    @Test
    public void testCheckPasswordComplexity_Invalid() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("abc"));
    }

    /**
     * Test of checkCellPhoneNumber method, of class Login.
     */
    @Test
    public void testCheckCellPhoneNumber() {
        System.out.println("checkCellPhoneNumber");
        String userInput = "";
        Login instance = new Login();
        boolean expResult = false;
        boolean result = instance.checkCellPhoneNumber(userInput);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of registerUser method, of class Login.
     */
    @Test
    public void testRegisterUser() {
        System.out.println("registerUser");
        boolean checkUserName = false;
        boolean checkPasswordComplexity = false;
        boolean checkCellPhoneNumber = false;
        Login instance = new Login();
        String expResult = "";
        String result = instance.registerUser(checkUserName, checkPasswordComplexity, checkCellPhoneNumber);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of returnLoginStatus method, of class Login.
     */
    @Test
    public void testReturnLoginStatus() {
        System.out.println("returnLoginStatus");
        String returnValue = "";
        Login instance = new Login();
        String expResult = "";
        String result = instance.returnLoginStatus(returnValue);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loginUser method, of class Login.
     */
    @Test
    public void testLoginUser() {
        System.out.println("loginUser");
        String uName = "";
        String pWord = "";
        String status = "";
        Login instance = new Login();
        boolean expResult = false;
        boolean result = instance.loginUser(uName, pWord, status);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
