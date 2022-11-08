package com.bodzify.repository.network.request.dto

import com.bodzify.model.MineTrack

class MineTrackDownloadRequestDto (
    val title: String,
    val artist: String,
    val url: String,
    val duration: Int,
    val releasedOn: Int,
) {
    constructor (mineSong: MineTrack) : this(
        mineSong.title,
        mineSong.artist,
        mineSong.url,
        mineSong.duration,
        mineSong.releaseOn
    )
}