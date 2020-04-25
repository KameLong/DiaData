package kamelong.com.diadata

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kamelong.com.tool.SQLiteHelper
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.util.*

/**
 * 運用のうち、1列車に対応するクラス
 * 複数のOperationItemのリストが一連の運用順となる
 * 運用順はOperation
 */
class OperationItem (var operation:Operation,var train:Train){
    var id:UUID= UUID.randomUUID()
    /**
     * 運用開始駅
     * -1の時はtrainの始発駅
     */
    var startStation:Int=-1
    /**
     * 運用終了駅
     * -1の時はtrainの終着駅
     */
    var endStation:Int=-1
    val route:Route
        get()=train.route

    /**
     * 初期設定
     */
    init{
        operation.operationItems.add(this)
    }

    fun toJSON(): JsonObject {
        val json= JsonObject()
        json.addProperty("operationItemID",id.toString())
        json.addProperty("startStation",startStation)
        json.addProperty("endStation",endStation)
        return json
    }
    fun toSQL(sqLiteHelper: SQLiteHelper){
//        val sql="insert into diagram (diagramID,lineFileID,sequence,name) values(?,?,?,?)"
//        val statement=sqLiteHelper.getStatement(sql)
//        statement.setString(1,diagramID.toString())
//        statement.setString(2,route.routeID.toString())
//        statement.setInt(3,route.diagrams.indexOf(this))
//        statement.setString(4,name)
//        statement.executeUpdate()
//        for(train in train[Direction.DOWN.ordinal]){
//            train.toSQL(sqLiteHelper)
//        }
//        for(train in train[Direction.UP.ordinal]){
//            train.toSQL(sqLiteHelper)
//        }
    }
    fun toXml(document: Document): Element {
        val diagramDom=document.createElement("Trains")
//        val trainsDown=document.createElement("TrainsDown")
//        val trainsUp=document.createElement("TrainsUp")
//        for(train in train[0]){
//            trainsDown.appendChild(train.saveAsXml(document))
//        }
//        for(train in train[1]){
//            trainsUp.appendChild(train.saveAsXml(document))
//        }
//        diagramDom.appendChild(document.createElement("TrainItems").apply {
//            appendChild(trainsDown)
//            appendChild(trainsUp)
//        })
        return diagramDom
    }


}