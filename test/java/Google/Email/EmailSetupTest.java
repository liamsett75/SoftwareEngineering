package Google.Email;

import _Initialize._Initialize;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmailSetupTest {

    @BeforeClass
    public static void setUp(){
        _Initialize.setUp();
    }

    @AfterClass
    public static void tearDown(){
        _Initialize.setUp();
    }
//
//    @Test
//    public void setService() {
//    }
//
//    @Test
//    public void getService() {
//    }
//
//    @Test
//    public void messageSetup() {
//    }
}