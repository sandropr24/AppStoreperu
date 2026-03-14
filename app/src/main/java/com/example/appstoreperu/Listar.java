package com.example.appstoreperu;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Listar extends AppCompatActivity {

    ListView lstProductos;
    RequestQueue requestQueue;

   private final String URL = "http://10.0.2.2:3000/productos";

    private void loadUI() {
        lstProductos = findViewById(R.id.lstProductos);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        loadUI();
        obtenerDatos();
    }

    private void obtenerDatos() {
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        renderizarListView(jsonArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("ErrorWS", volleyError.toString());
                        Toast.makeText(getApplicationContext(), "No se obtuvieron los datos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void renderizarListView(JSONArray jsonProductos) {
        try {
            List<JSONObject> listaProductos = new ArrayList<>();
            for (int i = 0; i < jsonProductos.length(); i++) {
                listaProductos.add(jsonProductos.getJSONObject(i));
            }

            ProductoAdapter adapter = new ProductoAdapter(this, listaProductos);
            lstProductos.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("ErrorJSON", e.toString());
        }
    }
}