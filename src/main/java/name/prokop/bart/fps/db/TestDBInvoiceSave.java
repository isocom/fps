/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * TestDBSLipSave.java
 *
 * Created on December 29, 2007, 9:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package name.prokop.bart.fps.db;

import name.prokop.bart.fps.datamodel.Invoice;
import name.prokop.bart.fps.datamodel.Slip;
import name.prokop.bart.runtime.RuntimeProperties;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Bart
 */
public class TestDBInvoiceSave {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(RuntimeProperties.INSTANCE.getSpringContext());
        InvoiceDAO slipDAO = ac.getBean(InvoiceDAO.class);        
        System.out.println("zapisuje");
        slipDAO.saveInvoice(Invoice.getTestInvoice());
        System.out.println("zapisalem");
        ac.destroy();
    }
}
