package de.burrotinto.burroplayer.interfaces.serial.executors

import de.burrotinto.burroplayer.adapter.random.RandomAdapter
import org.springframework.stereotype.Service

/**
 * Created by derduke on 08.03.17.
 */
@Service
class MVCRandomExecutor(private val randomAdapter: RandomAdapter) : RandomExecutor {

    override fun execute(command: Int) {
        randomAdapter.playRandom()
    }
}
