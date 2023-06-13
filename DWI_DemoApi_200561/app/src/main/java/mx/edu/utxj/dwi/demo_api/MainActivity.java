package mx.edu.utxj.dwi.demo_api;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button btnGuardar;
    private Button btnBuscar;
    private Button btnActualizar;
    private Button btnEliminar;
    private EditText etCodigoBarras;
    private EditText etName;
    private EditText etEstudio;
    private EditText etGenero;
    private EditText etPrecio;
    private EditText etExistencias;

    private ListView lvVideojuegos;
    private RequestQueue colaPeticiones;
    private JsonArrayRequest jsonArrayRequest;
    private ArrayList<String> origenDatos = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private String url = "http://192.168.0.106:3300/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGuardar = findViewById(R.id.btnSave);
        btnActualizar = findViewById(R.id.btnUpdate);
        btnBuscar = findViewById(R.id.btnSearch);
        btnEliminar = findViewById(R.id.btnDelete);
        etCodigoBarras = findViewById(R.id.etCodigoBarras);
        etName = findViewById(R.id.etName);
        etEstudio = findViewById(R.id.etEstudio);
        etGenero = findViewById(R.id.etGenero);
        etPrecio = findViewById(R.id.etPrecio);
        etExistencias = findViewById(R.id.etExistencias);
        lvVideojuegos = findViewById(R.id.lvVideojuegos);
        colaPeticiones = Volley.newRequestQueue(this);
        listVideojuegos();



        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JsonObjectRequest peticion = new JsonObjectRequest(
                        Request.Method.GET,
                        url + etCodigoBarras.getText().toString(),
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response.has("status"))
                                    Toast.makeText(MainActivity.this, "videojuego no encontrado", Toast.LENGTH_SHORT).show();
                                else {
                                    try {
                                        etName.setText(response.getString("name"));
                                        etEstudio.setText(response.getString("estudio"));
                                        etGenero.setText(response.getString("genero"));
                                        etPrecio.setText(String.valueOf(response.getString("precio")));
                                        etExistencias.setText(String.valueOf(response.getInt("existencias")));
                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Videojuego no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                colaPeticiones.add(peticion);
            }
        });





        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject videojuego = new JSONObject();
                try {
                    videojuego.put("codigobarras",etCodigoBarras.getText().toString());
                    videojuego.put("name",etName.getText().toString());
                    videojuego.put("estudio",etEstudio.getText().toString());
                    videojuego.put("genero",etGenero.getText().toString());
                    videojuego.put("precio",Float.parseFloat(etPrecio.getText().toString()));
                    videojuego.put("existencias",Float.parseFloat(etExistencias.getText().toString()));
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url +"insert/",
                        videojuego,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.getString("status").equals("Videojuego insertado")) {
                                        Toast.makeText(MainActivity.this, "Videojuego insertado correctamente!", Toast.LENGTH_SHORT).show();
                                        etCodigoBarras.setText("");
                                        etName.setText("");
                                        etEstudio.setText("");
                                        etGenero.setText("");
                                        etPrecio.setText("");
                                        etExistencias.setText("");
                                        adapter.clear();
                                        lvVideojuegos.setAdapter(adapter);
                                        listVideojuegos();
                                    }
                                }catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                colaPeticiones.add(jsonObjectRequest);
            }
        });


        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCodigoBarras.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Primero busque", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject videojuegos = new JSONObject();
                    try {
                        videojuegos.put("codigobarras", etCodigoBarras.getText().toString());

                        if (!etName.getText().toString().isEmpty()) {
                            videojuegos.put("name", etName.getText().toString());
                        }

                        if (!etEstudio.getText().toString().isEmpty()) {
                            videojuegos.put("estudio", etEstudio.getText().toString());
                        }

                        if (!etGenero.getText().toString().isEmpty()) {
                            videojuegos.put("genero", etGenero.getText().toString());
                        }

                        if (!etPrecio.getText().toString().isEmpty()) {
                            videojuegos.put("precio", Float.parseFloat(etPrecio.getText().toString()));
                        }

                        if (!etExistencias.getText().toString().isEmpty()) {
                            videojuegos.put("existencias", Float.parseFloat(etExistencias.getText().toString()));
                        }

                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    JsonObjectRequest actualizar = new JsonObjectRequest(
                            Request.Method.PUT,
                            url + "actualizar/" + etCodigoBarras.getText().toString(),
                            videojuegos,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Videojuego actualizado")) {
                                            Toast.makeText(MainActivity.this, "Videojuego actualizado", Toast.LENGTH_SHORT).show();

                                        } else if (response.getString("status").equals("No encontrado")) {
                                            Toast.makeText(MainActivity.this, "No encontrado", Toast.LENGTH_SHORT).show();
                                        }


                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                 }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                    );
                    colaPeticiones.add(actualizar);

                    listVideojuegos();
                    adapter.notifyDataSetChanged();


                    etCodigoBarras.setText("");
                    etName.setText("");
                    etEstudio.setText("");
                    etGenero.setText("");
                    etPrecio.setText("");
                    etExistencias.setText("");
                }
            }
        });








        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCodigoBarras.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Ingrese el nombre del videojuego", Toast.LENGTH_SHORT).show();
                } else {
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.DELETE,
                            url + "borrar/" + etCodigoBarras.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("status").equals("Videojuego eliminado")) {
                                            Toast.makeText(MainActivity.this, "Videojuego eliminado", Toast.LENGTH_SHORT).show();
                                        } else if (response.getString("status").equals("No encontrado")) {
                                            Toast.makeText(MainActivity.this, "No encontrado", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    colaPeticiones.add(jsonObjectRequest);

                    listVideojuegos();
                    adapter.notifyDataSetChanged();


                    etCodigoBarras.setText("");
                    etName.setText("");
                    etEstudio.setText("");
                    etGenero.setText("");
                    etPrecio.setText("");
                    etExistencias.setText("");
                }
            }
        });


    }










    protected void listVideojuegos(){

        jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        origenDatos.clear();
                        //Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                        for(int i = 0;i < response.length(); i++){
                            try {
                                String codigobarras =response.getJSONObject(i).getString("codigobarras");
                                String name = response.getJSONObject(i).getString("name") ;
                                String estudio = response.getJSONObject(i).getString("estudio");
                                origenDatos.add(codigobarras +"::" +name+"::"+estudio);
                            } catch (JSONException e) {
                            }
                        }
                        adapter = new ArrayAdapter<>(MainActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, origenDatos);
                        lvVideojuegos.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
        );
        colaPeticiones.add(jsonArrayRequest);
    }
}