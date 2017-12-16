package com.example.kevin.mapnew;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by kevin on 16/11/2016.
 */
public class obtenerParadas_Task extends AsyncTask<String,Integer,ArrayList<LatLng>> {
    // private LatLng recorrido[];
    private ArrayList<LatLng> puntos = new ArrayList<LatLng>();


    @Override
    protected ArrayList<LatLng> doInBackground(String... params) {
        try {


            // tt.show();
            Class.forName("com.mysql.jdbc.Driver");
            // "jdbc:postgresql://IP:PUERTO/DB", "USER", "PASSWORD");
            // Si estás utilizando el emulador de android y tenes el PostgreSQL en tu misma PC no utilizar 127.0.0.1 o localhost como IP, utilizar 10.0.2.2


            final Connection conn = DriverManager.getConnection("jdbc:mysql://10.0.2.2/db_administracion_colectivos", "root", "");
            //En el stsql se puede agregar cualquier consulta SQL deseada.
            Log.d("Conectado ","latitud");
            String stsql = "Select * from recorridos";
            final Statement st = conn.createStatement();
            final ResultSet rs = st.executeQuery(stsql);
            Integer index = 0;
            rs.next();
            while(rs.next()) {

                puntos.add(new LatLng(Double.parseDouble(rs.getString(4)), Double.parseDouble(rs.getString(3))));
                //Log.d(String.valueOf(recorrido[index]),"pos");
                Log.d(rs.getString("posicionLatitud"),"latitud");
                //puntos.add(rs.getString("punto_latitud"));
                Log.d(rs.getString("posicionLongitud"),"longitud");
                //puntos.add(rs.getString("punto_longitud"));

                index++;
                Log.d("numero:"+index,"longitud");


            }
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
        return  puntos;

    }
}
