package com.example.kotlinjetpackdogs.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class DogBreed(
    @ColumnInfo("breed_id")
    @SerializedName("id")
    val breedId: String?,

    @ColumnInfo("dog_name")
    @SerializedName("name")
    val dogBreed: String?,

    @ColumnInfo("life_span")
    @SerializedName("life_span")
    val lifeSpan: String?,

    @ColumnInfo("breed_group")
    @SerializedName("breed_group")
    val breedGroup: String?,

    @ColumnInfo("bred_for")
    @SerializedName("bred_for")
    val bredFor: String?,

    @ColumnInfo("temperament")
    @SerializedName("temperament")
    val temperament: String?,

    @ColumnInfo("dog_url")
    @SerializedName("url")
    val imageUrl: String?
): Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}
