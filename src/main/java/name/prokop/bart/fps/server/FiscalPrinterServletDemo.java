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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import name.prokop.bart.fps.datamodel.Slip;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;

/**
 *
 * @author prokob01
 */
public class FiscalPrinterServletDemo {

    public static void main(String... args) throws Exception {
        HttpClient client = new HttpClient();
        client.start();

        HttpExchange httpExchange = new MyExchange();
        httpExchange.setURL("http://localhost:8080/fps/printSlip");
        httpExchange.setRequestHeader("fiscalPrinterType", "DFEmul");
        httpExchange.setRequestHeader("comPort", "COM3");

        ByteArrayInputStream bais = new ByteArrayInputStream(Slip.getTestSlip().toJSONObject().toString().getBytes());
        httpExchange.setRequestContentSource(bais);
        httpExchange.setMethod("POST");
        httpExchange.setRequestContentType("application/json");

        client.send(httpExchange);

        // we expact HTTP status 200 here
        System.out.println(httpExchange.waitForDone());
    }

    private static class MyExchange extends ContentExchange {

        @Override
        protected void onResponseComplete() throws IOException {
            // Show the response from FPS HTTP Server
            System.out.println(getResponseContent());
        }
    }
}
