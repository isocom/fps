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

import name.prokop.bart.fps.datamodel.Slip;

/**
 *
 * @author bart
 */
public class MicrosoftSQL implements SlipDAO {

    @Override
    public void markAsErrored(String slipToPrint, String error) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void markAsPrinted(String slipToPrint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String findSlipToPrint(String printerName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Slip readSlip(String slipToPrint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void saveSlip(Slip slip) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    private DatabaseConnection db = null;
//    private static Logger logger = Logger.getLogger(MicrosoftSQL.class.getName());
//
//    public MicrosoftSQL(String connectionName) throws BartException {
//        try {
//            db = DatabaseConnectionFactory.getDatabaseConnection(connectionName);
//            db.getConncetion().setAutoCommit(false);
//            db.getConncetion().setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
//        } catch (SQLException e) {
//            logger.log(Level.WARNING, "Problem z serwerem SQL", e);
//            throw new BartException("Problem z podłączeniem do bazy danych", e);
//        }
//        System.out.println("Podłączono do bazy");
//    }
//
//    public void markAsErrored(String slipToPrint, String error) {
//        if (error == null) {
//            error = "...null...";
//        }
//        try {
//            db.sqlExec("UPDATE SLIPS SET STAGE=-1 WHERE EXTERNALREFERENCE='" + slipToPrint + "';");
//            if (error.length() > 110) {
//                error = error.substring(0, 110);
//            }
//            db.sqlExec("UPDATE SLIPS SET ERROR='" + error + "' WHERE EXTERNALREFERENCE='" + slipToPrint + "'");
//            db.getConncetion().commit();
//        } catch (SQLException e) {
//            FiscalPrinterServer.logger.log(Level.SEVERE, "Nie udaĹ‚o się oznaczyć paragonu jako błędnego.", e);
//            e.printStackTrace();
//        } finally {
//            try {
//                db.getConncetion().commit();
//            } catch (SQLException ex) {
//            }
//        }
//    }
//
//    public void markAsPrinted(String slipToPrint) {
//        try {
//            db.sqlExec("UPDATE SLIPS SET PRINTED_TS=getdate() WHERE EXTERNALREFERENCE='" + slipToPrint + "';");
//            db.sqlExec("UPDATE SLIPS SET STAGE=2 WHERE EXTERNALREFERENCE='" + slipToPrint + "'");
//            db.sqlExec("UPDATE SLIPS SET ERROR='Wydrukowany' WHERE EXTERNALREFERENCE='" + slipToPrint + "'");
//        } catch (SQLException e) {
//            FiscalPrinterServer.logger.log(Level.SEVERE, "Nie udało się oznaczyć paragonu jako wydrukowany.", e);
//            e.printStackTrace();
//        } finally {
//            try {
//                db.getConncetion().commit();
//            } catch (SQLException ex) {
//            }
//        }
//    }
//
//    public String findSlipToPrint(String printerName) {
//        String retVal = null;
//        try {
//            String sql = "SELECT EXTERNALREFERENCE FROM SLIPS WHERE STAGE=0 AND CASHBOXLOGO='" + printerName + "';";
//            ResultSet rs = db.getConncetion().createStatement().executeQuery(sql);
//            if (rs.next()) {
//                retVal = rs.getString("EXTERNALREFERENCE");
//            }
//            rs.getStatement().close();
//            rs.close();
//            if (retVal != null) {
//                db.sqlExec("UPDATE SLIPS SET STAGE=1 WHERE EXTERNALREFERENCE='" + retVal + "'");
//                db.sqlExec("UPDATE SLIPS SET ERROR='Trwa drukowanie' WHERE EXTERNALREFERENCE='" + retVal + "'");
//            }
//            db.getConncetion().commit();
//        } catch (SQLException e) {
//            FiscalPrinterServer.logger.log(Level.WARNING, "Nie udało się znaleść paragonu do druku", e);
//            e.printStackTrace();
//        }
//        if (retVal != null) {
//            System.out.println("Odczytano do wydruku: " + retVal);
//        }
//        return retVal;
//    }
//
//    public Slip readSlip(String slipToPrint) {
//        Slip retVal = new Slip();
//        try {
//            ResultSet rs = db.getConncetion().createStatement().executeQuery("SELECT * FROM SLIPS WHERE EXTERNALREFERENCE='" + slipToPrint + "';");
//            int slipId = 0;
//            if (rs.next()) {
//                retVal.setReference(rs.getString("EXTERNALREFERENCE"));
//                retVal.setCashierName(rs.getString("CASHIERNAME"));
//                retVal.setCashbox(rs.getString("CASHBOXLOGO"));
//                slipId = rs.getInt("ID");
//            }
//            rs.getStatement().close();
//            rs = db.getConncetion().createStatement().executeQuery("SELECT * FROM SLIPLINES WHERE FKID_SLIP=" + slipId + ";");
//            while (rs.next()) {
//                retVal.addLine(rs.getString("NAME"), rs.getDouble("AMOUNT"), rs.getDouble("PRICE"), VATRate.valueOf(rs.getString("TAXRATE")), DiscountType.values()[rs.getInt("DISCOUNT_TYPE")], rs.getDouble("DISCOUNT"));
//            }
//            rs.getStatement().close();
//            rs = db.getConncetion().createStatement().executeQuery("SELECT * FROM SLIPPAYMENTS WHERE FKID_SLIP=" + slipId + ";");
//            while (rs.next()) {
//                retVal.addPayment(SlipPayment.PaymentType.valueOf(rs.getString("FORM").trim()), rs.getDouble("AMOUNT"), rs.getString("NAME"));
//            }
//            rs.getStatement().close();
//            rs.close();
//            db.getConncetion().commit();
//        } catch (SQLException e) {
//            System.err.println("BĹ‚Ä…d odczytu paragonu: " + e.getMessage());
//            logger.log(Level.WARNING, "Nie udało siś odczytać paragonu do druku", e);
//        }
//        return retVal;
//    }
//
//    public void saveSlip(Slip slip) throws BartException {
//        String sql;
//        try {
//            sql = "INSERT INTO SLIPS (CASHBOXLOGO, EXTERNALREFERENCE, CASHIERNAME) VALUES ('"
//                    + slip.getCashbox() + "', '"
//                    + slip.getReference() + "', '"
//                    + slip.getCashierName() + "');";
//            db.sqlExec(sql);
//            int slipId;
//            ResultSet rs = db.getConncetion().createStatement().executeQuery("SELECT ID FROM SLIPS WHERE EXTERNALREFERENCE='" + slip.getReference() + "'");
//            rs.next();
//            slipId = rs.getInt("ID");
//            rs.getStatement().close();
//            rs.close();
//            for (int i = 0; i < slip.getNoOfLines(); i++) {
//                SaleLine sl = slip.getLine(i);
//                sql = "INSERT INTO SLIPLINES (FKID_SLIP, NAME, AMOUNT, PRICE, TAXRATE, DISCOUNT_TYPE, DISCOUNT) VALUES (" + slipId
//                        + ", '" + sl.getName() + "', " + sl.getAmount() + ", " + sl.getPrice() + ", '" + sl.getTaxRate().name() + "', " + sl.getDiscountType().ordinal() + ", " + sl.getDiscount() + ");";
//                db.sqlExec(sql);
//            }
//
//            double amount;
//            amount = slip.getPaymentAmount(SlipPayment.PaymentType.Cash);
//            if (amount != 0.0) {
//                db.sqlExec("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
//                        + "VALUES (" + slipId + ", " + amount + ", "
//                        + ((slip.getPaymentName(SlipPayment.PaymentType.Cash) == null) ? ("NULL") : ("'" + slip.getPaymentName(SlipPayment.PaymentType.Cash) + "'"))
//                        + ", '" + SlipPayment.PaymentType.Cash + "');");
//            }
//            amount = slip.getPaymentAmount(SlipPayment.PaymentType.CreditCard);
//            if (amount != 0.0) {
//                db.sqlExec("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
//                        + "VALUES (" + slipId + ", " + amount + ", '"
//                        + slip.getPaymentName(SlipPayment.PaymentType.CreditCard) + "', '" + SlipPayment.PaymentType.CreditCard + "');");
//            }
//            amount = slip.getPaymentAmount(SlipPayment.PaymentType.Cheque);
//            if (amount != 0.0) {
//                db.sqlExec("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
//                        + "VALUES (" + slipId + ", " + amount + ", '"
//                        + slip.getPaymentName(SlipPayment.PaymentType.Cheque) + "', '" + SlipPayment.PaymentType.Cheque + "');");
//            }
//            amount = slip.getPaymentAmount(SlipPayment.PaymentType.Voucher);
//            if (amount != 0.0) {
//                db.sqlExec("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
//                        + "VALUES (" + slipId + ", " + amount + ", '" + slip.getPaymentName(SlipPayment.PaymentType.Voucher) + "', '" + SlipPayment.PaymentType.Voucher + "');");
//            }
//            amount = slip.getPaymentAmount(SlipPayment.PaymentType.Other);
//            if (amount != 0.0) {
//                db.sqlExec("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
//                        + "VALUES (" + slipId + ", " + amount + ", '"
//                        + slip.getPaymentName(SlipPayment.PaymentType.Other) + "', '" + SlipPayment.PaymentType.Other + "');");
//            }
//            amount = slip.getPaymentAmount(SlipPayment.PaymentType.Credit);
//            if (amount != 0.0) {
//                db.sqlExec("INSERT INTO SLIPPAYMENTS (FKID_SLIP, AMOUNT, NAME, FORM) "
//                        + "VALUES (" + slipId + ", " + amount + ", '"
//                        + slip.getPaymentName(SlipPayment.PaymentType.Credit) + "', '" + SlipPayment.PaymentType.Credit + "');");
//            }
//            db.getConncetion().commit();
//        } catch (SQLException e) {
//            logger.log(Level.WARNING, "Nie mozna zapisać paragonu do bazy", e);
//            System.err.println("Problem z zapisem paragonu do bazy: " + e.getMessage());
//            throw new BartException("Serwer wyduku fiskalnego nie moze zapisać paragonu do bazy");
//        }
//    }
}
