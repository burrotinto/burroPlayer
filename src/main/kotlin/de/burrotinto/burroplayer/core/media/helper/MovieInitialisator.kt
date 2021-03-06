package de.burrotinto.burroplayer.core.media.helper

import de.burrotinto.burroplayer.core.media.analysator.MovieAnalyser
import de.burrotinto.burroplayer.core.media.remote.IndexMediaRemoteService
import de.burrotinto.burroplayer.core.media.remote.IndexOrganizationMediaRemoteService
import de.burrotinto.burroplayer.values.BurroPlayerConfig
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Created by Florian Klinger on 06.07.17, 16:17.
 */

@Service
class MovieInitialisator(val config: BurroPlayerConfig, val analyser: MovieAnalyser) {

    @Throws(IOException::class)
    fun initAllClipsByNumberAndPath(path: String, remote: IndexOrganizationMediaRemoteService) {
//        log.info("Filewalk: " + path)

        //Rekursiv
        Files.walk(Paths.get(path)).filter {
            !it.toFile().isDirectory && !remote.getIndexForFile(it.toFile().absolutePath).isPresent &&
                    analyser.isMovie(it.toFile().absolutePath)
        }.forEach {
            //                log.info("       is Movie File: " + p.toFile().absolutePath)

            val newPath = renameFriendly(it)

            var number = computateNumber(newPath)

            if (remote.hasPlayerAt(number)) {
                number = config.minNumber
                while (remote.hasPlayerAt(number)) {
                    number++
                }
            }
//                log.info("            {} on pos {} hinzufügen", p.toFile().absolutePath, number)

            remote.addMovie(number, newPath.toFile().absolutePath)
        }
    }


    private fun computateNumber(path: Path): Int {
        var number = 0
        var isNumber = false
        try {
            path.toFile().name.toCharArray().forEach {
                val x = Integer.parseInt(it + "")
                isNumber = true
                number = number * 10 + x
            }
        } catch (e: NumberFormatException) {

        }
        return if (isNumber) number else path.toFile().name.hashCode()
    }

    private fun renameFriendly(path: Path): Path {
        if (path.toFile().name.contains(" ")) {
            val newFile = File(path.toFile().absolutePath.replace(" ", "_"))
            path.toFile().renameTo(newFile)

//                    log.info("    File: {} renamed to: {}", p.toFile().absolutePath, newFile.absolutePath)

            return newFile.toPath()
        } else {
            return path

        }
    }


    @Throws(IOException::class)
    fun setPrefixMovieOnPos(path: String, remote: IndexMediaRemoteService, prefix: String, pos: Int) {
        Files.walk(File(path).toPath()).forEach {
            if (it.toFile().name.toLowerCase().startsWith(prefix)) {
                remote.addMovie(pos, it.toFile().absolutePath)
                return@forEach
            }
        }
    }
}