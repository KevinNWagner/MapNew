package com.example.kevin.mapnew;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.example.kevin.mapnew.MainActivity.resources;

/**
 * Created by kevin on 1/10/2016.
 */
public class obtenerRecorrido_Task extends AsyncTask<String,Integer,ArrayList<LatLng>> {
    // private LatLng recorrido[];
    final public ArrayList<LatLng> puntos = new ArrayList<LatLng>();
    String ip = resources.getString(R.string.ipDB);
    String port = resources.getString(R.string.portDB);
    String nameDB = resources.getString(R.string.nameDB);
    String pass = resources.getString(R.string.passDB);
    String user = resources.getString(R.string.userDB);

    String urlConection = "jdbc:mysql://" + ip + ":" + port + "/" + nameDB + "?connectTimeout=3000";

    @Override
    protected ArrayList<LatLng> doInBackground(String... params) {
        try {
            Log.d("Inicio de conexcion ", "latitud");

            // tt.show();
            Class.forName("com.mysql.jdbc.Driver");
            // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
            // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
            Log.d("check class ", "latitud");


            final Connection conn = DriverManager.getConnection(urlConection, user, pass);
            //En el stsql se puede agregar cualquier consulta SQL deseada.
            Log.d("Conectado ", "latitud");
            String stsql = "Select * from recorridos";
            final Statement st = conn.createStatement();
            final ResultSet rs = st.executeQuery(stsql);
            Integer index = 0;
            rs.next();

            //System.out.println( rs.getString(1) );
            while (rs.next()) {


                puntos.add(new LatLng(Double.parseDouble(rs.getString(4)), Double.parseDouble(rs.getString(3))));
                //Log.d(String.valueOf(recorrido[index]),"pos");
                Log.d(rs.getString("posicionLatitud"), "latitud");
                //puntos.add(rs.getString("punto_latitud"));
                Log.d(rs.getString("posicionLongitud"), "longitud");
                //puntos.add(rs.getString("punto_longitud"));

                index++;
                Log.d("numero:" + index, "longitud");


                //longitud = Double.parseDouble(rs.getString("longitud_colectivo"));

                //latitud = Double.parseDouble(rs.getString("latitud_colectivo"));
            }
            Log.d("End coneccion ", "latitud");
            conn.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            publishProgress((int) 3);
            isCancelled();


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            publishProgress((int) 3);
            isCancelled();
        }
        //colectivo= new LatLng(latitud,longitud);
        //return recorrido;

        return puntos;
    }


    public void setRecorrido(GoogleMap mMap, Context context) {


        PolygonOptions trayecto = new PolygonOptions();
        Integer a = 0;
        for (a = 0; a < puntos.size(); a++) {

            trayecto.add(puntos.get(a));

        }

        trayecto.strokeColor(Color.BLUE);
        trayecto.strokeWidth(8);

        if (!puntos.isEmpty()) {
            try {
                mMap.addPolygon(trayecto);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntos.get(1), 14));
            } catch (NumberFormatException ignored) {

            }
        } else {
            noHayConexcion(context,mMap);
        }


    }


    private void noHayConexcion(final Context context, final GoogleMap mMap) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ocurrio un problema");
        builder.setMessage("No hay coneccion ..");

        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {





                try {
                    if(ConexcionOn()){

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int ConexcionOn() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
        // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2
        Log.d("check class ", "latitud");


        final Connection conn = DriverManager.getConnection(urlConection, user, pass);

        String stsql = "Select version()";
        final Statement st = conn.createStatement();
        final ResultSet rs = st.executeQuery(stsql);

        if(rs.wasNull()){
            return 0;
        }


        return 1;
    }

}

