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
import java.util.Random;
import name.prokop.bart.fps.util.BPMath;
import name.prokop.bart.fps.util.StringGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Bart
 */
public class Slip {

    /**
     * Generuje testowy paragon, z obrotem 1 gr. i VAT wynoszącym 0,00 zł
     *
     * @return Przykladowy paragon
     */
    public static Slip getOneCentSlip() {
        Slip slip = new Slip();
        slip.setReference("R-k 0123456789");
        slip.setCashbox("XX99");
        slip.setCashierName("Bartek Prokop");
        slip.addLine("Test drukarki", 1, 0.01, VATRate.VAT23);
        slip.addPayment(SlipPayment.PaymentType.Cash, 0.01, null);
        return slip;
    }

    /**
     * Generuje testowy paragon, z obrotem 4 gr. i VAT wynoszącym 0,00 zł
     *
     * @return Przykladowy paragon
     */
    public static Slip getTestSlip() {
        Slip slip = new Slip();
        slip.setReference("R-k " + StringGenerator.generateRandomNumericId(6));
        slip.setCashbox("XX01");
        slip.setCashierName("Bartek Prokop");
        slip.addLine("Towar.23", 1, 0.01, VATRate.VAT23);
        slip.addLine("Towar.08RK", 1, 0.02, VATRate.VAT08, DiscountType.AmountDiscount, 0.01);
        slip.addLine("Towar.08RP", 1, 0.02, VATRate.VAT08, DiscountType.RateDiscount, 0.5);
        slip.addLine("Towar.08", 1, 0.01, VATRate.VAT08);
        slip.addLine("Test_05", 1, 0.01, VATRate.VAT05);
        slip.addLine("Towar.00", 1, 0.01, VATRate.VAT00);
        slip.addLine("Towar.ZW", 1, 0.01, VATRate.VATzw);
        return slip;
    }

    public static Slip getTestNoDiscountSlip() {
        Slip slip = new Slip();
        slip.setReference("R-k " + StringGenerator.generateRandomNumericId(6));
        slip.setCashbox("XX01");
        slip.setCashierName("Bartek Prokop");
        slip.addLine("Towar.23", 1, 0.01, VATRate.VAT23);
        slip.addLine("Towar.08", 1, 0.01, VATRate.VAT08);
        slip.addLine("Test_05", 1, 0.01, VATRate.VAT05);
        slip.addLine("Towar.00", 1, 0.01, VATRate.VAT00);
        slip.addLine("Towar.ZW", 1, 0.01, VATRate.VATzw);
        return slip;
    }

    /**
     * Generuje przyk┼éadowy paragon, do cel├│w czysto testowych
     *
     * @return Przyk┼éadowy paragon
     */
    public static Slip getSampleSlip() {
        Random r = new Random();
        Slip slip = new Slip();

        // numer zewnetrzny paragonu z waszej aplikacji
        slip.setReference("R-k " + StringGenerator.generateRandomNumericId(6));

        for (int i = 1; i <= 2; i++) {
            slip.addLine("Losowy1", BPMath.round(r.nextInt(10000) / 100.0 + 0.01, 2), 99.99, VATRate.VAT23);
            slip.addLine("Losowy2", BPMath.round(r.nextInt(10000) / 100.0 + 0.01, 2), BPMath.round(r.nextInt(10000) / 100.0 + 0.01, 2), VATRate.VAT23);
            slip.addLine("Losowy RABAT", BPMath.round(r.nextInt(10000) / 100.0 + 0.01, 2), BPMath.round(r.nextInt(10000) / 100.0 + 50, 2), VATRate.VAT23, DiscountType.AmountDiscount, BPMath.round(r.nextInt(5000) / 100.0, 2));
            slip.addLine("Losowy3", 9.99, r.nextInt(10000) / 100.0 + 0.01, VATRate.VAT23);
            slip.addLine("Losowy4", BPMath.round(r.nextInt(10000) / 10000.0 + 0.01, 4), BPMath.round(r.nextInt(10000) / 100.0 + 1.01, 2), VATRate.VAT08);
            slip.addLine("Losowy4", BPMath.round(r.nextInt(10000) / 10000.0 + 0.01, 4), BPMath.round(r.nextInt(10000) / 100.0 + 1.01, 2), VATRate.VAT08);
            slip.addLine("Losowy4", BPMath.round(r.nextInt(10000) / 10000.0 + 0.01, 4), BPMath.round(r.nextInt(10000) / 100.0 + 1.01, 2), VATRate.VAT08);
            slip.addLine("Losowy4", BPMath.round(r.nextInt(10000) / 10000.0 + 0.01, 4), BPMath.round(r.nextInt(10000) / 100.0 + 1.01, 2), VATRate.VAT08);
            slip.addLine("LosowyZERO", BPMath.round(r.nextInt(10000) / 10000.0 + 0.01, 4), BPMath.round(r.nextInt(10000) / 100.0 + 1.01, 2), VATRate.VAT00);
            slip.addLine("LosowyZW", BPMath.round(r.nextInt(10000) / 10000.0 + 0.01, 4), BPMath.round(r.nextInt(10000) / 100.0 + 1.01, 2), VATRate.VATzw);
        }
        slip.setCashierName("Jacek Bielarski");
        slip.setCashbox("BP01");

        // mozna nie uzyc formy platnosci w ogole
        slip.addPayment(SlipPayment.PaymentType.Voucher, slip.getTotal(), "Karnet 653214");

        return slip;
    }

