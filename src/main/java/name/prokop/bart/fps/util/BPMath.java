/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * BPMath.java
 *
 * Created on 23 styczeń 2005, 12:33
 */
package name.prokop.bart.fps.util;

/**
 *
 * @author bart
 */
public class BPMath {

    public static void main(String[] args) {
        System.out.println(7.75 * 0.3);
        System.out.println(round(round(7.75 * 0.3, 3), 2));
        System.out.println(roundCurrency(1.4449999999999999999));
        System.out.println(round(1.4449999999999999999, 2));
        double d = 32.464996;
        System.out.println(d);
        System.out.println(round(round(d, 3), 2));
        System.out.println(roundCurrency(d));
        System.out.println(round(d, 2));
        System.out.println(round(d + 0.00001, 2));
        d = 0.6875 * 73.52;
        System.out.println(0.0001 * 0.01);
        System.out.println(d);
        System.out.println(round(round(d, 3), 2));
        System.out.println(roundCurrency(d));
        System.out.println(round(d, 2));
        System.out.println(round(d + 0.00001, 2));
    }

    public static double roundCurrency(double val) {
        return round(val + 0.000000001, 2);
    }

    public static double round(double val, int radix) {
        double shift = Math.pow(10, radix);
        return Math.round(val * shift) / shift;
    }

    public static long truncate(double x) {
        return (long) x;
    }

    /**
     * remove integral part and return fractional part
     * @param x
     * @return
     */
    public static double fractional(double x) {
        return x - truncate(x);
    }
}
