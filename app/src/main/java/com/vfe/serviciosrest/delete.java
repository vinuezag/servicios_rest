package com.vfe.serviciosrest;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class delete extends AppCompatActivity {

    private EditText txtCedula;
    private Button btnEliminar;
    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        txtCedula = findViewById(R.id.txtCedula);
        btnEliminar = findViewById(R.id.btnEliminar);
        txtResultado = findViewById(R.id.txtResultado);

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarRegistro();
            }
        });
    }

    private void eliminarRegistro() {
        // Obtener la cédula del EditText
        String cedula = txtCedula.getText().toString();

        if (cedula.isEmpty()) {
            Toast.makeText(this, "Ingrese una cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL del servicio REST
        String url = Config.getBaseUrl() + "/eliminar-json/" + cedula;

        // Crear la solicitud DELETE
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Procesar la respuesta del servidor
                            String mensaje = response.getString("mensaje");
                            JSONObject entregado = response.getJSONObject("entregado");

                            // Formatear el mensaje para mostrarlo de forma limpia
                            String resultado = "Mensaje: " + mensaje + "\n\n";

                            // Extraer datos de 'entregado'
                            resultado += "Registro Entregado: \n";
                            resultado += "Cédula: " + entregado.getString("cedula") + "\n";
                            resultado += "Nombre: " + entregado.getString("nombre") + "\n";
                            resultado += "Correo: " + entregado.getString("correo") + "\n";
                            resultado += "Boletos: " + entregado.getInt("boletos") + "\n";
                            resultado += "Zona: " + entregado.getString("zona") + "\n\n";

                            // Procesar la lista de pendientes
                            JSONArray listaPendiente = response.getJSONArray("listaPendiente");
                            resultado += "Registros Pendientes: \n";
                            for (int i = 0; i < listaPendiente.length(); i++) {
                                JSONObject item = listaPendiente.getJSONObject(i);
                                resultado += "Cédula: " + item.getString("cedula") + ", ";
                                resultado += "Nombre: " + item.getString("nombre") + "\n";
                            }

                            // Procesar la lista de entregados
                            JSONArray listaEntregados = response.getJSONArray("listaEntregados");
                            resultado += "\nRegistros Entregados: \n";
                            for (int i = 0; i < listaEntregados.length(); i++) {
                                JSONObject item = listaEntregados.getJSONObject(i);
                                resultado += "Cédula: " + item.getString("cedula") + ", ";
                                resultado += "Nombre: " + item.getString("nombre") + "\n";
                            }

                            // Mostrar el resultado en la interfaz
                            txtResultado.setText(resultado);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            txtResultado.setText("Error al procesar la respuesta.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(delete.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        requestQueue.add(jsonObjectRequest);
    }
}
