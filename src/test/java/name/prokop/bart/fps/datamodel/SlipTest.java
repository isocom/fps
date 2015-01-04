/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package name.prokop.bart.fps.datamodel;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prokob01
 */
public class SlipTest {

    public SlipTest() {
    }

    @Test
    public void testSomeMethod() throws JSONException {
        Slip slip = SlipExamples.getSampleSlip();
        String s1 = slip.toJSONObject().toString(3);
        System.out.println(s1);
        slip = new Slip(new JSONObject(s1));
        String s2 = slip.toJSONObject().toString(3);
        System.out.println(s2);
        assertEquals(s2, s1);
    }
}
