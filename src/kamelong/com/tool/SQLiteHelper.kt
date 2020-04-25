package kamelong.com.tool

import java.sql.Connection
import java.sql.SQLException
import java.sql.DriverManager
import java.sql.PreparedStatement


class SQLiteHelper(filePath: String){
    private lateinit var conn: Connection
    init{
        try {
            // db parameters
            val url = "jdbc:sqlite:$filePath"
            // create a connection to the database
            conn = DriverManager.getConnection(url)

            println("Connection to SQLite has been established.")

        } catch (e: SQLException) {
            println(e.message)
        }
    }
    /**
     * 終了処理
     */
    fun close(){
        conn.close();
    }

    /**
     * 新規テーブル作成
     */
    fun createNewTable(tableName:String,values:ArrayList<Pair<String,String>>){
        var sql="create table if not exists $tableName(id integer primary key"
        for(value in values){
            sql+=",${value.first} ${value.second} not null"
        }
        sql+=")"
        val statement=conn.createStatement()
        statement.execute(sql)
    }
    /**
     * insert
     */
    fun beginTransaction(){
        conn.autoCommit=false
    }
    fun endTransaction(){
        conn.commit()
        conn.autoCommit=true
    }
    fun getStatement(sql:String):PreparedStatement{
        return conn.prepareStatement(sql)
    }
    fun insert(tableName: String,values:ArrayList<Pair<String,String>>){
        var sql="insert into $tableName("
        for (value in values){
            if(value!=values.last()){
                sql+="${value.first},"
            }else{
                sql+="${value.first})"
            }
        }
        sql+=" values("
        for (value in values){
            if(value!=values.last()){
                sql+="${value.first},"
            }else{
                sql+="${value.first})"
            }
        }
    }
    fun execute(sql:String){
        val statement=conn.createStatement()
        statement.execute(sql)
    }

}