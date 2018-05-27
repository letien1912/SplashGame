package game.splashgame.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "GameData")
data class GameData( @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id")
                            var id: Long?,
                            @ColumnInfo(name = "question") var question: String,
                            @ColumnInfo(name = "level") var level: String,
                            @ColumnInfo(name = "number") var number: Int,
                            @ColumnInfo(name = "status") var status: String)