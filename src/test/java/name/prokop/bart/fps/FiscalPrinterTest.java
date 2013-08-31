/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.prokop.bart.fps;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author prokob01
 */
public class FiscalPrinterTest {

    public FiscalPrinterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of print method, of class FiscalPrinter.
     */
    @Test
    public void testTypeInstantialization() throws Exception {
        for (FiscalPrinter.Type type : FiscalPrinter.Type.values()) {
            FiscalPrinter fiscalPrinter = type.getFiscalPrinter("Test");
            System.out.println(fiscalPrinter);
            assertNotNull(fiscalPrinter);
        }
    }
}
