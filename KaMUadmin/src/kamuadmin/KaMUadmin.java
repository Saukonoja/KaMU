/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kamuadmin;

import java.util.Scanner;
import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.registration.UserAttachCallback;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import org.kaaproject.kaa.common.endpoint.gen.UserAttachResponse;
import org.kaaproject.kaa.schema.sample.event.kamu.ChangeProfile;
import org.kaaproject.kaa.schema.sample.event.kamu.KaMUEventClassFamily;

/**
 *
 * @author h9073
 */
public class KaMUadmin {

    static KaaClient kaaClient;
    static int profileID;
    
    public static void main(String[] args) {
        createKaaClient();
        menu();
        do {
            menu();    
        } while (profileID != 0); 
    }
    
    public static void menu() {
        int sw = 0;
        //BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        
        Scanner in = new Scanner(System.in); 
        System.out.println("1. Water level");
        System.out.println("2. Water flow");
        System.out.println("3. Exit");
            
        try{
  
        sw = in.nextInt();
        
        switch (sw){
            case 1:
                sendProfileAll();
                break;
            case 2:
                System.out.println("You selected 2");
                break;
            case 3: 
                System.exit(0); 
                break;
        }
        
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public static void createKaaClient(){
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
            @Override
            public void onStarted() {    
                attachUser();
            }

            @Override
            public void onStopped() {
             
            }
        });
        kaaClient.start();
    }
    
    public static void attachUser(){
        kaaClient.attachUser("dev", "token", new UserAttachCallback() {
            @Override
            public void onAttachResult(UserAttachResponse response) {
                System.out.println("Attach response " + response.getResult());
                
                if (response.getResult() == SyncResponseResultType.SUCCESS){
                    System.out.println("User attached");
                    //receiveEvents();
                }
                else{
                    kaaClient.stop();
                    System.out.println("Stopped");
                }
            }
        });
    }
    
    public static void sendProfileAll(){
        final EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        final KaMUEventClassFamily tecf = eventFamilyFactory.getKaMUEventClassFamily();

        tecf.sendEventToAll(new ChangeProfile(profileID));
        System.out.println("Change profile request sent");
    }
}
