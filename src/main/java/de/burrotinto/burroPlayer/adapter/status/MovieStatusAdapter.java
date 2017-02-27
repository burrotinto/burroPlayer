package de.burrotinto.burroPlayer.adapter.status;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import de.burrotinto.burroPlayer.media.MediaRemote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import static java.lang.Thread.sleep;

/**
 * Created by derduke on 14.02.17.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MovieStatusAdapter implements InitializingBean, Runnable {
    private final MediaRemote mediaRemote;
    private final PinValue pin;

    private GpioPinDigitalOutput runningPin;

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            runningPin = GpioFactory.getInstance().provisionDigitalOutputPin(RaspiPin.getPinByName(pin.getMoviestatus()),
                    "LAEUFT", PinState.LOW);
            new Thread(this).start();
        } catch (UnsatisfiedLinkError e) {
            log.error("NO RRASPI", e);
        }
    }

    @Override
    public void run() {
        while (true) {
            runningPin.setState(mediaRemote.isSomeoneRunning());
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
