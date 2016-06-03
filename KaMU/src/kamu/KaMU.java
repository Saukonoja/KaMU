package kamu;

import java.net.InetAddress;
import java.util.Scanner;

public class KaMU {
    
    static boolean conn;
    
    public static void main(String[] args) {
        
        while (!conn){
            try{
                conn = InetAddress.getByName("192.168.142.38").isReachable(1000);
            }catch (Exception e){
                conn = false;
                System.out.println("Connection to Kaa server failed");
            }
            if (conn) {
                KaaController controller = new KaaController();
                controller.start();
            }else {
               //led.ledtoggle();
            }       
        }
        
        
    }  
}