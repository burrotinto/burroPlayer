package de.burrotinto.burroPlayer.adapter.web;

import de.burrotinto.burroPlayer.media.MediaRemote;
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
    private final MediaRemote mediaRemote;


    @RequestMapping("/")
    public String getMovies(@RequestParam(value = "id", required = false) Integer id, Model model) {
        Optional<Integer> i = Optional.ofNullable(id);
        i.ifPresent(integer -> mediaRemote.play(integer));
        log.info("Get Movies list, {} optional", i);

        model.addAttribute("playing", mediaRemote.isSomeoneRunning());
        model.addAttribute("movies", mediaRemote.getMovieMap());
        return "movies";
    }


}