package com.example.peidosaguacelica106.Clases;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.peidosaguacelica106.R;

public class FacturaSRI extends AppCompatActivity {
    private final String urlWeb="https://facturadorsri.sri.gob.ec/portal-facturadorsri-internet/pages/inicio.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factura_sri);

        WebView webView=findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);


        webView.getSettings().setBuiltInZoomControls(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.loadUrl(urlWeb);
    }
}