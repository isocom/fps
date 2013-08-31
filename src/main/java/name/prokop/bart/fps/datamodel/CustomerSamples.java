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
package name.prokop.bart.fps.datamodel;

/**
 *
 * @author prokob01
 */
public class CustomerSamples {

    public static Slip kamilSzarmach() {
        Slip slip = new Slip();
        slip.setReference("R-k 0123456789");
        slip.setCashbox("XX99");
        slip.setCashierName("Test Testowy");
        slip.addLine("Żetony", 1, 31, VATRate.VAT23);
        slip.addPayment(SlipPayment.PaymentType.Cash, 31.0, null);
        return slip;
    }
}
