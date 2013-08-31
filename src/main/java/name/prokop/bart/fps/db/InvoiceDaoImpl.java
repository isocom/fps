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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import name.prokop.bart.fps.datamodel.DiscountType;
import name.prokop.bart.fps.datamodel.SaleLine;
import name.prokop.bart.fps.datamodel.Invoice;
import name.prokop.bart.fps.datamodel.VATRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 *
 * @author prokob01
 */
@Repository
@Lazy
public class InvoiceDaoImpl implements InvoiceDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(InvoiceDaoImpl.class);
    @Autowired
    @Qualifier("slipDAOjdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public void markAsErrored(String invoiceToPrint, String error) {
        if (error == null) {
            error = "...null...";
        }
        String sql = "UPDATE INVOICES SET STAGE=-1 WHERE EXTERNALREFERENCE='" + invoiceToPrint + "';";
        jdbcTemplate.execute(sql);
        if (error.length() > 110) {
            error = error.substring(0, 110);
        }
        sql = "UPDATE INVOICES SET ERROR='" + error + "' WHERE EXTERNALREFERENCE='" + invoiceToPrint + "'";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void markAsPrinted(String invoiceToPrint) {
        jdbcTemplate.execute("UPDATE INVOICES SET PRINTED_TS=now() WHERE EXTERNALREFERENCE='" + invoiceToPrint + "';");
        jdbcTemplate.execute("UPDATE INVOICES SET STAGE=2 WHERE EXTERNALREFERENCE='" + invoiceToPrint + "'");
        jdbcTemplate.execute("UPDATE INVOICES SET ERROR='Wydrukowany' WHERE EXTERNALREFERENCE='" + invoiceToPrint + "'");
    }

    @Override
    public String findInvoiceToPrint(String printerName) {
        String retVal;
        String sql = "SELECT EXTERNALREFERENCE FROM INVOICES WHERE STAGE=0 AND CASHBOXLOGO='" + printerName + "' LIMIT 1;";
        try {
            retVal = jdbcTemplate.queryForObject(sql, String.class);
        } catch (DataAccessException dae) {
            LOGGER.info("No invoice to print");
            return null;
        }
        if (retVal != null) {
            jdbcTemplate.execute("UPDATE INVOICES SET STAGE=1 WHERE EXTERNALREFERENCE='" + retVal + "'");
            jdbcTemplate.execute("UPDATE INVOICES SET ERROR='Trwa drukowanie' WHERE EXTERNALREFERENCE='" + retVal + "'");
            System.out.println("Odczytano do wydruku: " + retVal);
        }
        return retVal;
    }

    @Override
    public Invoice readInvoice(String invoiceToPrint) {
        Invoice retVal;

        String sql = "SELECT * FROM INVOICES WHERE EXTERNALREFERENCE='" + invoiceToPrint + "';";
        retVal = jdbcTemplate.queryForObject(sql, new RowMapper<Invoice>() {
            @Override
            public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
                Invoice invoice = new Invoice();
                invoice.setReference(rs.getString("EXTERNALREFERENCE"));
                invoice.setCashierName(rs.getString("CASHIERNAME"));
                invoice.setHeader(rs.getString("HEADER"));
                invoice.setNip(rs.getString("NIP"));
                invoice.setPaymentDue(rs.getString("PMTDUE"));
                invoice.setPaymentType(rs.getString("PMTTYPE"));
                invoice.setCashbox(rs.getString("CASHBOXLOGO"));
                return invoice;
            }
        });
        sql = "SELECT ID FROM INVOICES WHERE EXTERNALREFERENCE='" + invoiceToPrint + "';";
        int invoiceId = jdbcTemplate.queryForInt(sql);

        sql = "SELECT * FROM INVOICELINES WHERE FKID_INVOICE=" + invoiceId + ";";
        for (Map<String, Object> line : jdbcTemplate.queryForList(sql)) {
            retVal.addLine(((String) line.get("NAME")).trim(),
                    (double) line.get("AMOUNT"),
                    (double) line.get("PRICE"),
                    VATRate.valueOf((String) line.get("TAXRATE")),
                    DiscountType.values()[(int) line.get("DISCOUNT_TYPE")],
                    (double) line.get("DISCOUNT"));
        }

        return retVal;
    }

    @Override
    public void saveInvoice(Invoice invoice) {
        String sql;
        sql = "INSERT INTO INVOICES (CASHBOXLOGO, EXTERNALREFERENCE, HEADER, NIP, PMTDUE, PMTTYPE, CASHIERNAME) VALUES ('"
                + invoice.getCashbox() + "', '"
                + invoice.getReference() + "', '"
                + invoice.getHeader() + "', '"
                + invoice.getNip() + "', '"
                + invoice.getPaymentDue() + "', '"
                + invoice.getPaymentType() + "', '"
                + invoice.getCashierName() + "');";
        jdbcTemplate.execute(sql);
        sql = "SELECT ID FROM INVOICES WHERE EXTERNALREFERENCE='" + invoice.getReference() + "'";
        int invoiceId = jdbcTemplate.queryForInt(sql);
        for (int i = 0; i < invoice.getNoOfLines(); i++) {
            SaleLine sl = invoice.getLine(i);
            sql = "INSERT INTO INVOICELINES (FKID_INVOICE, NAME, AMOUNT, PRICE, TAXRATE, DISCOUNT_TYPE, DISCOUNT) VALUES (" + invoiceId
                    + ", '" + sl.getName() + "', " + sl.getAmount() + ", " + sl.getPrice() + ", '" + sl.getTaxRate().name() + "', " + sl.getDiscountType().ordinal() + ", " + sl.getDiscount() + ");";
            jdbcTemplate.execute(sql);
        }
    }
}
