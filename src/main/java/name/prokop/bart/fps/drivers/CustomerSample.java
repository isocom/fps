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

import name.prokop.bart.fps.FiscalPrinterException;
import name.prokop.bart.fps.datamodel.CustomerSamples;

/**
 *
 * @author prokob01
 */
public class CustomerSample {

    public static void main(String[] args) {
        Posnet101 fp;

        if (args.length != 0) {
            fp = (Posnet101) Posnet101.getFiscalPrinter(args[0]);
        } else {
            fp = (Posnet101) Posnet101.getFiscalPrinter("COM1");
        }

        try {
            fp.print(CustomerSamples.kamilSzarmach());
            //fp.print(Slip.getSampleSlip());
            //fp.print(Invoice.getTestInvoice());
            fp.openDrawer();
            //fp.printDailyReport();
        } catch (FiscalPrinterException e) {
            System.err.println(e);
        }
    }
}
