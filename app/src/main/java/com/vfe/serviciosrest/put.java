package com.vfe.serviciosrest;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class put extends AppCompatActivity {

    private EditText txtCedula;
    private TextView txtCorreoActual, txtNuevoCorreo;
    private Button btnBuscar, btnActualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);

        txtCedula = findViewById(R.id.txtCedula);
        txtCorreoActual = findViewById(R.id.dos);
        txtNuevoCorreo = findViewById(R.id.txtCorreo);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarRegistro();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarCorreo();
            }
        });

    }

    private void buscarRegistro() {
        String cedula = txtCedula.getText().toString().trim();
        String url = Config.getBaseUrl()+"/buscar-registro/" + cedula;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String correoActual = response.getString("correo");
                            txtCorreoActual.setText(correoActual);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(put.this, "Error al parsear la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(put.this, "Registro no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void actualizarCorreo() {
        String cedula = txtCedula.getText().toString().trim();
        String nuevoCorreo = txtNuevoCorreo.getText().toString().trim();
        String url = Config.getBaseUrl() + "/actualizar-correo/" + cedula;

        if (nuevoCorreo.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el nuevo correo", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("correo", nuevoCorreo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String mensaje = response.getString("mensaje");
                            Toast.makeText(put.this, mensaje, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(put.this, "Error al parsear la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Leer el error detallado desde la respuesta JSON del servidor
                        String errorMessage = "Error desconocido";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                // Convertir la respuesta de error a un objeto JSON
                                String jsonError = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(jsonError);
                                errorMessage = errorJson.optString("mensaje", "Error al actualizar el correo");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(put.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

}