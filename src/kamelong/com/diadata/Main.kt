package kamelong.com.diadata

import java.io.File


fun main(){
    val start=System.currentTimeMillis()
    println("test")
    val data=DiaData()
    println(System.currentTimeMillis()-start)
    data.loadFile(File("C:\\Users\\kame\\Documents\\sample.oud"))
    println(System.currentTimeMillis()-start)
//    data.saveAsSQL("test.aod")
    println(System.currentTimeMillis()-start)
    data.saveAsXml("C:\\Users\\kame\\Documents\\sample.xml")
    println(System.currentTimeMillis()-start)
    data.saveAsJson("C:\\Users\\kame\\Documents\\sample.json",0)
    println(System.currentTimeMillis()-start)
}