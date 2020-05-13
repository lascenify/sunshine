package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.example.android.sunshine.core.domain.Wind
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Wind")
data class WindEntity(
    @ColumnInfo(name = "speed")
    val speed:Double?,

    @ColumnInfo(name = "degrees")
    val directionInDegrees:Double?
):Parcelable{
    @Ignore
    constructor(wind: Wind?) : this(
        speed = wind?.speed,
        directionInDegrees = wind?.directionInDegrees)
}