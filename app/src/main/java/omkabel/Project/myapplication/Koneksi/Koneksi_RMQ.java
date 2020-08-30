package omkabel.Project.myapplication.Koneksi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import omkabel.Project.myapplication.Features.Myservices;

public class Koneksi_RMQ {
    Settings settings = new Settings();
    String user = "iot_pertanian";
    String pass = "iotpertanian";
    String host = "167.205.7.226";
    String vhost = "/iotpertanian";
    String exchanges_name = "amq.fanout";
    String queue_name_publish = "mqtt-subscription-ec:fa:bc:63:16:a0qos0";
    String queue_name_subscribe = "Telur";
    String routingKey = "Telur";
    final Myservices myservices;
    public Koneksi_RMQ(Myservices myservices) {
        this.myservices = myservices;
    }


    // Define new Connection Factory
    ConnectionFactory factory = new ConnectionFactory();


    /**
     * Setup Connection Factory
     * Load Setting Connection RMQ Sebagai Parameter Koneksi
     */
    public void setupConnectionFactory() {
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri("amqp://"+user+":"+pass+"@"+host);
            factory.setVirtualHost(vhost);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Publish data lewat RMQ
     * @param message
     */
    public void publish(String message) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String messageOn = message ;
            channel.basicPublish("", queue_name_publish,null,messageOn.getBytes());
            myservices.Berhasil();


        } catch (IOException e) {
            Log.d("Publish Error", e.getMessage());
            myservices.Gagag();
        } catch (TimeoutException e) {
            Log.d("Publish Error", e.getMessage());
            myservices.Gagag();
        } catch (Exception e) {
            Log.d("Publish Error", e.getMessage());
            myservices.Gagag();
        }

    }

    /**
     * Optional, Send Speed for threading speed
     * @throws InterruptedException
     */
    public void SendSpeed() throws InterruptedException {
        Thread.sleep(500); //0.5 sec
    }

    /**
     * Fungsi untuk subscribe data RMQ
     * @param handler
     * @param subscribeThread
     */
    public void subscribe(final Handler handler, Thread subscribeThread)
    {
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.basicQos(0);
                        channel.queueBind(queue_name_subscribe, exchanges_name, routingKey);
                        QueueingConsumer consumer = new QueueingConsumer(channel);
                        channel.basicConsume(queue_name_subscribe, true, consumer);
                        QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                        if (delivery != null){

                            try{

                                String message = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                    message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                                }
                                Log.d("ConsumeDataRMQ", "MessageConsumed" + message);

                                Message msg = handler.obtainMessage();
                                Bundle bundle = new Bundle();

                                bundle.putString("msg", message);
                                msg.setData(bundle);
                                handler.sendMessage(msg);

                                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                            }catch (Exception e){
                                channel.basicReject(delivery.getEnvelope().getDeliveryTag(),true);
                            }
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e1) {
                        Log.d("", "Connection broken: " + e1.getClass().getName());
                        try {
                            Thread.sleep(4000); //sleep and then try again
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            }
        });
        subscribeThread.start();
    }

}
