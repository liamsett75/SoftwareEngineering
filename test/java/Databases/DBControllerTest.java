package Databases;

import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBControllerTest {

    @BeforeClass
    public static void setUp(){
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }

    /**
     * openConnection() - tests opening connection
     */
    @Test
    public void getConnection() {
        // connection can only be opened once
        DBConnection.getConnection();
    }

    /**
     * closeConnection() - tests closing connection
     */
    @Test
    public void getInstance() {
        DBConnection.getInstance();
    }
}