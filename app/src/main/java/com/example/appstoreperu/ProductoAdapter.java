package com.example.appstoreperu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

public class ProductoAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> productos;

    public ProductoAdapter(Context context, List<JSONObject> productos){
        super(context, 0, productos);
        this.context = context;
        this.productos = productos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        }

        JSONObject producto = productos.get(position);

        TextView tvDescripcion = convertView.findViewById(R.id.tvDescripcion);
        TextView tvPrecio = convertView.findViewById(R.id.tvPrecio);
        TextView tvStock = convertView.findViewById(R.id.tvStock);
        TextView tvGarantia = convertView.findViewById(R.id.tvGarantia);

        try{
            tvDescripcion.setText(producto.getString("descripcion"));
            tvPrecio.setText("Precio: S/ " + producto.getDouble("precio"));
            tvStock.setText("Stock: " + producto.getInt("stock"));
            tvGarantia.setText("Garantía: " + producto.getInt("garantia") + " meses");
        }catch(Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}