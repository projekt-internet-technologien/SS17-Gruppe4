package de.uniluebeck.ait.quickstart;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LedControl {
	
	
	public static void main(String[] args) throws InterruptedException {

		// get a handle to the GPIO controller
		final GpioController gpio = GpioFactory.getInstance();

		// creating the pin with parameter PinState.HIGH
		// will instantly power up the pin
		final GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "PinLED", PinState.HIGH);
		System.out.println("light is: ON");

		// wait 2 seconds
		Thread.sleep(2000);

		// turn off GPIO 1
		pin.low();
		System.out.println("light is: OFF");
		// wait 1 second
		Thread.sleep(1000);
		// turn on GPIO 1 for 1 second and then off
		System.out.println("light is: ON for 1 second");
		pin.pulse(1000, true);

		// turn on GPIO 1
		pin.high();
		System.out.println("light is: ON");
		
//		//LDR
//		final GpioPinDigitalInput LDRpin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, "PinLDR");
//		
//		System.out.println("LDR: " + LDRpin);
//		
//		//high = hell
//		if(LDRpin.isHigh()){
//			pin.low();
//			System.out.println("LDR is high");
//		}
//		
//		//low = dunkel
//		if(LDRpin.isLow()){
//			pin.high();
//			System.out.println("LDR is low");
//		}
		
		
		// release the GPIO controller resources
		gpio.shutdown();
	}
}
