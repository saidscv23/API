package com.example.webservice;

import androidx.appcompat.app.AppCompatActivity;



import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class registro extends AppCompatActivity {



        private EditText txtCedula;
        private TextView ingresosTextView;
        private Button btnConsultar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registro);

            txtCedula = findViewById(R.id.txtingreso);
            ingresosTextView = findViewById(R.id.ingresos);
            btnConsultar = findViewById(R.id.button);

            btnConsultar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cedula = txtCedula.getText().toString().trim();
                    consultarRegistro(cedula);
                }
            });
        }

        private void consultarRegistro(String cedula) {
            String url = "http://10.10.18.90/WS/webapi3.php";
            OkHttpClient client = new OkHttpClient();


            RequestBody requestBody = new FormBody.Builder()
                    .add("op", "tabla")
                    .add("ced", cedula)
                    .build();


            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(okhttp3.Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(okhttp3.Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }

                    String responseData = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                StringBuilder formattedResult = new StringBuilder();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String nombreCompletoPaciente = jsonObject.getString("Nombre_Completo_Paciente");
                                    String cedulaPaciente = jsonObject.getString("Cedula_Paciente");
                                    String nombreCompletoMedico = jsonObject.getString("Nombre_Completo_Medico");
                                    String fechaCita = jsonObject.getString("Fecha_Cita");
                                    String horaCita = jsonObject.getString("Hora_Cita");
                                    String fechaRegistro = jsonObject.getString("Fecha_Registro");

                                    String registroFormateado = "Nombre del Paciente: " + nombreCompletoPaciente + "\n" +
                                            "Cédula del Paciente: " + cedulaPaciente + "\n" +
                                            "Nombre del Médico: " + nombreCompletoMedico + "\n" +
                                            "Fecha de la Cita: " + fechaCita + "\n" +
                                            "Hora de la Cita: " + horaCita + "\n" +
                                            "Fecha de Registro: " + fechaRegistro + "\n\n";
                                    formattedResult.append(registroFormateado);
                                }
                                ingresosTextView.setText(formattedResult.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }