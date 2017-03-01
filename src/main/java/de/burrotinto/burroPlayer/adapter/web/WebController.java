package de.burrotinto.burroPlayer.adapter.web;

import de.burrotinto.burroPlayer.adapter.file.FileChecker;
import de.burrotinto.burroPlayer.media.remote.IndexMediaRemoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebController {
    private final IndexMediaRemoteService indexMediaRemoteService;
    private final FileChecker fileChecker;

    @RequestMapping("/")
    public String getMovies(@RequestParam(value = "id", required = false) Integer id, @RequestParam(value = "pause",
            required = false, defaultValue = "false") Boolean pause, @RequestParam(value = "reload",
            required = false, defaultValue = "false") Boolean reload, Model model) {
        Optional<Integer> optionalID = Optional.ofNullable(id);
        optionalID.ifPresent(integer -> indexMediaRemoteService.play(integer));

        log.info("Get Movies list, {} optional", optionalID);

        if (pause) {
            indexMediaRemoteService.pause();
        }
        if (reload) {
            fileChecker.check();
        }
        if (indexMediaRemoteService.getPlayingIndex().isPresent()) {
            model.addAttribute("playing", indexMediaRemoteService.getPlayingIndex().get());
        } else {
            model.addAttribute("playing", "NONE");
        }


        model.addAttribute("movies", indexMediaRemoteService.getMovieMap());
        model.addAttribute("paused", indexMediaRemoteService.isPaused());
        return "movies";
    }

}