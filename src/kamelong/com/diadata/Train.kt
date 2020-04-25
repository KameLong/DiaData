package kamelong.com.diadata

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kamelong.com.tool.SQLiteHelper
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.PrintWriter
import java.sql.ResultSet
import java.util.*
import kotlin.collections.HashMap

class Train(val route: Route, val diagram:Diagram){
    var trainID:UUID= UUID.randomUUID()
    private set

    /**
     * 複数列車を同一列車とするとき、BlockIDは同一となる
     */
    var trainBlockID:UUID=UUID.randomUUID()
    private set

    //方向
    var direction:Direction=Direction.DOWN
    //列車種別
    var type:Int=0

    //列車番号
    var number:String=""
    //列車名
    var name:String=""
    var count:String=""
    var remark:String=""

    var stationTime:ArrayList<StationTime> = ArrayList<StationTime>()

    /**
     * 運用
     */
    var operationItem:ArrayList<OperationItem> = ArrayList()

    /**
     * 初期設定
     */
    init{

        operationItem.add(OperationItem(Operation(),this))
    }


    //時刻表順のindexをダイヤデータのindexに変更する
    fun stationIndex(timeTableIndex:Int):Int{
        if(timeTableIndex<0){
            throw ArrayIndexOutOfBoundsException("timetableIndex=$timeTableIndex")
        }
        if(timeTableIndex>=route.stationCount){
            throw ArrayIndexOutOfBoundsException("timetableIndex=$timeTableIndex")
        }
        if(direction==Direction.DOWN){
            return timeTableIndex
        }else{
            return route.stationCount-timeTableIndex-1
        }
    }

    //OuDia形式のデータを取り込む
    fun setOuDiaValue(key:String,value:String) {
        when (key) {
            "trainID"->trainID=UUID.fromString(value)
            "trainBlockID"->trainBlockID=UUID.fromString(value)
            "Syubetsu" -> type = value.toInt()
            "Ressyabangou" -> number = value
            "Ressyamei" -> name = value
            "Gousuu" -> count = value
            "EkiJikoku" -> setOuDiaTime(value.split(","))
            "RessyaTrack" -> setOuDiaTrack(value.split(","))
            "Bikou" -> remark = value
        }

    }
    fun saveAsOuDia(out: PrintWriter) {
        out.println("Ressya.")
        out.println("trainID=${trainID}")
        out.println("trainBlockID=${trainBlockID}")
        if (direction == Direction.DOWN) {
            out.println("Houkou=Kudari")
        } else {
            out.println("Houkou=Nobori")
        }
        out.println("Syubetsu=$type")
        if (number.length > 0) {
            out.println("Ressyabangou=$number")
        }
        if (name.length > 0) {
            out.println("Ressyamei=$name")
        }
        if (count.length > 0) {
            out.println("Gousuu=$count")
        }
        out.println("EkiJikoku=" + getEkijikokuOudia(false))
        if (remark.length > 0) {
            out.println("Bikou=$remark")
        }
        out.println(".")
    }

    private fun setOuDiaTime(values:List<String>){
        stationTime = ArrayList()
        for (i in 0 until route.stationCount) {
            stationTime.add(StationTime(this))
        }
        var i = 0
        while (i < values.size && i < route.stationCount) {
            stationTime[stationIndex(i)].setOuDiaTimeValue(values[i])
            i++
        }

    }
    private fun setOuDiaTrack(value:List<String>) {
        var i = 0
        while (i < value.size && i < route.stationCount) {
            stationTime[stationIndex(i)].setOuDiaTrackValue(value[i])
            i++
        }
    }
    /**
     * OuDia形式の駅時刻行を作成します。
     * @param secondFrag trueの時oudia2nd形式に対応します。
     * @return
     */
    private fun getEkijikokuOudia(secondFrag: Boolean): String? {
        val result = StringBuilder()
        if (stationTime.size > route.stationCount) {
            println("駅数オーバーフロー")
            return ""
        }
        for (i in 0 until stationTime.size) {
            val stationIndex: Int = stationIndex(i)
            result.append(stationTime[stationIndex].getOuDiaString(secondFrag))
            result.append(",")
        }
        return result.toString()
    }

