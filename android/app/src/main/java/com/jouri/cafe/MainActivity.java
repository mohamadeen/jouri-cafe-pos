package com.jouri.cafe;

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(RawPrinterPlugin.class);
        super.onCreate(savedInstanceState);
    }
}
