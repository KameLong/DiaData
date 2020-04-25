package kamelong.com.diadata

import kamelong.com.tool.SQLiteHelper
import kamelong.com.tool.ShiftJISBufferedReader
import org.w3c.dom.Document
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.collections.ArrayList


/**
 * 時刻表データの主体となるもの
 *１冊の時刻表のイメージ
 */
class DiaData{
    var id:UUID= UUID.randomUUID()
    var name:String=""

    /**
     * 時刻表のリスト
     */
    var routes:ArrayList<Route> = ArrayList()
    var trainTypes:ArrayList<TrainType> = ArrayList()
    var diagramNames:ArrayList<String> = ArrayList()
    fun loadFile(file:File){
        if(!file.isFile){
            throw Exception("not found this file")
        }
        if(file.name.endsWith(".oud")){
            val br= ShiftJISBufferedReader(InputStreamReader(FileInputStream(file), "Shift-JIS"))
            val route=Route(this)
            route.fromOuDia(br)
            this.routes.add(route)
        }
        if(file.name.endsWith("json")){
        }
    }

    fun saveAsSQL(filePath:String){
        val file= File(filePath)
        if(file.isFile){
            file.delete()
        }
        val startTime=System.currentTimeMillis()
        val sqlite=SQLiteHelper(filePath)
        sqlite.execute(Route.createTableSQL())
        sqlite.execute(Diagram.createTableSQL())
        sqlite.execute(Train.createTableSQL())
        sqlite.beginTransaction()

        for(route in routes){
            route.toSQL(sqlite)
        }
        sqlite.endTransaction()

        println("time=${System.currentTimeMillis()-startTime}")
    }
    fun saveAsOuDia(filePath:String,routeIndex:Int){
        routes[routeIndex].saveAsOuDia(filePath,trainTypes)
    }
    fun saveAsXml(filePath:String){
        val document: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()
        document.appendChild(routes[0].toXml(document))
        val transformerFactory = TransformerFactory.newInstance()
        val transformer: Transformer = transformerFactory.newTransformer()
        val source = DOMSource(document)
        val result = StreamResult(File(filePath))
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "Shift_JIS");
        transformer.transform(source, result)
    }
    fun saveAsJson(filePath:String,routeIndex:Int){
        routes[routeIndex].saveAsJSON(filePath)
    }


}