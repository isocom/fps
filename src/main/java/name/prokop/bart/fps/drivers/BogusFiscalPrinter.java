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

import name.prokop.bart.fps.FiscalPrinter;
import name.prokop.bart.fps.FiscalPrinterException;
import name.prokop.bart.fps.datamodel.Invoice;
import name.prokop.bart.fps.datamodel.Slip;

/**
 *
 * @author Bart
 */
public class BogusFiscalPrinter implements FiscalPrinter {

    public static FiscalPrinter getFiscalPrinter(String comPortName) {
        return new BogusFiscalPrinter();
    }

    public BogusFiscalPrinter() {
        System.out.println("Bogus Fiscal Printer initialized");
    }

    @Override
    public void print(Slip slip) throws FiscalPrinterException {
        System.out.println("Bogus Fiscal Printer\nPrinting Slip:\n" + slip);
    }

    @Override
    public void print(Invoice invoice) throws FiscalPrinterException {
        System.out.println("Bogus Fiscal Printer\nPrinting Invoice:\n" + invoice);
    }

    @Override
    public void openDrawer() throws FiscalPrinterException {
        System.out.println("Opening drawer");
    }

    @Override
    public void printDailyReport() throws FiscalPrinterException {
        System.out.println("Printing daily report");
    }
}
