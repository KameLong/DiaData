package kamelong.com.diadata

import com.google.gson.JsonObject
import kamelong.com.tool.SDlog
import kamelong.com.tool.SQLiteHelper
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.sql.ResultSet

class StationTime(@Transient val train:Train):Cloneable {
    //着時刻、秒単位
    var depTime: Time = Time(train.route)
        private set
    //発時刻、秒単位
    var ariTime: Time =Time(train.route)
        private set
    var stopType: StopType = StopType.NONE
    //発着番線　-1の時はデフォルト
    var stopTrack: Int = -1

    /**
     * OuDia形式の時刻情報を格納する
     */
    fun setOuDiaTimeValue(value: String) {
        if (value.length == 0) {
            stopType = StopType.NONE
            return
        }
        if (!value.contains(";")) {
            stopType = StopType.values()[value.toInt()]
            return
        }
        stopType = StopType.values()[value.split(";")[0].toInt()]
        val timeValue = value.split(";")[1]
        if (value.contains("/")) {
            ariTime.fromOuDiaString(timeValue.split("/")[0])
            if (value.split("/")[1].length != 0) {
                depTime.fromOuDiaString(timeValue.split("/")[1])
            }
        } else {
            depTime.fromOuDiaString(timeValue)
        }
    }
    /**
     * OuDia形式の番線情報を格納する
     */
    fun setOuDiaTrackValue(value: String) {
        var value = value
        try {
            if (value.length == 0) {
                stopTrack = -1
                return
            }
            if (value.contains(";")) {
                value = value.split(";")[0]
            }
            stopTrack = value.toInt() - 1
        } catch (e: Exception) {
            stopTrack = -1
            SDlog.log(e)
        }

    }

    fun toJSON():JsonObject{
        val json=JsonObject()
        json.addProperty("a",ariTime.time)
        json.addProperty("d",depTime.time)
        json.addProperty("ty",stopType.ordinal)
        json.addProperty("tr",stopTrack)
        return json
    }
    fun toSQL(sqLiteHelper: SQLiteHelper){
        if(isNull){
            return
        }
        val sql="insert into stop_time (trainID,sequence,ariTime,depTime,stopType,stopTrack) values(?,?,?,?,?,?)"
        val statement=sqLiteHelper.getStatement(sql)
        statement.setString(1,train.trainID.toString())
        statement.setInt(2,train.stationTime.indexOf(this))
        statement.setInt(3,ariTime.time)
        statement.setInt(4,depTime.time)
        statement.setInt(5,stopType.ordinal)
        statement.setInt(6,stopTrack)
        statement.executeUpdate()
    }
    fun fromSQL(rs:ResultSet){
        ariTime.time=rs.getInt("ariTime")
        depTime.time=rs.getInt("depTime")
        stopType= StopType.values()[rs.getInt("stopType")]
        stopTrack=rs.getInt("stopTrack")

    }
    fun getOuDiaString(oudia2ndFrag: Boolean): String {
        var result: String = ""
        if (stopType == StopType.NONE) {
            return result
        }
        result += stopType.ordinal
        if(ariTime.isNull()&&depTime.isNull()){
            return result
        }
        if (ariTime.isNull()) {
            result += ariTime.toOuDiaString() + "/"
        }
        if (depTime.isNull()) {
            result += depTime.toOuDiaString()
        }
        if (oudia2ndFrag) {
            result += "$" + stopTrack
        }
        return result
    }
    fun saveAsXml(document:Document):Element{
        val timeDom=document.createElement("時刻明細")
        timeDom.appendChild(document.createElement("駅名").apply { textContent=train.diagram.route.stations[train.stationTime.indexOf(this@StationTime)].name })
        timeDom.appendChild(document.createElement("発車時刻").apply {
            if(depTime.isNull()){
                textContent=ariTime.toXmlString()

            }else{
                textContent=depTime.toXmlString()

            }
        })
        timeDom.appendChild(document.createElement("停車時間").apply { textContent=Time.minus(depTime,ariTime).toXmlString2() })
        timeDom.appendChild(document.createElement("停車種類").apply {
            textContent=
                when(stopType){
                    StopType.NONE->"0"
                    StopType.STOP->"0"
                    StopType.PASS->"2"
                    StopType.NOVIA->"2"
            } })
        return timeDom
    }
    val isNull:Boolean
    get(){
        return stopType==StopType.NONE
    }


}
enum class StopType{
    NONE,STOP,PASS,NOVIA
}
