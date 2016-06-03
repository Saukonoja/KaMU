package kamu;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.event.EventFamilyFactory;
import org.kaaproject.kaa.client.event.registration.UserAttachCallback;
import org.kaaproject.kaa.client.logging.strategies.RecordCountLogUploadStrategy;
import org.kaaproject.kaa.common.endpoint.gen.SyncResponseResultType;
import org.kaaproject.kaa.common.endpoint.gen.UserAttachResponse;
import org.kaaproject.kaa.schema.sample.event.kamu.ChangeProfile;
import org.kaaproject.kaa.schema.sample.event.kamu.KaMUEventClassFamily;
import org.kaaproject.kaa.schema.sample.event.kamu.RegisterDevice;
import org.kaaproject.kaa.schema.sample.event.kamu.RegistrationAnswer;

public class KaaController {
    static KaaClient kaaClient;
    static int profileID;
    logSender sender = new logSender();
    
    public void start() {
        kaaClient = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
           @Override
           public void onStarted() {
               attachUser();
           }
        });
        kaaClient.start();
        kaaClient.setLogUploadStrategy(new RecordCountLogUploadStrategy(1));
    }
    
    public void menu() {
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
        
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void attachUser() {
         kaaClient.attachUser("dev", "token", new UserAttachCallback() {
            @Override
            public void onAttachResult(UserAttachResponse response) {
                System.out.println("Attach response " + response.getResult());
                
                if (response.getResult() == SyncResponseResultType.SUCCESS){
                    onUserAttached();
                }
                else{
                    kaaClient.stop();
                    System.out.println("Stopped");
                }
            }
        });
    }
    
    public void onUserAttached(){
        final EventFamilyFactory eventFamilyFactory = kaaClient.getEventFamilyFactory();
        final KaMUEventClassFamily tecf = eventFamilyFactory.getKaMUEventClassFamily();
        tecf.addListener(new KaMUEventClassFamily.Listener() {
            @Override
            public void onEvent(ChangeProfile event, String source) {
                System.out.println("ChangeProfile event");
                profileID = event.getProfileID();
                System.out.println("Got ID: " + profileID + " From: " + source);
                try{
                    sender.sendLog();
                }catch (Exception e){
                    System.out.println("Log send failed");
                }
            }   

            @Override
            public void onEvent(RegisterDevice event, String source) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onEvent(RegistrationAnswer event, String source) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }
    
}
