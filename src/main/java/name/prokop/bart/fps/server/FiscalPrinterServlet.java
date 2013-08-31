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

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.prokop.bart.fps.FiscalPrinter;
import name.prokop.bart.fps.FiscalPrinter.Type;
import name.prokop.bart.fps.datamodel.Slip;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author prokob01
 */
public class FiscalPrinterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fiscalPrinterType = req.getHeader("fiscalPrinterType");
        String comPort = req.getHeader("comPort");
        String slipJsonString = IOUtils.toString(req.getReader());

        JSONObject slipJson;
        try {
            slipJson = new JSONObject(slipJsonString);
        } catch (JSONException je) {
            throw new IOException(je);
        }
        Slip slip = new Slip(slipJson);

        Type type = FiscalPrinter.Type.valueOf(fiscalPrinterType);
        FiscalPrinter fiscalPrinter = type.getFiscalPrinter(comPort);
        fiscalPrinter.print(slip);

        String result;
        try {
            result = slip.toJSONObject().toString(3);
        } catch (JSONException je) {
            throw new IOException(je);
        }

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println(result);
        writer.close();
    }
}
