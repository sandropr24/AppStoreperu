package com.example.appstoreperu;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    EditText edtIdMarca, edtDescripcion, edtPrecio, edtStock, edtGarantia;
    Button btnRegistrarProducto;
    RequestQueue requestQueue;

    private final String URL = "http://10.0.2.2:3000/productos";

    private void loadUI() {
        edtIdMarca = findViewById(R.id.edtIdMarca);
        edtDescripcion = findViewById(R.id.edtDescripcion);
        edtPrecio = findViewById(R.id.edtPrecio);
        edtStock = findViewById(R.id.edtStock);
        edtGarantia = findViewById(R.id.edtGarantia);
        btnRegistrarProducto = findViewById(R.id.btnRegistrarProducto);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        loadUI();

        btnRegistrarProducto.setOnClickListener(v -> validarRegistro());
    }

    private void limpiarCampos() {
        edtIdMarca.setText("");
        edtDescripcion.setText("");
        edtPrecio.setText("");
        edtStock.setText("");
        edtGarantia.setText("");
        edtIdMarca.requestFocus();
    }

    private void validarRegistro() {
        if (edtIdMarca.getText().toString().isEmpty()) {
            edtIdMarca.setError("Ingrese ID de Marca");
            edtIdMarca.requestFocus();
            return;
        }
        if (edtDescripcion.getText().toString().isEmpty()) {
            edtDescripcion.setError("Ingrese descripción");
            edtDescripcion.requestFocus();
            return;
        }
        if (edtPrecio.getText().toString().isEmpty()) {
            edtPrecio.setError("Ingrese precio");
            edtPrecio.requestFocus();
            return;
        }
        if (edtStock.getText().toString().isEmpty()) {
            edtStock.setError("Ingrese stock");
            edtStock.requestFocus();
            return;
        }
        if (edtGarantia.getText().toString().isEmpty()) {
            edtGarantia.setError("Ingrese garantía");
            edtGarantia.requestFocus();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Productos");
        builder.setMessage("¿Seguro de registrar?");
        builder.setPositiveButton("Sí", (a,b) -> registrarProducto());
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void registrarProducto() {
        requestQueue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idmarca", Integer.parseInt(edtIdMarca.getText().toString()));
            jsonObject.put("descripcion", edtDescripcion.getText().toString().trim());
            jsonObject.put("precio", Double.parseDouble(edtPrecio.getText().toString()));
            jsonObject.put("stock", Integer.parseInt(edtStock.getText().toString()));
            jsonObject.put("garantia", Integer.parseInt(edtGarantia.getText().toString()));
        } catch (JSONException e) {
            Log.e("Error", e.toString());
        }

        Log.d("ValoresWS", jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                response -> {
                    Toast.makeText(this, "Producto registrado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                },
                error -> {
                    if (error instanceof TimeoutError) {
                        Log.e("Error WS", "Timeout: servidor tardó demasiado");
                    } else if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        String body = new String(error.networkResponse.data);
                        Log.e("Error WS", "Código: " + statusCode + " - " + body);
                    } else {
                        Log.e("Error WS", "Sin respuesta de red");
                    }
                }
        );

        int socketTimeout = 10000;
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }
}