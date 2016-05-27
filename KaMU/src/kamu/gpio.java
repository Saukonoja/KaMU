/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kamu;

 

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author h3694
 */
public class gpio {
    static final GpioController gpio = GpioFactory.getInstance();
    static final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyLED", PinState.HIGH);
    
        public void ledoff() {
            gpio.shutdown();
        }
        public void ledon(){
            pin.setShutdownOptions(true, PinState.LOW);
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
  