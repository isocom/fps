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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import name.prokop.bart.fps.util.BPMath;
import name.prokop.bart.fps.util.StringGenerator;

/**
 *
 * @author Bart
 */
public class Invoice {

    /**
     * Generuje testowy paragon, z obrotem 4 gr. i VAT wynoszącym 0,00 zł
     *
     * @return Przykladowy paragon
     */
    public static Invoice getTestInvoice() {
        Invoice slip = new Invoice();
        slip.setReference("R-k " + StringGenerator.generateRandomNumericId(6));
        slip.setCashbox("BP01");
        slip.setCashierName("Bartek Prokop");
        slip.addLine("Towar.22", 1, 0.01, VATRate.VAT22);
//        slip.addLine("Towar.07RK", 1, 0.02, VATRate.VAT07, DiscountType.AmountDiscount, 0.01);
//        slip.addLine("Towar.07RP", 1, 0.02, VATRate.VAT07, DiscountType.RateDiscount, 0.5);
        slip.addLine("Towar.07", 1, 0.01, VATRate.VAT07);
//        slip.addLine("Test_03", 1, 0.01, VATRate.VAT03);
        slip.addLine("Towar.00", 1, 0.01, VATRate.VAT00);
        slip.addLine("Towar.ZW", 1, 0.01, VATRate.VATzw);
        return slip;
    }

    /**
     * Generuje przyk┼éadowy paragon, do cel├│w czysto testowych
     *
     * @return Przyk┼éadowy paragon
     */
    public static Invoice getSampleSlip() {
        Invoice slip = new Invoice();

        // numer zewnetrzny paragonu z waszej aplikacji
        slip.setReference("R-k " + StringGenerator.generateRandomNumericId(6));

        // poszczególne linijki paragonu
        slip.addLine("Deska drewniania", 0.999, 22.13, VATRate.VAT22);
        //slip.addLine("Lejek metalowy", 1.0, 18.01, VATRate.VAT07, DiscountType.AmountDiscount, 18.0);
        slip.addLine("Lejek metalowy", 1.0, 18.01, VATRate.VAT07, DiscountType.RateDiscount, 0.5);
        slip.addLine("Pampers", 1.0, 39.99, VATRate.VAT00);
        //linijka wywo┼éa wyj─ůtek - cena bruto == zero
        //slip.addLine("Drut 15 mm", 0.123456, 0.01, SlipLine.VATRate.VATzw);
        slip.addLine("LiteryĄĆĘÓąćęó", 123, 0.12, VATRate.VAT22);
        slip.addLine("Cement", 9.99, 99.99, VATRate.VAT22);
        slip.addLine("Pustak", 9.99, 0.01, VATRate.VAT07);
        slip.addLine("Beton", 48.0011, 1005.01, VATRate.VAT07);
        //slip.addLine("Mleko szkolne", 5, 3, SlipLine.VATRate.VAT03);
        slip.addLine("Paliwo", 34.87, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.871, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.872, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.873, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.874, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.875, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.876, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.877, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.878, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.879, 4.37, VATRate.VAT07);
        slip.addLine("Paliwo", 34.880, 4.37, VATRate.VAT07);

        slip.setCashierName("Jacek Bielarski");
        slip.setCashbox("BP01");
        return slip;
    }

    /**
     * Dodaje pozycje do paragonu
     *
     * @param name Nazwa towaru
     * @param amount Ilość towaru
     * @param price Cena brutto towaru
     * @param taxRate Stawka VAT na towar
     */
    public void addLine(String name, double amount, double price, VATRate taxRate) {
        addLine(new SaleLine(name, amount, price, taxRate));
    }

    public void addLine(String name, double amount, double price, VATRate taxRate, DiscountType discountType, double discount) {
        addLine(new SaleLine(name, amount, price, taxRate, discountType, discount));
    }

    public void addLine(SaleLine slipLine) {
        slipLines.add(slipLine);
    }

    /**
     * Getter for property noOfLines.
     *
     * @return Value of property noOfLines.
     */
    public int getNoOfLines() {
        return slipLines.size();
    }

