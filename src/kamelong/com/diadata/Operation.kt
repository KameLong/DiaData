package kamelong.com.diadata

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kamelong.com.tool.SQLiteHelper
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.util.*
import kotlin.collections.ArrayList

/**
 * 運用を表すクラス
 * このクラスは複数のLineFileにまたがることができる。
 */
class Operation {
    var operationID:UUID= UUID.randomUUID()
    /**
     * 運用名
     */
    var name:String=""
    /**
     * 車両名
     */
    var vehicleName:String=""
    /**
     * 両数
     */
    var vehicleNumber:Int=0

    /**
     * 運用要素リスト
     */
    var operationItems:ArrayList<OperationItem> = ArrayList()

    fun conbineOperation(item:OperationItem){
        item.operation.operationItems.remove(item)
        operationItems.add(item)
        item.operation=this
        
    }


    fun toJSON(): JsonObject {
        val json= JsonObject()
        json.addProperty("operationID",operationID.toString())
        json.addProperty("name",name)
        json.addProperty("vehicleName",vehicleName)
        json.addProperty("vehicleNumber",vehicleNumber)
        val items=JsonArray()
        for( item in operationItems){
            items.add(item.toJSON())
        }
        json.add("operationItems",items)
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