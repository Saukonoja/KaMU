package kamu;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Random;
import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.logging.strategies.RecordCountLogUploadStrategy;
import org.kaaproject.kaa.schema.sample.logging.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class logSender {
    private static final Logger LOG = LoggerFactory.getLogger(KaMU.class);
    String macStr = "";
    boolean conn = false;
    
        public void sendLog() throws InterruptedException{
            led led = new led();
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
                led.ledon();
                LOG.info("Kaa client started");
            }

            @Override
            public void onStopped() {
                led.ledoff();
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
        while (!conn){
            try{
                conn = InetAddress.getByName("192.168.142.38").isReachable(1000);
            }catch (Exception e){
                conn = false;
                LOG.info("Connection to Kaa server");
            }
            if (conn) {
                kaaClient.start();
            }else {
               led.ledtoggle();
            }       
        }
        while (conn){           
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            System.out.println(currentTime);

            Random random = new Random();
            LogData log = new LogData(macStr, random.nextInt(50), currentTime);
            kaaClient.addLogRecord(log);

            led.ledtoggle();

            LOG.info("Log record {} sent", log.toString());
            Thread.sleep(10000);                
        }
        // Collect log record delivery futures and corresponding log record creation timestamps.
        //while (true) {
       
        kaaClient.stop();
        System.exit(0); 
        //}
        } 
}
