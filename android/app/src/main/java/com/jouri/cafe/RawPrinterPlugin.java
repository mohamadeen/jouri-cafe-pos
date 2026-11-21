package com.jouri.cafe;

import android.util.Base64;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/** Sends raw ESC/POS bytes to a network thermal printer (TCP 9100). */
@CapacitorPlugin(name = "RawPrinter")
public class RawPrinterPlugin extends Plugin {
    @PluginMethod
    public void print(final PluginCall call) {
        final String host = call.getString("host");
        final int port = call.getInt("port", 9100);
        final String data = call.getString("data");
        final int timeout = call.getInt("timeout", 5000);
        if (host == null || host.isEmpty() || data == null) { call.reject("missing host or data"); return; }
        new Thread(() -> {
            Socket s = new Socket();
            try {
                s.connect(new InetSocketAddress(host, port), timeout);
                s.setSoTimeout(timeout);
                OutputStream out = s.getOutputStream();
                out.write(Base64.decode(data, Base64.DEFAULT));
                out.flush();
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                JSObject r = new JSObject(); r.put("ok", true); call.resolve(r);
            } catch (Exception e) {
                call.reject("print failed: " + e.getMessage(), e);
            } finally {
                try { s.close(); } catch (Exception ignored) {}
            }
        }).start();
    }
}
