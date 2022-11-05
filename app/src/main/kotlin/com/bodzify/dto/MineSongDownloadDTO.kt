package com.bodzify.dto

import com.bodzify.model.MineSong
import java.util.*

class MineSongDownloadDTO (
    val title: String,
    val artist: String,
    val url: String,
    val duration: Int,
    val releaseDate: Int,
) {
    constructor (mineSong: MineSong) : this(
        mineSong.title,
        mineSong.artist,
        mineSong.url,
        mineSong.duration,
        mineSong.releaseDate
    )
}