package kamelong.com.diadata

import com.google.gson.JsonObject
import kamelong.com.tool.SQLiteHelper
import java.io.PrintWriter
import java.sql.ResultSet
import java.util.*


/**
 * 発着番線を示すクラス
 */
class Track(@Transient val station:Station){
    var trackID:UUID= UUID.randomUUID()
        private set

    var name:String=""
    var shortName:String=""
    //OuDia形式のデータを取り込む
    fun setOuDiaValue(key:String,value:String) {
        when (key) {
            "TrackName" -> name = value
            "TrackRyakusyou" -> shortName = value
        }
    }

    fun toJSON():JsonObject{
        val json=JsonObject()
        json.addProperty("name",name)
        json.addProperty("shortName",shortName)
        return json
    }
    fun toSQL(sqLiteHelper: SQLiteHelper){
        val sql="insert into track (trackID,stationID,sequence,name,shortName) values(?,?,?,?,?)"
        val statement=sqLiteHelper.getStatement(sql)
        statement.setString(1,trackID.toString())
        statement.setString(2,station.stationID.toString())
        statement.setInt(3,station.track.indexOf(this))
        statement.setString(4,name)
        statement.setString(5,shortName)
        statement.executeUpdate()
    }
    fun fromSQL(rs: ResultSet, diagramMap:HashMap<UUID,Diagram>){
        trackID=UUID.fromString(rs.getString("trackID"))
        name=rs.getString("name")
        shortName=rs.getString("shortName")
    }
    companion object {
        fun createTableSQL(): String {
            return "create table track(id int primary key,trackID text,stationID text,sequence int,name text,shortName text)"
        }
    }


}