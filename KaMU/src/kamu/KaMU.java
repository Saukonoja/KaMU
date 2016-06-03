package kamu;

import java.net.InetAddress;
import java.util.Scanner;
import static org.kaaproject.kaa.client.channel.impl.channels.DefaultBootstrapChannel.LOG;

public class KaMU {
    static boolean conn;
    public static void main(String[] args) throws InterruptedException {
        
        KaaController controller = new KaaController("KaaController");
        
        
        while (true){
             if (conn) {
                Led.ledtoggle(0);
                controller.start();
                    
                }
            try{
                conn = InetAddress.getByName("192.168.142.46").isReachable(1000);
                 } catch (Exception e){
                    Led.ledtoggle(500);
                    LOG.info("Connection to Kaa server failed: " + e.getMessage());
                    
                }
               
           
            Thread.sleep(10000);
        }
        
       }
    
}