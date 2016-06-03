package kamu;

//import static kamu.KaaController.kaaClient;
import java.util.logging.Level;
import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.logging.strategies.RecordCountLogUploadStrategy;
import org.kaaproject.kaa.schema.sample.logging.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSender implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(KaMU.class);
    private Thread thread;
    private String threadName; 
   
    LogSender (String name) {
        threadName = name;   
        System.out.println("Creating " +  threadName );
    }
    


     public void start(){        
        
        if (thread == null)
        {
            System.out.println("Starting " +  threadName );
            thread = new Thread (this, threadName);
            thread.start();
        }    
    }
     
     
    @Override
    public void run() {
          Thread thisThread = Thread.currentThread();
          while (thread == thisThread){
              try {
                   sendLog();
              } catch (InterruptedException ex) {
                   java.util.logging.Logger.getLogger(LogSender.class.getName()).log(Level.SEVERE, null, ex);
                  }
      }
        
    }
    
    
    public void stop(){
        System.out.println("Stopping " +  threadName );
        thread = null;
    }
    
    
    public static void sendLog() throws InterruptedException{
        
        KaaClient kaaClient1 = Kaa.newClient(new DesktopKaaPlatformContext(), new SimpleKaaClientStateListener() {
            @Override
            public void onStarted() {          
               
            }

            @Override
            public void onStopped() {
             
            }
        });
        kaaClient1.setLogUploadStrategy(new RecordCountLogUploadStrategy(1));
        kaaClient1.start();
            
        while (KaMU.conn){                     
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            //System.out.println(currentTime);

            LogData log = new LogData(KaaController.getMac(), KaaController.profile, currentTime);
            KaaController.kaaClient.addLogRecord(log);
            

            Led.ledtoggle(2500);

            LOG.info("Log record {} sent", log.toString());
            Thread.sleep(10000);                
        }
        KaaController.kaaClient.stop();
        System.exit(0); 
    } 

 
}