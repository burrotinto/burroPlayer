package de.burrotinto.burroPlayer.adapter.rs232;

import de.burrotinto.burroPlayer.media.MediaRemote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by derduke on 16.02.17.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class PlayerExector implements Execute{
    private final MediaRemote mediaRemote;

    @Override
    public void execute(int command) {
        log.info("Videostartbefehl: " + command);
        mediaRemote.play(command);
    }
}
