package kamu;

import java.util.Scanner;

public class KaMU {
    
    public static void main(String[] args) throws InterruptedException {
        int sw = 0;
        //BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        
        Scanner in = new Scanner(System.in); 
        System.out.println("1. Water level");
        System.out.println("2. Water flow");
        System.out.println("3. Exit");
        while(sw != 3){
            
        try{
  
        sw = in.nextInt();
        
        switch (sw){
            case 1:
                logSender sender = new logSender();
                sender.sendLog();
                   break;
            case 2:
                System.out.println("You selected 2");
                break;
            case 3: 
                System.exit(0); 
                break;
        }
        
        }catch (Exception e){
             System.out.println(e.getMessage());
        }
       }
    }  
}