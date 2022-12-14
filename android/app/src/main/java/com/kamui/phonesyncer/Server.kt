package com.kamui.phonesyncer

import androidx.room.*

@Entity
data class Server(
    @ColumnInfo(name = "name") var name: String, @ColumnInfo(name = "address") var address: String, @ColumnInfo(name = "port") var port: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

@Dao
interface ServerDao {
    @Query("SELECT * FROM server")
    fun getAll(): List<Server>

    @Insert
    fun insert(server: Server)

    @Delete
    fun delete(server: Server)

    @Update
    fun update(server: Server)
}
