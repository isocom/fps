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
package name.prokop.bart.fps.db;

import name.prokop.bart.fps.datamodel.Invoice;

/**
 *
 * @author bart
 */
public interface InvoiceDAO {

    public void markAsErrored(String invoiceToPrint, String error);

    public void markAsPrinted(String invoiceToPrint);

    public String findInvoiceToPrint(String printerName);

    public Invoice readInvoice(String invoiceToPrint);

    public void saveInvoice(Invoice invoice);
}