    /**
     * Indexed getter for property payment.
     *
     * @param index Index of the property.
     * @return Value of the property at <CODE>index</CODE>.
     */
    public SaleLine getLine(int index) {
        return slipLines.get(index);
    }

    /**
     * Służy pozyskaniu wartości brutto całego paragonu
     *
     * @return Wartość brutto całego paragonu
     */
    public double getTotal() {
        double sum = 0.0;
        for (int i = 0; i < slipLines.size(); i++) {
            sum += slipLines.get(i).getTotal();
        }
        return BPMath.round(sum + 0.000001, 2);
    }

    /**
     * Enum dla okreslenia stanu danego paragonu
     */
    public enum PrintingState {

        /**
         * Nowo utworzony paragom
         */
        Created,
        /**
         * LOCK - podczas drukowania
         */
        DuringPrinting,
        /**
         * Wydrukowany - wszystko w porzadku jest
         */
        Printed,
        /**
         * Paragon z bledem
         */
        Errored;

        @Override
        public String toString() {
            switch (this) {
                case Created:
                    return "Nowy";
                case DuringPrinting:
                    return "Drukuje sie";
                case Errored:
                    return "Bledny";
                case Printed:
                    return "Wydrukowany";
                default:
                    throw new IllegalStateException();
            }
        }
    }

    @Override
    public String toString() {
        String retVal = "Slip: Reference: " + reference + " Kasa: " + getCashbox() + "\n";
        for (SaleLine sl : slipLines) {
            retVal += sl + "\n";
        }
        retVal += "Suma paragonu: " + getTotal() + " Kasjer: " + getCashierName();
        return retVal;
    }
    /**
     * referencja dla paragonu. musi byc unikalna
     */
    private String reference;
    /**
     * Data utworzenia paragonu
     */
    private Date created = new Date();
    /**
     * Data wydrukowania paragonu (jego fiskalizacji)
     */
    private Date printed = null;
    /**
     * Nazwa kasy - pole na paragonie określające nazwę kasy fiskalnej. Używane
     * również do rozróżnienia gdzie należy kolejkować wydruki.
     */
    private String cashbox;
    /**
     * Imie i Nazwisko kasjera
     */
    private String cashierName;
    /**
     * Linijki paragonu
     */
    private List<SaleLine> slipLines = new ArrayList<>();
    /**
     * Stan paragonu
     */
    private PrintingState printingState = PrintingState.Created;
    /**
     * Opis bledu w paragonie
     */
    private String errorNote = PrintingState.Created.toString();

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getPrinted() {
        return printed;
    }

    public void setPrinted(Date printed) {
        this.printed = printed;
    }

    public List<SaleLine> getSlipLines() {
        return slipLines;
    }

    public void setSlipLines(List<SaleLine> slipLines) {
        this.slipLines = slipLines;
    }

    public String getCashbox() {
        return cashbox;
    }

    public void setCashbox(String cashbox) {
        if (cashbox != null) {
            cashbox = cashbox.trim();
        }
        this.cashbox = cashbox;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        if (cashierName != null) {
            cashierName = cashierName.trim();
        }
        this.cashierName = cashierName;
    }

    public PrintingState getPrintingState() {
        return printingState;
    }

    public void setPrintingState(PrintingState printingState) {
        this.printingState = printingState;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        if (reference != null) {
            reference = reference.trim();
        }
        this.reference = reference;
    }

    public String getErrorNote() {
        return errorNote;
    }

    public void setErrorNote(String errorNote) {
        if (errorNote != null) {
            errorNote = errorNote.trim();
        }
        this.errorNote = errorNote;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(String paymentDue) {
        this.paymentDue = paymentDue;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    /**
     * "813-188-60-14"
     */
    private String nip;
    /**
     * "nigdy"
     */
    private String paymentDue;
    /**
     * "przelew"
     */
    private String paymentType;
    /**
     * "Firma\nul. Przemysłowa 9A\n35-111 Rzeszów"
     */
    private String header;
}
