/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package name.prokop.bart.fps.drivers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author bart
 */
public final class FiscalPrinterInfo {

    private static Map<String, Class> map;

    static {
        map = new HashMap<String, Class>();
        map.put("Emulator drukarki", BogusFiscalPrinter.class);
        map.put("Elzab Omega 2", ElzabOmega2.class);
        map.put("Innova Profit v. 4.51", InnovaProfit451.class);
        map.put("Optimus Vivo", OptimusVivo.class);
        map.put("Posnet Thermal v. 1.01", Thermal101.class);
        map.put("Posnet Thermal v. 2.03", Thermal203.class);
        map.put("Posnet Thermal v. 3.01", Thermal301.class);
        map.put("Posnet Thermal starej homologacji", ThermalOld.class);
        map.put("Posnet 101", Posnet101.class);
    }

    public static Map<String, Class> getFiscalPrinterDriverList() {
        return map;
    }

    public static String getDriverName(Class driver) {
        for (Entry<String, Class> e : map.entrySet()) {
            if (e.getValue().equals(driver)) {
                return e.getKey();
            }
        }
        return null;
    }
}
