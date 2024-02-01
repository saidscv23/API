package com.example.webservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class registro extends AppCompatActivity {
    private TextView registroTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registroTextView = findViewById(R.id.registroTextView);

        // Llamando al método para realizar la solicitud al servicio web
        consultarRegistro();
    }

    private void consultarRegistro() {
        String url = "http://192.168.101.10/WS/webapi.php?op=tabla2";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    ResponseBody responseBody = response.body();
                    if (!response.isSuccessful()) {
                        throw new IOException("Respuesta inesperada: " + response);
                    }
                    final String resultado = responseBody.string();
                    registro.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            registroTextView.setText(resultado);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
