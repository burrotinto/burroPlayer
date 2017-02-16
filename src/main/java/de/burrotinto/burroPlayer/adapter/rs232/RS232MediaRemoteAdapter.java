package de.burrotinto.burroPlayer.adapter.rs232;

import de.burrotinto.burroPlayer.adapter.status.StatusAdapter;
import de.burrotinto.comm.IgetCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by derduke on 14.02.17.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RS232MediaRemoteAdapter implements InitializingBean, Runnable {
    private final PauseExecutor pauseExecutor;
    private final PlayerExector playerExector;
    private final StatusExecutor statusExecutor;
    private final StopExecutor stopExecutor;
    private final WrongCodeExecutor wrongCodeExecutor;

    private final StatusAdapter statusAdapter;
    private final ControllBytes controllBytes;

    private final Optional<Execute>[] executes = new Optional[256];
    private final IgetCommand<Integer> empfaenger;

    public void getNextBefehl() throws InterruptedException {
        int code = empfaenger.holen();
        statusAdapter.somethingHappens();
        executes[code].orElse(wrongCodeExecutor).execute(code);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(this).start();
        for (int i = 0; i < executes.length; i++) {
            executes[i] = Optional.empty();
        }
        for (int i = controllBytes.getStartRange(); i < controllBytes.getEndRange(); i++) {
            executes[i] = Optional.of(playerExector);
        }
        executes[controllBytes.getPause()] = Optional.of(pauseExecutor);
        executes[controllBytes.getStop()] = Optional.of(stopExecutor);
        executes[controllBytes.getStatus()] = Optional.of(statusExecutor);
    }

    @Override
    public void run() {
        while (true) {
            try {
                getNextBefehl();
            } catch (InterruptedException e) {
                log.error("Parsing error", e);
            }
        }
    }
}

