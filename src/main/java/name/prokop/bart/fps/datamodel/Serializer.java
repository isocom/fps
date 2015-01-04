package name.prokop.bart.fps.datamodel;

import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

public class Serializer {

    public Slip decodeSlip(JSONObject json) {
        Slip slip = new Slip();
        slip.setCashbox((json.has("cashbox") && !json.isNull("cashbox")) ? json.getString("cashbox") : null);
        slip.setCashierName((json.has("cashierName") && !json.isNull("cashierName")) ? json.getString("cashierName") : null);
        slip.setCreated((json.has("created") && !json.isNull("created")) ? new Date(json.getLong("created")) : null);
        slip.setErrorNote((json.has("errorNote") && !json.isNull("errorNote")) ? json.getString("errorNote") : null);
        slip.setPrinted((json.has("printed") && !json.isNull("printed")) ? new Date(json.getLong("printed")) : null);
        slip.setPrintingState((json.has("printingState") && !json.isNull("printingState")) ? Slip.PrintingState.valueOf(json.getString("printingState")) : null);
        slip.setReference((json.has("reference") && !json.isNull("reference")) ? json.getString("reference") : null);
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
            slip.addLine(saleLine);
        }
        JSONArray slipPaymentsJson = json.getJSONArray("slipPayments");
        for (int i = 0; i < slipPaymentsJson.length(); i++) {
            JSONObject slipPaymentJson = slipPaymentsJson.getJSONObject(i);
            SlipPayment slipPayment = new SlipPayment();
            slipPayment.setAmount(slipPaymentJson.getDouble("amount"));
            if (slipPaymentJson.has("name")) {
                slipPayment.setName(slipPaymentJson.getString("name"));
            }
            slipPayment.setType(SlipPayment.PaymentType.valueOf(slipPaymentJson.getString("type")));
            slip.addPayment(slipPayment);
        }
        return slip;
    }

    public JSONObject toJSON(Slip slip) {
        JSONObject json = new JSONObject();
        json.put("cashbox", slip.getCashbox());
        json.put("cashierName", slip.getCashierName());
        if (slip.getCreated() != null) {
            json.put("created", slip.getCreated().getTime());
        }
        json.put("errorNote", slip.getErrorNote());
        if (slip.getPrinted() != null) {
            json.put("printed", slip.getPrinted().getTime());
        }
        if (slip.getPrintingState() != null) {
            json.put("printingState", slip.getPrintingState().name());
        }
        json.put("reference", slip.getReference());
        JSONArray slipLinesJson = new JSONArray();
        for (SaleLine saleLine : slip.getSlipLines()) {
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
        for (SlipPayment slipPayment : slip.getSlipPayments()) {
            JSONObject slipPaymentJson = new JSONObject();
            slipPaymentJson.put("amount", slipPayment.getAmount());
            slipPaymentJson.put("name", slipPayment.getName());
            slipPaymentJson.put("type", slipPayment.getType().name());
            slipPaymentsJson.put(slipPaymentJson);
        }
        json.put("slipPayments", slipPaymentsJson);
        return json;
    }

    public void validate(Slip slip) throws IllegalArgumentException {
        if (Toolbox.isNullOrEmpty(slip.getCashbox())) {
            throw new IllegalArgumentException("cashbox is null or empty");
        }
        if (Toolbox.isNullOrEmpty(slip.getCashierName())) {
            throw new IllegalArgumentException("cashierName is null or empty");
        }
        if (Toolbox.isNullOrEmpty(slip.getReference())) {
            throw new IllegalArgumentException("reference is null or empty");
        }
        if (slip.getSlipLines().isEmpty()) {
            throw new IllegalArgumentException("There are no lines");
        }
        if (slip.getTotal() < 0.0001) {
            throw new IllegalArgumentException("total value less than 0.01");
        }
    }

    public Invoice decodeInvoice(JSONObject json) {
        Invoice invoice = new Invoice();
        invoice.setCashbox((json.has("cashbox") && !json.isNull("cashbox")) ? json.getString("cashbox") : null);
        invoice.setCashierName((json.has("cashierName") && !json.isNull("cashierName")) ? json.getString("cashierName") : null);
        invoice.setCreated((json.has("created") && !json.isNull("created")) ? new Date(json.getLong("created")) : null);
        invoice.setErrorNote((json.has("errorNote") && !json.isNull("errorNote")) ? json.getString("errorNote") : null);
        invoice.setHeader((json.has("header") && !json.isNull("header")) ? json.getString("header") : null);
        invoice.setNip((json.has("nip") && !json.isNull("nip")) ? json.getString("nip") : null);
        invoice.setPaymentDue((json.has("paymentDue") && !json.isNull("paymentDue")) ? json.getString("paymentDue") : null);
        invoice.setPaymentType((json.has("paymentType") && !json.isNull("paymentType")) ? json.getString("paymentType") : null);
        invoice.setPrinted((json.has("printed") && !json.isNull("printed")) ? new Date(json.getLong("printed")) : null);
        invoice.setPrintingState((json.has("printingState") && !json.isNull("printingState")) ? Invoice.PrintingState.valueOf(json.getString("printingState")) : null);
        invoice.setReference((json.has("reference") && !json.isNull("reference")) ? json.getString("reference") : null);
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
            invoice.addLine(saleLine);
        }
        return invoice;
    }

    public JSONObject toJSON(Invoice invoice) {
        JSONObject json = new JSONObject();
        if (invoice.getCashbox() != null) {
            json.put("cashbox", invoice.getCashbox());
        }
        if (invoice.getCashierName() != null) {
            json.put("cashierName", invoice.getCashierName());
        }
        if (invoice.getCreated() != null) {
            json.put("created", invoice.getCreated().getTime());
        }
        if (invoice.getHeader() != null) {
            json.put("header", invoice.getHeader());
        }
        if (invoice.getNip() != null) {
            json.put("nip", invoice.getNip());
        }
        if (invoice.getPaymentDue() != null) {
            json.put("paymentDue", invoice.getPaymentDue());
        }
        if (invoice.getPaymentType() != null) {
            json.put("paymentType", invoice.getPaymentType());
        }
        if (invoice.getErrorNote() != null) {
            json.put("errorNote", invoice.getErrorNote());
        }
        if (invoice.getPrinted() != null) {
            json.put("printed", invoice.getPrinted().getTime());
        }
        if (invoice.getPrintingState() != null) {
            json.put("printingState", invoice.getPrintingState().name());
        }
        if (invoice.getReference() != null) {
            json.put("reference", invoice.getReference());
        }
        JSONArray slipLinesJson = new JSONArray();
        for (SaleLine saleLine : invoice.getSlipLines()) {
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
        return json;
    }

    public void validate(Invoice invoice) throws IllegalArgumentException {
        if (Toolbox.isNullOrEmpty(invoice.getCashbox())) {
            throw new IllegalArgumentException("cashbox is null or empty");
        }
        if (Toolbox.isNullOrEmpty(invoice.getCashierName())) {
            throw new IllegalArgumentException("cashierName is null or empty");
        }
        if (Toolbox.isNullOrEmpty(invoice.getHeader())) {
            throw new IllegalArgumentException("header is null or empty");
        }
        if (Toolbox.isNullOrEmpty(invoice.getNip())) {
            throw new IllegalArgumentException("nip is null or empty");
        }
        if (Toolbox.isNullOrEmpty(invoice.getPaymentDue())) {
            throw new IllegalArgumentException("payment due is null or empty");
        }
        if (Toolbox.isNullOrEmpty(invoice.getPaymentType())) {
            throw new IllegalArgumentException("payment type is null or empty");
        }
        if (Toolbox.isNullOrEmpty(invoice.getReference())) {
            throw new IllegalArgumentException("reference is null or empty");
        }
        if (invoice.getSlipLines().isEmpty()) {
            throw new IllegalArgumentException("There are no lines");
        }
        if (invoice.getTotal() < 0.0001) {
            throw new IllegalArgumentException("total value less than 0.01");
        }
    }
}
