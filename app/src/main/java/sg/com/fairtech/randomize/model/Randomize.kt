package sg.com.fairtech.randomize.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "randomize_list")
data class Randomize (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "list_name")
    var list_name: String,

    @ColumnInfo(name = "list_item")
    var list_item: String

)
