package kamelong.com.diadata

/**
 * このクラスはAOdiaで時刻表現する際に用いるクラスです
 */
class Time(@Transient val route: Route){
    /**
     * 秒単位の時刻
     */
    var time:Int=-1
    set(value){
        field=value
    }
    get(){
        if(field<0){
            return -1
        }
        while(field<route.diagramStartTime){
            field+=24*3600
        }
        while(field>route.diagramStartTime+24*3600){
            field-=24*3600
        }
        return field
    }
    /**
     * 文字列形式の時刻を秒の数値に変える
     * @param sTime 3桁から6桁の数字で構成された文字列
     */

    fun fromOuDiaString(sTime:String) {
        var hh = 0
        var mm = 0
        var ss = 0
        try {
            when (sTime.length) {
                3 -> {
                    hh = sTime.substring(0, 1).toInt()
                    mm = sTime.substring(1, 3).toInt()
                }
                4 -> {
                    hh = sTime.substring(0, 2).toInt()
                    mm = sTime.substring(2, 4).toInt()
                }
                5 -> {
                    hh = sTime.substring(0, 1).toInt()
                    mm = sTime.substring(1, 3).toInt()
                    ss = sTime.substring(3, 5).toInt()
                }
                6 -> {
                    hh = sTime.substring(0, 2).toInt()
                    mm = sTime.substring(2, 4).toInt()
                    ss = sTime.substring(4, 6).toInt()
                }
                else -> throw TimeFormatException("sTime.length=${sTime.length}")
            }
        }catch (e:NumberFormatException){
            throw TimeFormatException("NumberFormatException:${e.message}")
        }
        if (hh < 0) {
            throw TimeFormatException("hh=${hh}")
        }
        if (mm < 0) {
            throw TimeFormatException("mm=${mm}")
        }
        if (ss < 0) {
            throw TimeFormatException("ss=${ss}")
        }
        time= 3600 * hh + 60 * mm + ss
    }
    fun toOuDiaString():String {
        if (time < 0) return ""
        val ss = time % 60
        time = time / 60
        val mm = time % 60
        time = time / 60
        val hh = time % 24
        if (ss == 0) {
            return hh.toString() + String.format("%02d", mm)
        } else {
            return hh.toString() + String.format("%02d", mm) + String.format("%02d", ss)
        }
    }
    fun toXmlString():String{
        var time2=time
        if(time2<0)return "0001/01/01 0:00:00"
        val ss = time2 % 60
        time2 = time2 / 60
        val mm = time2 % 60
        time2 = time2 / 60
        val hh = time2 % 24
        return "2009/01/01 "+hh.toString() +":"+ String.format("%02d", mm) +":"+ String.format("%02d", ss)
    }
    fun toXmlString2():String{
        var time2=time
        if(time2<0)return "00:00:00"
        val ss = time2 % 60
        time2 = time2 / 60
        val mm = time2 % 60
        time2 = time2 / 60
        val hh = time2 % 24
        return hh.toString() +":"+ String.format("%02d", mm) +":"+ String.format("%02d", ss)
    }
    fun isNull():Boolean{
        return time<0
    }
    companion object{
        fun minus(a:Time,b:Time):Time{
            if(a.isNull()||b.isNull()){
                return Time(a.route).apply { time=-1 }
            }
            if(a.time<b.time){
                return Time(a.route).apply { time=a.time+24*3600-b.time }
            }
                return Time(a.route).apply { time=a.time-b.time }
        }
    }

}
class TimeFormatException(message: String) : Exception(message) {

}