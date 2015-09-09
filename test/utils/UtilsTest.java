/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Properties;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Janus
 */
public class UtilsTest {
    
    public UtilsTest() {
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
     * Test of initProperties method, of class Utils.
     */
    @Test
    public void testInitProperties() {
        System.out.println("initProperties");
        String propertyFile = "";
        Properties expResult = null;
        Properties result = Utils.initProperties(propertyFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLogFile method, of class Utils.
     */
    @Test
    public void testSetLogFile() {
        System.out.println("setLogFile");
        String logFile = "";
        String className = "";
        Utils.setLogFile(logFile, className);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeLogger method, of class Utils.
     */
    @Test
    public void testCloseLogger() {
        System.out.println("closeLogger");
        String logger = "";
        Utils.closeLogger(logger);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLogger method, of class Utils.
     */
    @Test
    public void testGetLogger() {
        System.out.println("getLogger");
        String logFile = "";
        String className = "";
        Logger expResult = null;
        Logger result = Utils.getLogger(logFile, className);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
