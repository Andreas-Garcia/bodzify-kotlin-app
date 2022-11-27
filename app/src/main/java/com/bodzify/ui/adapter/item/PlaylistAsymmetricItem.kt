package com.bodzify.ui.adapter.item

import android.os.Parcel
import android.os.Parcelable.Creator
import com.bodzify.model.playlist.Playlist
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem

class PlaylistAsymmetricItem : AsymmetricItem {
    private var columnSpan = 0
    private var rowSpan = 0
    var position = 0
        private set
    var playlistName: String? = ""

    @JvmOverloads
    constructor(playlist: Playlist, position: Int) {
        this.columnSpan = playlist.trackCount + 1
        this.rowSpan = playlist.trackCount + 1
        this.position = position
        this.playlistName = playlist.name
    }

    constructor(`in`: Parcel) {
        readFromParcel(`in`)
    }

    override fun getColumnSpan(): Int {
        return columnSpan
    }

    override fun getRowSpan(): Int {
        return rowSpan
    }

    override fun toString(): String {
        return String.format("%s: %sx%s", position, rowSpan, columnSpan)
    }

    override fun describeContents(): Int {
        return 0
    }

    private fun readFromParcel(`in`: Parcel) {
        columnSpan = `in`.readInt()
        rowSpan = `in`.readInt()
        position = `in`.readInt()
        playlistName = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(columnSpan)
        dest.writeInt(rowSpan)
        dest.writeInt(position)
    }

    companion object {
        /* Parcelable interface implementation */
        @JvmField val CREATOR: Creator<PlaylistAsymmetricItem> = object
            : Creator<PlaylistAsymmetricItem> {
            override fun createFromParcel(`in`: Parcel): PlaylistAsymmetricItem? {
                return PlaylistAsymmetricItem(`in`)
            }

            override fun newArray(size: Int): Array<PlaylistAsymmetricItem?> {
                return arrayOfNulls(size)
            }
        }
    }
}