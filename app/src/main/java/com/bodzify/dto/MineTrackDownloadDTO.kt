package com.bodzify.dto

import com.bodzify.model.MineTrack

class MineTrackDownloadDTO (
    val title: String,
    val artist: String,
    val url: String,
    val duration: Int,
    val releaseDate: Int,
) {
    constructor (mineSong: MineTrack) : this(
        mineSong.title,
        mineSong.artist,
        mineSong.url,
        mineSong.duration,
        mineSong.releaseOn
    )
}