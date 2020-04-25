package kamelong.com.diadata

import java.util.*
import kotlin.collections.ArrayList

/**
 * 運用を表すクラス
 * このクラスは複数のLineFileにまたがることができる。
 */
class Operation {
    var id:UUID= UUID.randomUUID()
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
    var operationItem:ArrayList<OperationItem> = ArrayList()

    fun conbineOperation(item:OperationItem){
        item.operation.operationItem.remove(item)
        operationItem.add(item)
        item.operation=this
        
    }
}