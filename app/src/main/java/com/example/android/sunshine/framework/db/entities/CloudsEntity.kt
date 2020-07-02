package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.android.sunshine.core.domain.forecast.Clouds
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Clouds")
data class CloudsEntity(
    @PrimaryKey
    @ColumnInfo(name = "all")
    val all:Long
):Parcelable{
    @Ignore
    constructor(clouds: Clouds) : this(all = clouds.all)
}