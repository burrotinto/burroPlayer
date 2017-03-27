package de.burrotinto.burroPlayer.adapter.rs232.executors;

import de.burrotinto.burroPlayer.media.remote.IndexMediaRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by derduke on 16.02.17.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerExector implements Executor {
    private final IndexMediaRemoteService indexMediaRemoteService;

    @Override
    public void execute(int command) {
        log.info("Videostartbefehl: " + command);
        indexMediaRemoteService.play(command);
    }
}
