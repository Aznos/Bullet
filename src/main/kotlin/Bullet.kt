package com.aznos

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

fun main() {
    val logger: Logger = LogManager.getLogger("Bullet")

    logger.info("Hello world!")
}