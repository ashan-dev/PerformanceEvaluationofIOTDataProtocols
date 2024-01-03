package com.example.performanceevaluationofiotdataprotocols;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MqttAndroidClient client;
    private static final String SERVER_URI = "tcp://test.mosquitto.org:1883";
    private static final String TAG = "MainActivity";
    private static final String TOPIC = "";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    private void mqttConnect() {
        try {
            System.out.println("test 1");
            MqttClient client = new MqttClient(SERVER_URI, CLIENT_ID, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            System.out.println("test 2");
            client.connect(options);
            System.out.println("test 3");


            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("The Connection was lost."+ cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Incoming message: " );
                    String payload = new String(message.getPayload());
                    System.out.println("Incoming message: " + payload);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update the UI (e.g., set text on a TextView)

                        }
                    });
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete: " );
                }
            });

            // Subscribe to the topic
//            client.subscribe(TOPIC, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void subscribe(String topicToSubscribe) {
        final String topic = topicToSubscribe;
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Subscription successful to topic: " + topic);
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    System.out.println("Failed to subscribe to topic: " + topic);

                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttConnect();

        Button button = (Button) findViewById(R.id.StartButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailScreen();
            }
        });



        Spinner spinner = findViewById(R.id.planets_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("RGB");
        arrayList.add("Light");
        arrayList.add("Proximity");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList );
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);



//        client.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean reconnect, String serverURI) {
//                if (reconnect) {
//                    System.out.println("Reconnected to : " + serverURI);
//                    // Re-subscribe as we lost it due to new session
//                    subscribe("iotlab/al/sensors");
//                } else {
//                    System.out.println("Connected to: " + serverURI);
//                    subscribe("iotlab/al/sensors");
//                }
//            }
//
//            @Override
//            public void connectionLost(Throwable cause) {
//                System.out.println("The Connection was lost.");
//            }
//
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws
//                    Exception {
//                String newMessage = new String(message.getPayload());
//                System.out.println("Red: " + Integer.parseInt(newMessage.split(",")[0]));
//                System.out.println("Green: " + Integer.parseInt(newMessage.split(",")[1]));
//                System.out.println("Blue: " + Integer.parseInt(newMessage.split(",")[2]));
////               txv_light.setText(newMessage.split("&")[0]);
////               txv_proximity.setText(newMessage.split("&")[1]);
//
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });

    }
    public void openDetailScreen(){
        Intent intent = new Intent(this, DetailedScreen.class);
        startActivity(intent);
    }
}