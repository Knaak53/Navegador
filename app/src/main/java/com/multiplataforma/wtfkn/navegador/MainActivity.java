package com.multiplataforma.wtfkn.navegador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AutoCompleteTextView ed;
    WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = (AutoCompleteTextView) findViewById(R.id.editText);
        myWebView = (WebView) findViewById(R.id.webview);
        WebViewClient myWebClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                ed.setText(view.getUrl());
                super.onPageFinished(view, url);
            }
        };
        myWebView.setWebViewClient(myWebClient);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    buscar(v);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
                    handled = true;
                }
                return handled;
            }
        });
        final Context context = this;
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!(ed.getText().toString().equals(""))) {
                    AdminSqlLite admin = new AdminSqlLite(context, "historial", null, 1);
                    SQLiteDatabase db = admin.getReadableDatabase();
                    String selectString = "select distinct url from urls where url LIKE '%" + ed.getText() + "%'";
                    Cursor cursor = db.rawQuery(selectString, null);
                    String[] urls = new String[cursor.getCount()];
                    for (int i = 0; cursor.moveToNext(); i++) {
                        urls[i] = cursor.getString(0);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, urls);
                    db.close();
                    ed.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void registrarUrl(String url) {
        AdminSqlLite admin = new AdminSqlLite(this,"historial",null,1);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues registro = new ContentValues();
        registro.put("url",url);
        db.insert("urls",null,registro);
        db.close();
    }
//vdsvsdfew
    public void onBackPressed() {
        atras(myWebView);
    }

    public void atras(View view) {
        if(myWebView.canGoBack()){
            myWebView.goBack();
        }
    }

    public void buscar(View view){
        int conProtocolo = ed.getText().toString().indexOf("http://");
        if(conProtocolo>=0){
            myWebView.loadUrl(ed.getText().toString());
        }else{
            myWebView.loadUrl("http://"+ed.getText().toString());
        }
        registrarUrl(ed.getText().toString());
        ed.setText(myWebView.getUrl());
    }

    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
       /* // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.SE)) {
            myWebView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the eafault

        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);*/
//    }
}


