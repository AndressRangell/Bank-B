package com.bcp.geolocalizacion;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.newpos.pay.R;
import com.newpos.libpay.Logger;

public class Geolocalizacion extends AppCompatActivity {

    LocationManager locationManager;
    double longitudeNetwork;
    double latitudeNetwork;
    TextView longitudeValueNetwork;
    TextView latitudeValueNetwork;
    TextView distancia;

    private static final String DIR_LATITUD = "7.119456";
    private static final String DIR_LONGITUD = "-73.124520";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocalizacion);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        longitudeValueNetwork = findViewById(R.id.longitudeValueNetwork);
        latitudeValueNetwork = findViewById(R.id.latitudeValueNetwork);
        distancia = findViewById(R.id.distancia);
        if (ContextCompat.checkSelfPermission(Geolocalizacion.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Geolocalizacion.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Geolocalizacion.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
    }
    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.enableLocali))
                .setMessage(getResources().getString(R.string.msgUbicaDesact)+
                        "usa esta app")
                .setPositiveButton(getResources().getString(R.string.configUbica), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton( getResources().getString(R.string.cancel), (paramDialogInterface, paramInt) -> {
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void toggleNetworkUpdates(View view) {
        if (!checkLocation())
            return;
        Button button = (Button) view;
        if (button.getText().equals(getResources().getString(R.string.pause))) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && androidx.core.content.ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //nothing
            }
            locationManager.removeUpdates(locationListenerNetwork);
            button.setText(R.string.resume);
        }
        else {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 20 * 1000, 10, locationListenerNetwork);
            Toast.makeText(this, "\n" + getResources().getString(R.string.congigRedConf), Toast.LENGTH_LONG).show();
            button.setText(R.string.pause);
        }
    }

    private final LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitudeNetwork = location.getLongitude();
            latitudeNetwork = location.getLatitude();

            runOnUiThread(() -> {
                longitudeValueNetwork.setText(longitudeNetwork + "");
                latitudeValueNetwork.setText(latitudeNetwork + "");
                Toast.makeText(Geolocalizacion.this, "\n" +  getResources().getString(R.string.actProveeRed), Toast.LENGTH_SHORT).show();
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //nothing
        }

        @Override
        public void onProviderEnabled(String s) {
            //nothing
        }
        @Override
        public void onProviderDisabled(String s) {
            //nothing
        }
    };

    public void calcularDistancia(View view) {
        Location location = new Location(getResources().getString(R.string.localizacion1));
        location.setLatitude(Double.parseDouble(latitudeValueNetwork.getText().toString()));  //latitud
        location.setLongitude(Double.parseDouble(longitudeValueNetwork.getText().toString())); //longitud
        Location location2 = new Location(getResources().getString(R.string.localizacion2));
        location2.setLatitude(Double.parseDouble(DIR_LATITUD));  //latitud
        location2.setLongitude(Double.parseDouble(DIR_LONGITUD)); //longitud

        float distance = location.distanceTo(location2);
        float distanciaKm = distance / 1000;

        distancia.setText(String.valueOf(distanciaKm) + getResources().getString(R.string.km));

        float distanciaM = distanciaKm * 1000;
        if (distanciaM > 50) {
            Toast.makeText(this, getResources().getString(R.string.msjDistancia), Toast.LENGTH_LONG).show();
        }
        Logger.debug("" + view);
    }
}
