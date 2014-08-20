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
import name.prokop.bart.fps.datamodel.Slip;
import name.prokop.bart.fps.datamodel.SlipPayment;
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
public class SlipDaoImpl implements SlipDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlipDaoImpl.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void markAsErrored(String slipToPrint, String error) {
        if (error == null) {
            error = "...null...";
        }
        String sql = "UPDATE SLIPS SET STAGE=-1 WHERE EXTERNALREFERENCE='" + slipToPrint + "';";
        jdbcTemplate.execute(sql);
        if (error.length() > 110) {
            error = error.substring(0, 110);
        }
        sql = "UPDATE SLIPS SET ERROR='" + error + "' WHERE EXTERNALREFERENCE='" + slipToPrint + "'";
        jdbcTemplate.execute(sql);
    }

    @Override
    public void markAsPrinted(String slipToPrint) {
        jdbcTemplate.execute("UPDATE SLIPS SET PRINTED_TS=now() WHERE EXTERNALREFERENCE='" + slipToPrint + "';");
        jdbcTemplate.execute("UPDATE SLIPS SET STAGE=2 WHERE EXTERNALREFERENCE='" + slipToPrint + "'");
        jdbcTemplate.execute("UPDATE SLIPS SET ERROR='Wydrukowany' WHERE EXTERNALREFERENCE='" + slipToPrint + "'");
    }

    @Override
    public String findSlipToPrint(String printerName) {
        String retVal;
        String sql = "SELECT EXTERNALREFERENCE FROM SLIPS WHERE STAGE=0 AND CASHBOXLOGO='" + printerName + "' LIMIT 1;";
        try {
            retVal = jdbcTemplate.queryForObject(sql, String.class);
        } catch (DataAccessException dae) {
            LOGGER.info("No slip to print");
            return null;
        }
        if (retVal != null) {
            jdbcTemplate.execute("UPDATE SLIPS SET STAGE=1 WHERE EXTERNALREFERENCE='" + retVal + "'");
            jdbcTemplate.execute("UPDATE SLIPS SET ERROR='Trwa drukowanie' WHERE EXTERNALREFERENCE='" + retVal + "'");
            System.out.println("Odczytano do wydruku: " + retVal);
        }
        return retVal;
    }

    @Override
    public Slip readSlip(String slipToPrint) {
        Slip retVal;

        String sql = "SELECT * FROM SLIPS WHERE EXTERNALREFERENCE='" + slipToPrint + "';";
        retVal = jdbcTemplate.queryForObject(sql, new RowMapper<Slip>() {
            @Override
            public Slip mapRow(ResultSet rs, int rowNum) throws SQLException {
                Slip slip = new Slip();
                slip.setReference(rs.getString("EXTERNALREFERENCE"));
                slip.setCashierName(rs.getString("CASHIERNAME"));
                slip.setCashbox(rs.getString("CASHBOXLOGO"));
                return slip;
            }
        });
        sql = "SELECT ID FROM SLIPS WHERE EXTERNALREFERENCE='" + slipToPrint + "';";
        int slipId = jdbcTemplate.queryForInt(sql);

        sql = "SELECT * FROM SLIPLINES WHERE FKID_SLIP=" + slipId + ";";
        for (Map<String, Object> line : jdbcTemplate.queryForList(sql)) {
            retVal.addLine(((String) line.get("NAME")).trim(),
                    (double) line.get("AMOUNT"),
                    (double) line.get("PRICE"),
                    VATRate.valueOf((String) line.get("TAXRATE")),
                    DiscountType.values()[(int) line.get("DISCOUNT_TYPE")],
                    (double) line.get("DISCOUNT"));
        }

        sql = "SELECT * FROM SLIPPAYMENTS WHERE FKID_SLIP=" + slipId + ";";
        for (Map<String, Object> payment : jdbcTemplate.queryForList(sql)) {
            String name = (String) payment.get("NAME");
            if (name != null) {
                name = name.trim();
            }

            retVal.addPayment(SlipPayment.PaymentType.valueOf(((String) payment.get("FORM")).trim()),
                    (double) payment.get("AMOUNT"), name);
        }

        return retVal;
    }

    @Override
    public void saveSlip(Slip slip) {
        String sql;
        sql = "INSERT INTO SLIPS (CASHBOXLOGO, EXTERNALREFERENCE, CASHIERNAME) VALUES ('"
                + slip.getCashbox() + "', '"
                + slip.getReference() + "', '"
                + slip.getCashierName() + "');";
        jdbcTemplate.execute(sql);
        sql = "SELECT ID FROM SLIPS WHERE EXTERNALREFERENCE='" + slip.getReference() + "'";
        int slipId = jdbcTemplate.queryForInt(sql);
        for (int i = 0; i < slip.getNoOfLines(); i++) {
            SaleLine sl = slip.getLine(i);
            sql = "INSERT INTO SLIPLINES (FKID_SLIP, NAME, AMOUNT, PRICE, TAXRATE, DISCOUNT_TYPE, DISCOUNT) VALUES (" + slipId
                    + ", '" + sl.getName() + "', " + sl.getAmount() + ", " + sl.getPrice() + ", '" + sl.getTaxRate().name() + "', " + sl.getDiscountType().ordinal() + ", " + sl.getDiscount() + ");";
            jdbcTemplate.execute(sql);
        }

        double amount;
        amount = slip.getPaymentAmount(SlipPayment.PaymentType.Cash);
        if (amount != 0.0) {
            jdbcTemplate.execute("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
                    + "VALUES (" + slipId + ", " + amount + ", "
                    + ((slip.getPaymentName(SlipPayment.PaymentType.Cash) == null) ? ("NULL") : ("'" + slip.getPaymentName(SlipPayment.PaymentType.Cash) + "'"))
                    + ", '" + SlipPayment.PaymentType.Cash + "');");
        }
        amount = slip.getPaymentAmount(SlipPayment.PaymentType.CreditCard);
        if (amount != 0.0) {
            jdbcTemplate.execute("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
                    + "VALUES (" + slipId + ", " + amount + ", '"
                    + slip.getPaymentName(SlipPayment.PaymentType.CreditCard) + "', '" + SlipPayment.PaymentType.CreditCard + "');");
        }
        amount = slip.getPaymentAmount(SlipPayment.PaymentType.Cheque);
        if (amount != 0.0) {
            jdbcTemplate.execute("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
                    + "VALUES (" + slipId + ", " + amount + ", '"
                    + slip.getPaymentName(SlipPayment.PaymentType.Cheque) + "', '" + SlipPayment.PaymentType.Cheque + "');");
        }
        amount = slip.getPaymentAmount(SlipPayment.PaymentType.Voucher);
        if (amount != 0.0) {
            jdbcTemplate.execute("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
                    + "VALUES (" + slipId + ", " + amount + ", '" + slip.getPaymentName(SlipPayment.PaymentType.Voucher) + "', '" + SlipPayment.PaymentType.Voucher + "');");
        }
        amount = slip.getPaymentAmount(SlipPayment.PaymentType.Other);
        if (amount != 0.0) {
            jdbcTemplate.execute("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
                    + "VALUES (" + slipId + ", " + amount + ", '"
                    + slip.getPaymentName(SlipPayment.PaymentType.Other) + "', '" + SlipPayment.PaymentType.Other + "');");
        }
        amount = slip.getPaymentAmount(SlipPayment.PaymentType.Credit);
        if (amount != 0.0) {
            jdbcTemplate.execute("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
                    + "VALUES (" + slipId + ", " + amount + ", '"
                    + slip.getPaymentName(SlipPayment.PaymentType.Credit) + "', '" + SlipPayment.PaymentType.Credit + "');");
        }
    }
}
