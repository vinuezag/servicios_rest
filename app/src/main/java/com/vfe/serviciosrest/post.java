package com.vfe.serviciosrest;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class post extends AppCompatActivity {

    private EditText txtCedula, txtNombre, txtCorreo;
    private Spinner cantBoletos, zona;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        txtCedula = findViewById(R.id.txtCedula);
        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        cantBoletos = findViewById(R.id.cantBoletos);
        zona = findViewById(R.id.zona);
        btnGuardar = findViewById(R.id.btnGuardar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.zona, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zona.setAdapter(adapter);

        ArrayAdapter<CharSequence> adaptar = ArrayAdapter.createFromResource(this,
                R.array.numBoletos, android.R.layout.simple_spinner_item);
        adaptar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cantBoletos.setAdapter(adaptar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarDatos();
            }
        });
    }

    private void enviarDatos() {
        // Obtener los datos de los EditTexts y Spinners
        String cedula = txtCedula.getText().toString();
        String nombre = txtNombre.getText().toString();
        String correo = txtCorreo.getText().toString();
        int boletos = Integer.parseInt(cantBoletos.getSelectedItem().toString());
        String zonaSeleccionada = zona.getSelectedItem().toString();

        // Crear el objeto JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cedula", cedula);
            jsonObject.put("nombre", nombre);
            jsonObject.put("correo", correo);
            jsonObject.put("boletos", boletos);
            jsonObject.put("zona", zonaSeleccionada);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear la solicitud POST
        String url = Config.getBaseUrl() + "/guardar-json";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Procesar la respuesta del servidor
                            String mensaje = response.getString("mensaje");
                            if (response.has("camposInvalidos")) {
                                // En caso de campos inválidos, mostrar los detalles
                                String camposInvalidos = response.getJSONArray("camposInvalidos").toString();
                                Toast.makeText(post.this, "Campos inválidos: " + camposInvalidos, Toast.LENGTH_LONG).show();
                            } else if (response.has("mensaje") && mensaje.equals("JSON guardado exitosamente")) {
                                // Si el JSON fue guardado exitosamente
                                Toast.makeText(post.this, mensaje, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(post.this, "Respuesta no esperada: " + response.toString(), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(post.this, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de error de red o servidor
                        String errorMessage = "Error al enviar datos: " + error.getMessage();
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                JSONObject errorResponse = new JSONObject(new String(error.networkResponse.data));
                                if (errorResponse.has("mensaje")) {
                                    errorMessage = errorResponse.getString("mensaje");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Toast.makeText(post.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
        );

        // Agregar la solicitud a la cola
        requestQueue.add(jsonObjectRequest);
    }
}
