package kamelong.com.diadata

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kamelong.com.tool.SQLiteHelper
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.PrintWriter
import java.util.*

/**
 * 時刻表の「平日」とか「土日」別のデータ
 */
class Diagram(@Transient val route:Route){
    var diagramID:UUID= UUID.randomUUID()

    var name:String=""

    var train:Array<ArrayList<Train>> = arrayOf(ArrayList(),ArrayList())
    //OuDia形式のデータを取り込む
    fun setOuDiaValue(key:String,value:String) {
        when (key) {
            "DiaName" -> name = value
        }

    }
    /**
     * OuDia形式で出力します
     */
    fun toOuDia(out: PrintWriter) {
        out.println("Dia.")
        out.println("DiaName=$name")
        out.println("Kudari.")
        for (t in train[0]) {
            t.saveAsOuDia(out)
        }
        out.println(".")
        out.println("Nobori.")
        for (t in train[1]) {
            t.saveAsOuDia(out)
        }
        out.println(".")
        out.println(".")
    }
    fun toJSON():JsonObject{
        val json=JsonObject()
        json.addProperty("diagramID",diagramID.toString())
        json.addProperty("name",name)
        val trainJson=JsonObject()
        val downTrainJson=JsonArray()
        val upTrainJson=JsonArray()
        for(train in train[Direction.DOWN.ordinal]){
            downTrainJson.add(train.toJSON())
        }
        for(train in train[Direction.UP.ordinal]){
            upTrainJson.add(train.toJSON())
        }
        trainJson.add("down",downTrainJson)
        trainJson.add("up",upTrainJson)
        json.add("train",trainJson)
        return json
    }
    fun toSQL(sqLiteHelper: SQLiteHelper){
        val sql="insert into diagram (diagramID,lineFileID,sequence,name) values(?,?,?,?)"
        val statement=sqLiteHelper.getStatement(sql)
        statement.setString(1,diagramID.toString())
        statement.setString(2,route.routeID.toString())
        statement.setInt(3,route.diagrams.indexOf(this))
        statement.setString(4,name)
        statement.executeUpdate()
        for(train in train[Direction.DOWN.ordinal]){
            train.toSQL(sqLiteHelper)
        }
        for(train in train[Direction.UP.ordinal]){
            train.toSQL(sqLiteHelper)
        }
    }
    fun saveAsXml(document: Document):Element{
        val diagramDom=document.createElement("Trains")
        val trainsDown=document.createElement("TrainsDown")
        val trainsUp=document.createElement("TrainsUp")
        for(train in train[0]){
            trainsDown.appendChild(train.saveAsXml(document))
        }
        for(train in train[1]){
            trainsUp.appendChild(train.saveAsXml(document))
        }
        diagramDom.appendChild(document.createElement("TrainItems").apply {
            appendChild(trainsDown)
            appendChild(trainsUp)
        })
        return diagramDom
    }
    companion object {

        fun createTableSQL(): String {
            return "create table diagram(id int primary key,diagramID text,lineFileID text,sequence int,name text)"
        }
    }


}