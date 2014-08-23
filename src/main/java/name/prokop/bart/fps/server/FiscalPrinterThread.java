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
package name.prokop.bart.fps.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import name.prokop.bart.fps.FiscalPrinter;
import name.prokop.bart.fps.datamodel.Invoice;
import name.prokop.bart.fps.datamodel.Slip;
import name.prokop.bart.fps.db.InvoiceDAO;
import name.prokop.bart.fps.db.SlipDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author bart
 */
public class FiscalPrinterThread implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(FiscalPrinterThread.class);
    private final String printerName;
    @Autowired
    private SlipDAO slipTools;
    @Autowired
    private InvoiceDAO invoiceTools;
    private boolean terminate = false;
    private FiscalPrinter fiscalPrinter;

    public FiscalPrinterThread(String printerName) {
        this.printerName = printerName;
        LOGGER.info("new Fiscal Printer Thread was setup, for printer named: " + printerName + ".");
    }

    String getPrinterName() {
        return printerName;
    }

    public FiscalPrinter getFiscalPrinter() {
        return fiscalPrinter;
    }

    public void setFiscalPrinter(FiscalPrinter fiscalPrinter) {
        this.fiscalPrinter = fiscalPrinter;
    }

    @PostConstruct
    private void start() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @PreDestroy
    private void terminate() {
        terminate = true;
    }

//    @Scheduled(cron = "* 55 23 * * ?")
//    @Scheduled(cron = "* * * * * ?")
//    private void printDailyReport() {
//        try {
//            fiscalPrinter.printDailyReport();
//        } catch (Exception fpe) {
//            fpe.printStackTrace(System.out);
//        }
//    }
    @Override
    public void run() {
        while (!terminate) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }

            long time = System.currentTimeMillis();
            String slipToPrint = slipTools.findSlipToPrint(printerName);
            System.out.println("Znalezienie paragonu do druku: " + (System.currentTimeMillis() - time) + " ms.");
            if (slipToPrint != null) {
                System.out.println("Paragon do druku: " + slipToPrint);
                try {
                    time = System.currentTimeMillis();
                    Slip readedSlip = slipTools.readSlip(slipToPrint);
                    System.out.println("Odczytanie paragonu do druku: " + (System.currentTimeMillis() - time) + " ms.");
                    if (readedSlip != null) {
                        synchronized (this) {
                            time = System.currentTimeMillis();
                            fiscalPrinter.print(readedSlip);
                            System.out.println("Wydruk paragonu: " + (System.currentTimeMillis() - time) + " ms.");
                        }
                        slipTools.markAsPrinted(slipToPrint);
                    }
                    //System.err.println("Wydrukowano: " + slipToPrint);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    slipTools.markAsErrored(slipToPrint, e.getMessage());
                }
            }

            String invoiceToPrint = invoiceTools.findInvoiceToPrint(printerName);
            if (invoiceToPrint != null) {
                System.err.println("Paragon do druku: " + invoiceToPrint);
                try {
                    Invoice readedSlip = invoiceTools.readInvoice(invoiceToPrint);
                    if (readedSlip != null) {
                        synchronized (this) {
                            fiscalPrinter.print(readedSlip);
                        }
                        invoiceTools.markAsPrinted(invoiceToPrint);
                    }
                    //System.err.println("Wydrukowano: " + invoiceToPrint);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    invoiceTools.markAsErrored(invoiceToPrint, e.getMessage());
                }
            }
        }
    }
}