    public Slip() {
    }

    public Slip(JSONObject json) {
        try {
            this.cashbox = (json.has("cashbox") && !json.isNull("cashbox")) ? json.getString("cashbox") : null;
            this.cashierName = (json.has("cashierName") && !json.isNull("cashierName")) ? json.getString("cashierName") : null;
            this.created = (json.has("created") && !json.isNull("created")) ? new Date(json.getLong("created")) : null;
            this.errorNote = (json.has("errorNote") && !json.isNull("errorNote")) ? json.getString("errorNote") : null;
            this.printed = (json.has("printed") && !json.isNull("printed")) ? new Date(json.getLong("printed")) : null;
            this.printingState = (json.has("printingState") && !json.isNull("printingState")) ? PrintingState.valueOf(json.getString("printingState")) : null;
            this.reference = (json.has("reference") && !json.isNull("reference")) ? json.getString("reference") : null;
            JSONArray slipLinesJson = json.getJSONArray("slipLines");
            for (int i = 0; i < slipLinesJson.length(); i++) {
                JSONObject slipLineJson = slipLinesJson.getJSONObject(i);
                SaleLine saleLine = new SaleLine();
                saleLine.setAmount(slipLineJson.getDouble("amount"));
                saleLine.setDiscount(slipLineJson.getDouble("discount"));
                saleLine.setDiscountType(DiscountType.valueOf(slipLineJson.getString("discountType")));
                saleLine.setName(slipLineJson.getString("name"));
                saleLine.setPrice(slipLineJson.getDouble("price"));
                saleLine.setTaxRate(VATRate.valueOf(slipLineJson.getString("taxRate")));
                addLine(saleLine);
            }
            JSONArray slipPaymentsJson = json.getJSONArray("slipPayments");
            for (int i = 0; i < slipPaymentsJson.length(); i++) {
                JSONObject slipPaymentJson = slipPaymentsJson.getJSONObject(i);
                SlipPayment slipPayment = new SlipPayment();
                slipPayment.setAmount(slipPaymentJson.getDouble("amount"));
                slipPayment.setName(slipPaymentJson.getString("name"));
                slipPayment.setType(SlipPayment.PaymentType.valueOf(slipPaymentJson.getString("type")));
                addPayment(slipPayment);
            }
        } catch (JSONException je) {
            throw new RuntimeException(je);
        }
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("cashbox", this.cashbox);
            json.put("cashierName", this.cashierName);
            if (created != null) {
                json.put("created", this.created.getTime());
            }
            json.put("errorNote", this.errorNote);
            if (printed != null) {
                json.put("printed", this.printed.getTime());
            }
            if (printingState != null) {
                json.put("printingState", this.printingState.name());
            }
            json.put("reference", this.reference);
            JSONArray slipLinesJson = new JSONArray();
            for (SaleLine saleLine : slipLines) {
                JSONObject slipLineJson = new JSONObject();
                slipLineJson.put("amount", saleLine.getAmount());
                slipLineJson.put("discount", saleLine.getDiscount());
                slipLineJson.put("discountType", saleLine.getDiscountType().name());
                slipLineJson.put("name", saleLine.getName());
                slipLineJson.put("price", saleLine.getPrice());
                slipLineJson.put("taxRate", saleLine.getTaxRate().name());
                slipLinesJson.put(slipLineJson);
            }
            json.put("slipLines", slipLinesJson);
            JSONArray slipPaymentsJson = new JSONArray();
            for (SlipPayment slipPayment : slipPayments) {
                JSONObject slipPaymentJson = new JSONObject();
                slipPaymentJson.put("amount", slipPayment.getAmount());
                slipPaymentJson.put("name", slipPayment.getName());
                slipPaymentJson.put("type", slipPayment.getType().name());
                slipPaymentsJson.put(slipPaymentJson);
            }
            json.put("slipPayments", slipPaymentsJson);
            return json;
        } catch (JSONException je) {
            throw new RuntimeException(je);
        }
    }

    /**
     * Dodaje pozycje do paragonu
     *
     * @param name Nazwa towaru
     * @param amount Ilość towaru
     * @param price Cena brutto towaru
     * @param taxRate Stawka VAT na towar
     */
    public final void addLine(String name, double amount, double price, VATRate taxRate) {
        addLine(new SaleLine(name, amount, price, taxRate));
    }

    public final void addLine(String name, double amount, double price, VATRate taxRate, DiscountType discountType, double discount) {
        addLine(new SaleLine(name, amount, price, taxRate, discountType, discount));
    }

    public final void addLine(SaleLine slipLine) {
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

    public boolean isUsingPayments() {
        return slipPayments.size() > 0;
    }

    /**
     * Pozwala ustawić formy płatnosci do wydruku na paragonie fiskalnym
     *
     * @param paymentForm forma płatności
     * @param amount kwota płatnosci
     * @param name dodatkowy opis (w przypadku formy Cash ignorowany)
     */
    public final void addPayment(SlipPayment.PaymentType paymentForm, double amount, String name) {
        if (name != null) {
            if (name.length() > 16) {
                name = name.substring(0, 16);
            }
            name = name.trim();
        }

        SlipPayment slipPayment = getPayment(paymentForm);
        if (slipPayment == null) {
            slipPayment = new SlipPayment();
            slipPayments.add(slipPayment);
            slipPayment.setType(paymentForm);
            slipPayment.setName(name);
        }

        slipPayment.setAmount(BPMath.round(slipPayment.getAmount() + amount, 2));
    }

    public final void addPayment(SlipPayment payment) {
        SlipPayment slipPayment = getPayment(payment.getType());
        if (slipPayment == null) {
            slipPayments.add(payment);
        } else {
            slipPayment.setAmount(slipPayment.getAmount() + payment.getAmount());
        }
    }

    /**
     * Zwraca uprzednio ustawione formy płatności
     *
     * @param type forma płatności
     * @return wartość danej formy płatności
     */
    public SlipPayment getPayment(SlipPayment.PaymentType type) {
        for (SlipPayment slipPayment : slipPayments) {
            if (slipPayment.getType() == type) {
                return slipPayment;
            }
        }
        return null;
    }

    /**
     * Zwraca uprzednio ustawione formy płatności
     *
     * @param paymentForm forma płatności
     * @return wartość danej formy płatności
     */
    public double getPaymentAmount(SlipPayment.PaymentType paymentForm) {
        SlipPayment sp = getPayment(paymentForm);
        if (sp != null) {
            return sp.getAmount();
        } else {
            return 0.0;
        }
    }

    /**
     * Podaje uprzednio zdefiniowane opisy dla poszczególnych form płatności
     *
     * @param paymentForm typ formy płatności
     * @return dodatkowe określenie dla danej formy płatności
     */
    public String getPaymentName(SlipPayment.PaymentType paymentForm) {
        SlipPayment sp = getPayment(paymentForm);
        if (sp != null) {
            return sp.getName();
        } else {
            return "";
        }
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
        return BPMath.roundCurrency(sum);


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
     * Powiazane zaplaty do paragonu
     */
    private List<SlipPayment> slipPayments = new ArrayList<>();
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

    public List<SlipPayment> getSlipPayments() {
        return slipPayments;
    }

    public void setSlipPayments(List<SlipPayment> slipPayments) {
        this.slipPayments = slipPayments;
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

    public static void main(String[] args) throws Exception {
        System.out.println(Slip.getSampleSlip().toJSONObject().toString(2));
    }
}
