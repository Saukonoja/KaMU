package kamu;

 

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class led {
    static final GpioController gpio = GpioFactory.getInstance();
    static final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.LOW);
    
        public void ledoff() {
            gpio.shutdown();
        }
        public void ledon(){
            pin.setShutdownOptions(true, PinState.LOW);
            pin.toggle();
        }
        public void ledtoggle(){
            try {
            pin.toggle();
            Thread.sleep(500);
            pin.toggle();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }     
        }
       
     
}
  