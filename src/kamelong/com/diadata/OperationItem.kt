package kamelong.com.diadata

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

    /**
     * 初期設定
     */
    init{
        operation.operationItem.add(this)
    }



}