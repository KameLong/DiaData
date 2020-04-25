package kamelong.com.diadata

import java.io.File


fun main(){
    val start=System.currentTimeMillis()
    println("test")
    val data=DiaData()
    println(System.currentTimeMillis()-start)
    data.loadFile(File("C:\\Users\\kame\\Documents\\sample2.oud"))
    println(System.currentTimeMillis()-start)
//    data.saveAsSQL("test.aod")
    println(System.currentTimeMillis()-start)
    data.saveAsXml("C:\\Users\\kame\\Documents\\sample2.xml")
    println(System.currentTimeMillis()-start)
    data.saveAsJson("C:\\Users\\kame\\Documents\\sample2.json",0)
    println(System.currentTimeMillis()-start)
}