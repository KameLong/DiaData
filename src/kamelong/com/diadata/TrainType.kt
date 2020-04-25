package kamelong.com.diadata

import com.google.gson.JsonObject
import kamelong.com.tool.Color
import kamelong.com.tool.SQLiteHelper
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.PrintWriter
import java.sql.ResultSet
import java.util.*
class TrainType {
    var typeID:UUID= UUID.randomUUID()
    private set

    //種別名
    var name:String=""
    //種別略称
    var shortName:String=""

    //表示色
    var textColor:Color=Color()
    var diaColor:Color=Color()

    //ダイヤ線スタイル
    var diaLineStyle:TrainTypeDiaStyle=TrainTypeDiaStyle.SOLID
    //ダイヤ線
    var diaLineBold:Boolean=false
    var diaStopMark:Boolean=false
    //OuDia形式のデータを取り込む
    fun setOuDiaValue(key:String,value:String) {
        when (key) {
            "typeID"->typeID=UUID.fromString(value)
            "Syubetsumei" -> name = value
            "Ryakusyou" -> shortName = value
            "JikokuhyouMojiColor" -> textColor.setOuDiaColor(value)
//            "JikokuhyouFontIndex" -> fontIndex = value.toInt()
            "DiagramSenColor" -> diaColor.setOuDiaColor(value)
            "DiagramSenStyle" -> when (value) {
                "SenStyle_Jissen" -> diaLineStyle = TrainTypeDiaStyle.SOLID
                "SenStyle_Hasen" -> diaLineStyle = TrainTypeDiaStyle.DASH
                "SenStyle_Tensen" -> diaLineStyle = TrainTypeDiaStyle.DOT
                "SenStyle_Ittensasen" -> diaLineStyle = TrainTypeDiaStyle.CHAIN
            }
            "DiagramSenIsBold" -> diaLineBold = value == "1"
            "StopMarkDrawType" -> diaStopMark = value == "EStopMarkDrawType_DrawOnStop"
        }

    }
    fun toJSON():JsonObject{
        val json=JsonObject()
        json.addProperty("typeID",typeID.toString())
        json.addProperty("name",name)
        json.addProperty("shortName",shortName)
        json.addProperty("textColor",textColor.htmlColor)
        json.addProperty("diaColor",diaColor.htmlColor)
        json.addProperty("diaLineStyle",diaLineStyle.toString())
        json.addProperty("diaLineBold",diaLineBold)
        json.addProperty("diaStopMark",diaStopMark)
        return json
    }
    fun saveAsOuDia(output: PrintWriter){
        output.println("Ressyasyubetsu.")
        output.println("Syubetsumei=$name")
        output.println("Ryakusyou=$shortName")
        output.println("JikokuhyouMojiColor=" + textColor.oudiaString)
        output.println("DiagramSenColor=" + diaColor.oudiaString)
        when (diaLineStyle) {
            TrainTypeDiaStyle.SOLID -> output.println("DiagramSenStyle=SenStyle_Jissen")
            TrainTypeDiaStyle.DASH -> output.println("DiagramSenStyle=SenStyle_Hasen")
            TrainTypeDiaStyle.DOT -> output.println("DiagramSenStyle=SenStyle_Tensen")
            TrainTypeDiaStyle.CHAIN -> output.println("DiagramSenStyle=SenStyle_Ittensasen")
        }
        if (diaLineBold) {
            output.println("DiagramSenIsBold=1")
        }
        if (diaStopMark) {
            output.println("StopMarkDrawType=EStopMarkDrawType_DrawOnStop")
        } else {
            output.println("StopMarkDrawType=EStopMarkDrawType_Nothing")
        }

    }
    fun toSQL(sqLiteHelper: SQLiteHelper){
        val sql="insert into trainType(typeID,name,shortName,textColor,diaColor,diaLineStyle,diaLineBold,diaStopMark) values(?,?,?,?,?,?,?,?)"
        val statement=sqLiteHelper.getStatement(sql)
        statement.setString(1,typeID.toString())
        statement.setString(2,name)
        statement.setString(3,shortName)
        statement.setString(4,textColor.htmlColor)
        statement.setString(5,diaColor.htmlColor)
        statement.setString(6,diaLineStyle.name)
        statement.setBoolean(7,diaLineBold)
        statement.setBoolean(8,diaStopMark)
        statement.executeUpdate()
    }
    fun fromSQL(rs: ResultSet, diagramMap:HashMap<UUID,Diagram>){
        typeID=UUID.fromString(rs.getString("trackID"))
        name=rs.getString("name")
        shortName=rs.getString("shortName")
        textColor=Color(rs.getString("textColor"))
        diaColor=Color(rs.getString("diaColor"))
        diaLineStyle=TrainTypeDiaStyle.valueOf(rs.getString("diaLineStyle"))
        diaLineBold=rs.getBoolean("diaLineBold")
        diaStopMark=rs.getBoolean("diaStopMark")
    }
    fun toXml(document:Document):Element{
        val typeDom=document.createElement("列車種別明細")
        typeDom.appendChild(document.createElement("列車種別名").apply { textContent=name })
        typeDom.appendChild(document.createElement("描画色").apply { textContent="255-${textColor.getRed()}-${textColor.getGreen()}-${textColor.getBlue()}" })
        typeDom.appendChild(document.createElement("線種").apply { textContent="1" })
        typeDom.appendChild(document.createElement("停車通過").apply { textContent="0" })
        return typeDom
    }
    companion object {
        fun createTableSQL(): String {
            return "create table type(id int primary key,typeID text,name text,shortName text,textColor text,diaColor text,diaLineStyle text,diaLineBold int,diaStopMark int)"
        }
    }

}
enum class TrainTypeDiaStyle{
    SOLID,DASH,DOT,CHAIN
}