    fun toJSON():JsonObject{
        val json=JsonObject()
        json.addProperty("trainID",trainID.toString())
        json.addProperty("trainBlockID",trainBlockID.toString())
        json.addProperty("direction",direction.ordinal)
        json.addProperty("type",type)
        json.addProperty("number",number)
        json.addProperty("name",name)
        json.addProperty("count",count)
        json.addProperty("remark",remark)
        val stationTimeJson=JsonArray()
        for (sTime in stationTime){
            stationTimeJson.add(sTime.toJSON())
        }
        json.add("stationTime",stationTimeJson)
        return json
    }
    fun toSQL(sqLiteHelper: SQLiteHelper){
        val sql="insert into train (trainID,diagramID,sequence,direction,type,number,name,count,remark) values(?,?,?,?,?,?,?,?,?)"
        val statement=sqLiteHelper.getStatement(sql)
        statement.setString(1,trainID.toString())
        statement.setString(2,diagram.diagramID.toString())
        statement.setInt(3,diagram.train[direction.ordinal].indexOf(this))
        statement.setInt(4,direction.ordinal)
        statement.setInt(5,type)
        statement.setString(6,number)
        statement.setString(7,name)
        statement.setString(8,count)
        statement.setString(9,remark)
        statement.executeUpdate()
        for(time in stationTime){
            time.toSQL(sqLiteHelper)
        }
    }
    fun fromSQL(rs:ResultSet,diagramMap:HashMap<UUID,Diagram>){
        trainID=UUID.fromString(rs.getString("trainID"))
        direction= Direction.values()[rs.getInt("direction")]
        type=rs.getInt("type")
        number=rs.getString("number")
        name=rs.getString("name")
        count=rs.getString("count")
        remark=rs.getString("remark")
    }
    fun saveAsXml(document: Document):Element{
        val trainDom=document.createElement("列車明細")
        trainDom.appendChild(document.createElement("列車番号").apply { textContent=trainID.toString() })
        trainDom.appendChild(document.createElement("列車名").apply { textContent=name })
        trainDom.appendChild(document.createElement("列車号番号").apply {
            if(count.length==0){
                textContent="0"
            }else{
                textContent=count
            }
        })
        trainDom.appendChild(document.createElement("列車種別").apply { textContent=type.toString() })
        trainDom.appendChild(document.createElement("動力種別").apply { textContent="2" })
        trainDom.appendChild(document.createElement("輸送種別").apply { textContent="2" })
        trainDom.appendChild(document.createElement("運転日").apply { textContent=diagram.route.diagrams.indexOf(diagram).toString() })
        trainDom.appendChild(document.createElement("他線へ直通_起点側").apply { textContent="False" })
        trainDom.appendChild(document.createElement("他線へ直通_起点側_反転").apply { textContent="False" })
        trainDom.appendChild(document.createElement("他線へ直通_終点側").apply { textContent="False" })
        trainDom.appendChild(document.createElement("他線へ直通_終点側_反転").apply { textContent="False" })
        trainDom.appendChild(document.createElement("他線へ直通_中間部").apply { textContent="False" })
        trainDom.appendChild(document.createElement("時刻表要素").apply {
            if(direction==Direction.DOWN){
                for(time in stationTime){
                    if(!time.isNull) {
                        appendChild(time.saveAsXml(document))
                    }
                }

            }
            else{
                for(time in stationTime.reversed()){
                    if(!time.isNull) {
                        appendChild(time.saveAsXml(document))
                    }
                }
            }


             })

        return trainDom





    }


    companion object {
        fun createTableSQL(): String {
            return "create table train(id int primary key,trainID text,diagramID text,sequence int,direction int,type int,number tet,name text,count text,remark text)"
        }
    }

}
enum class Direction(){
    DOWN,
    UP
}