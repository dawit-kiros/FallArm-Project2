package com.example.fallarmproject;

import static java.lang.Math.pow;

import androidx.appcompat.app.AppCompatActivity;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import android.widget.Button;
import android.widget.EditText;

import java.io.PrintWriter;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private int sendCount = 1;
    // TextViews for displaying Accelerometer data
    TextView xCoor; // X axis object
    TextView yCoor; // Y axis object
    TextView zCoor; // Z axis object

    // TextViews for displaying Gyroscope data
    TextView xCoor2; // X axis object
    TextView yCoor2; // Y axis object
    TextView zCoor2; // Z axis object

    TextView txtPatientID ; //Patient ID

    TextView txtFallDownStatus;

    private Button buttonSend;
    private Button sendSMSBtn;

    private static final String SERVER_IP = "192.168.189.1";
    private static final int SERVER_PORT = 12345;

    float x = 0f;
    float y = 0f;
    float z = 0f;

    float x2 = 0f;
    float y2 = 0f;
    float z2 = 0f;

    private double latitude = 0 ;
    private double longitude = 0;

    String fallDownStatus = "No";

    private Socket client;
    private PrintWriter printwriter;
    private EditText textField;
    private Button button;
    private String message;


    Random rand = new Random();
    int patientId = rand.nextInt(1000000);

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        // Create objects for displaying Accelerometer data
        xCoor = findViewById(R.id.xcoor); // X axis object
        yCoor = findViewById(R.id.ycoor); // Y axis object
        zCoor = findViewById(R.id.zcoor); // Z axis object

        // Create objects for displaying Gyroscope data
        xCoor2 = findViewById(R.id.xcoor2); // X axis object
        yCoor2 = findViewById(R.id.ycoor2); // Y axis object
        zCoor2 = findViewById(R.id.zcoor2); // Z axis object

        txtPatientID = findViewById(R.id.patientID);
        txtFallDownStatus = findViewById(R.id.falldown);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Register to listen to Accelerometer data
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        // Register to listen to Gyroscope data
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);

        displayPatientDetails();

        sendSMSBtn = (Button) findViewById(R.id.btnSendSMS);


        sendSMSBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });

        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "New Data from Fall Arm!";
                new SendDataToServerTask().execute(message);
            }
        });

        displayLocation();
    }

    private class SendDataToServerTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket("192.168.189.1", 12345);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                String msg = "New Data from Fall Arm!\n" +
                        "Patient Id: " + patientId + "\n" +
                        "===== Accelerometer Data === " + "\n" +
                        "X: " + x + "\n" +
                        "Y: " + y + "\n" +
                        "Z: " + z + "\n" +
                        "===== Orientation Data === " + "\n" +
                        "X: " + x2 + "\n" +
                        "Y: " + y2 + "\n" +
                        "Z: " + z2 + "\n" +
                        "===== Location Data === " + "\n" +
                        "Latitude: " + latitude + "\n" +
                        "Longitude: " + longitude + "\n" +
                        "===== Fall down Status === " +
                        fallDownStatus;
                dataOutputStream.writeUTF(msg);

                return true;
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            finally{
                if (socket != null){
                    try {
                        socket.close();
                        return true;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return false;
                    }
                }

                if (dataOutputStream != null){
                    try {
                        dataOutputStream.close();
                        return true;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return false;
                    }
                }

                if (dataInputStream != null){
                    try {
                        dataInputStream.close();
                        return true;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(MainActivity.this, "Data sent to server!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to send data to server.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void displayLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            try {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED) {

                } else {
                    ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION }, 1);
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            } catch (SecurityException e) {
                Log.e("FallArm", "Location permission not granted."); }
        }
    }
    protected void sendSMSMessage() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);
        sendCount++;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String latitudeString = Double.toString(Math.round(latitude * 100) / 100);
                    String longitudeString = Double.toString(Math.round(longitude * 100) / 100);
                    SmsManager smsManager = SmsManager.getDefault();
                    String smsMessage = "Hello Nurse, \nA patient with the following details has fall down :\npatient ID: " + patientId + "\nLat: " +latitudeString +   "\nLong: " +longitudeString+ "\nPlease visit the patient as soon as possible!";
                    smsManager.sendTextMessage("+19256639114", null, smsMessage, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent successfully.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
    public void DetermineFallDown(){
        double vectorSum = Math.round(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));

        int riskClass;

        int fallDownDetected = 0;
        if (vectorSum >= 9 && vectorSum <= 10) {
            riskClass = 1;
        } else if (vectorSum >= 7 && vectorSum <= 8) {
            riskClass = 2;
        } else if (vectorSum >= 4 && vectorSum <= 6) {
            riskClass = 3;
        } else if (vectorSum >= 1 && vectorSum <= 3) {
            riskClass = 4;
        } else {
            riskClass = 5;
        }

        if (riskClass < 3){
            fallDownStatus = "Yes";

        }

        if (fallDownStatus == "Yes") {
            txtFallDownStatus.setText(fallDownStatus);
            if (sendCount == 1 )
                sendSMSMessage();
        }
    }



    public void displayPatientDetails()
    {
        txtPatientID.setText("Patient ID: " + patientId);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Check Accelerometer sensor type
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Assign directions
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            xCoor.setText("X: " + x);
            yCoor.setText("Y: " + y);
            zCoor.setText("Z: " + z);
        }

        // Check Gyroscope sensor type
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            x2 = event.values[0];
            y2 = event.values[1];
            z2 = event.values[2];
            xCoor2.setText("X: " + x2);
            yCoor2.setText("Y: " + y2);
            zCoor2.setText("Z: " + z2);
        }

        DetermineFallDown();
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            updateLocationInfo(location);

        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private void updateLocationInfo(Location location) {
        if (location != null) {
            TextView myLatitude = findViewById(R.id.latitude);
            TextView myLongitude = findViewById(R.id.longitude);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            String addressString = "No address found";
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (!addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    addressString = sb.toString();
                }
            } catch (IOException e) {
                Log.e("WhereAmI", "Error retrieving address", e);
            }
            String latitudeInfo = "Latitude: " + latitude ;
            String longitudeInfo = "Longitude: " + longitude;
            myLatitude.setText(latitudeInfo);
            myLongitude.setText(longitudeInfo);
        }
    }

}
