package com.example.kevin.mapnew;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class initialMaps extends AppCompatActivity implements OnMapReadyCallback  {
    public static Resources resources;

    private MapView viewMap;
    private GoogleMap mMap;
    static LatLng ubicacionColectivo = null;
    static LatLng recorridoPuntos[] = null;
    static Marker colemarker;
    TextView texto;
    boolean selectParada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resources = getResources();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        FloatingActionButton paradasBtn = (FloatingActionButton) findViewById(R.id.fab);
        paradasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obtenerParadas_Task paradas = new obtenerParadas_Task();

                ArrayList<LatLng> paradasP ;
                try {
                    paradasP = paradas.execute().get();

                    Integer a = 0;
                    for (a = 0; a < paradasP.size(); a++){

                        final MarkerOptions paradap = new MarkerOptions();

                        paradap.position(paradasP.get(a)).title("Parada: "+(a+1));
                        mMap.addMarker(paradap);


                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                selectParada =true;

            }
        });

        FloatingActionButton direccion = (FloatingActionButton) findViewById(R.id.fab2);
        direccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        //MapView map = (MapView) findViewById(R.id.mapView);
        // map.getMapAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        final MarkerOptions colectivo = new MarkerOptions();

        colectivo.position(new LatLng(-31.623359, -58.504376)).title("Colectivo").icon(BitmapDescriptorFactory.fromResource(R.drawable.unt)).anchor(0.5f, 0.5f);
        colemarker = mMap.addMarker(colectivo);

        final MarkerOptions parada = new MarkerOptions();

        parada.position(new LatLng(-31.62617741632967, -58.50274975768242));

        colemarker = mMap.addMarker(new MarkerOptions()
                .title("Parada 3")
                .position(new LatLng(-31.62617741632967, -58.50274975768242))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("633mts del Colectivo más cercano")
        );
        Log.d("Marcelino", "latitud");


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(selectParada) {
                    dialogSeleccionarParada(marker);
                    selectParada = false;
                }
                return false;
            }
        });

   //     mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {


/*
            @Override
            public void onMapClick(LatLng latLng) {
                obtenerDataColectivo_Task obtener = new obtenerDataColectivo_Task(texto);
                try {
                    ubicacionColectivo = obtener.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

//                Log.d(ubicacionColectivo.toString(),"ubicaion");
               if(ubicacionColectivo != null) {
                   //mMap.clear();
                   //LatLng parada1 = new LatLng(-31.623359, -58.504376);
                   Toast.makeText(getApplicationContext(),ubicacionColectivo.toString(),Toast.LENGTH_SHORT).show();
                   colemarker.setPosition(ubicacionColectivo);



               }
            }

        });
*/

        verRecorrido();


    }

        private void dialogSeleccionarParada(final Marker marker) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(marker.getTitle());
            builder.setMessage("Desea seleccionar esta parada ?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mMap.clear();
                    SetParada(marker);
                    dialog.dismiss();
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

        public void SetParada(final Marker marker){
            final MarkerOptions parada = new MarkerOptions();
            parada.title(marker.getTitle());
            parada.position(marker.getPosition());

            //parada.equals(marker);
            mMap.addMarker(parada);
            verRecorrido();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parada.getPosition(), 16));

        }

        public void verRecorrido(){
            obtenerRecorrido_Task recorrido = new obtenerRecorrido_Task();
            try {
                recorrido.execute().get();
                recorrido.setRecorrido(mMap,this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }



}
