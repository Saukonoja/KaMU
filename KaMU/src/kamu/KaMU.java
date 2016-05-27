/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kamu;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.concurrent.ExecutionException;
import java.util.Random;

import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.logging.strategies.RecordCountLogUploadStrategy;
import org.kaaproject.kaa.schema.sample.logging.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


/**
 *
 * @author h9073
 */
public class KaMU {
    
    //private static final int LOGS_TO_SEND_COUNT = 2;

    private static final Logger LOG = LoggerFactory.getLogger(KaMU.class);
    private static final GpioController gpio = GpioFactory.getInstance();
    private static final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String macStr = "";
        
        try {
            NetworkInterface netInf = NetworkInterface.getNetworkInterfaces().nextElement();
            byte[] mac = netInf.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macStr = sb.toString();
            System.out.println(sb.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        LOG.info("Kakke0.2 started");
        //Create a Kaa client with the Kaa desktop context.
        KaaClient kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
            @Override
            public void onStarted() {
                pin.setShutdownOptions(true, PinState.LOW);
                LOG.info("Kaa client started");
            }

            @Override
            public void onStopped() {
                gpio.shutdown();
                LOG.info("Kaa client stopped");
            }
        });

        // Set record count strategy for uploading logs with count threshold set to 1.
        // Defined strategy configuration informs to upload every log record as soon as it is created.
        // The default strategy uploads logs after either a threshold logs count
        // or a threshold logs size has been reached.
        kaaClient.setLogUploadStrategy(new RecordCountLogUploadStrategy(1));
        //kaaClient.setLogUploadStrategy(new PeriodicLogUploadStrategy(10, TimeUnit.SECONDS));

        // Start the Kaa client and connect it to the Kaa server.
        kaaClient.start();

        // Collect log record delivery futures and corresponding log record creation timestamps.
        //while (true) {
        for (int i = 0; i < 5; i++) {
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            System.out.println(currentTime);
            
            Random random = new Random();
            LogData log = new LogData(macStr, random.nextInt(50), currentTime);
            kaaClient.addLogRecord(log);
            
            pin.toggle();
            Thread.sleep(500);
            pin.toggle();
            
            LOG.info("Log record {} sent", log.toString());
            Thread.sleep(10000);
        }
        kaaClient.stop();
        //}
    }
}