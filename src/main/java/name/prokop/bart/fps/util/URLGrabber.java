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
 * URLGrabber.java
 *
 * Created on 26 grudzień 2004, 14:43
 */
package name.prokop.bart.fps.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author bart
 */
public class URLGrabber implements Runnable {

    /**
     * retrieved data
     */
    private byte[] data;
    private boolean completed = false;
    private boolean errored = false;
    private int progress = 0;
    private final URL url;
    private Throwable error = null;
    private final File destFile;

    public URLGrabber(String url) throws MalformedURLException {
        this(new URL(url));
    }

    public URLGrabber(URL url) {
        this.url = url;
        this.destFile = null;
    }

    public URLGrabber(URL url, File file) {
        this.url = url;
        this.destFile = file;
    }

    public void downlowad() {
        new Thread(this).start();
    }

    public void run() {
        try {
            InputStream is = url.openStream();
            OutputStream os;
            if (destFile == null) {
                os = new ByteArrayOutputStream();
            } else {
                os = new FileOutputStream(destFile);
            }
            while (true) {
                byte[] t = new byte[1024];
                int c = is.read(t);
                if (c == -1) {
                    break;
                }
                os.write(t, 0, c);
                progress += c;
            }
            if (destFile == null) {
                data = ((ByteArrayOutputStream) os).toByteArray();
            } else {
                os.close();
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
            errored = true;
            error = ioex;
        } finally {
            completed = true;
        }
    }

    public byte[] getData() {
        return data;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getProgress() {
        return progress;
    }

    public boolean isErrored() {
        return errored;
    }

    public Throwable getError() {
        return error;
    }

    public static InputStream getDocumentAsInputStream(URL url) throws IOException {
        InputStream in = url.openStream();
        return in;
    }

    public static InputStream getDocumentAsInputStream(String url) throws MalformedURLException, IOException {
        URL u = new URL(url);
        return getDocumentAsInputStream(u);
    }

    public static String getDocumentAsString(URL url) throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream in = url.openStream();
        int c;
        while ((c = in.read()) != -1) {
            result.append((char) c);
        }
        return result.toString();
    }

    public static String getDocumentAsString(String url) throws MalformedURLException, IOException {
        URL u = new URL(url);
        return getDocumentAsString(u);
    }

    public static byte[] downloadFile(URL url) throws IOException {
        InputStream is = url.openStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            byte[] t = new byte[2000];
            int c = is.read(t);
            if (c == -1) {
                break;
            }
            baos.write(t, 0, c);
        }
        return baos.toByteArray();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        URLGrabber grabber = new URLGrabber("http://ftp.ttsoft.pl/CNK/dist.zip");
        grabber.downlowad();
    }
}
