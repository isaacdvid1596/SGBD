/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gbd_sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author IsaacDavid
 */
public class DBManager {
    
    private String currUsername , currPassword, dbURL;
    Connection conn = null;
    PreparedStatement st = null;
    ResultSet res = null;
    
    
    public DBManager(String username , String password ,String dburl){
        currUsername = username;
        currPassword = password;
        dbURL = dburl;
    }
    
    
    public void connect() throws Exception{
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\IsaacDavid\\Desktop\\db\\"+dbURL);
    }
    
    
    public void disconnect() throws Exception
    {
        conn.close();
    }
    
    
    //Strings
    public String stringForSQL(String data[])
    {
        String generatedString = "";
        
        for(int i=0;i<data.length;i++)
        {
            if(i==data.length-1){
                generatedString += data[i];
            }else{
                generatedString += (data[i]+",");
            }
        }
        
        return generatedString;
    }
    
    public String stringForTable(String fieldNames [], String type [])
    {
        String generatedString  = "";
        
        for(int i=0;i<fieldNames.length;i++)
        {
            if(i==fieldNames.length-1)
            {
                generatedString += (fieldNames[i]+"("+type[i]+")");
            }
            else
            {
                generatedString += (fieldNames[i]+"("+type[i]+")");
            }
        }
        
        return generatedString;
    }
    
    
    
    //Tables
    public String showTables()
    {
        String sqlStatement = String.format("select * from sqlite_master where type='table';");
        
        return sqlStatement;
    }
    
    public String showTable(String tableName)
    {
        String sqlStatement = String.format("select * from %s;",tableName);
        
        return sqlStatement;
    }
   
    public String createTables(String tableName, String fielda,String fielda1,String fielda2,String fielda3,String fieldb,String fieldb1,String fieldc, String fieldc1)
    {
        String sqlStatement = String.format("create table %s (%s %s %s %s , %s %s, %s %s);", tableName,fielda,fielda1,fielda2,fielda3,fieldb,fieldb1,fieldc,fieldc1);
        
        return sqlStatement;
    }
    
    public String updateTables(String tableName,String colName,String newData)
    {
        String sqlStatement = String.format("alter table %s add column %s %s;",tableName,colName,newData);
        
        return sqlStatement;
    }
    
    
    public String deleteTable(String tableName)
    {
        String sqlStatement = String.format("drop table %s;",tableName);
        
        return sqlStatement;
    }
    //EndTable
    
    
    //Indexes 
    public String showIndexes()
    {
        String sqlStatement = String.format("select * from sqlite_master where type='index';");
        
        return sqlStatement;
    }
    
    public String createIndex(String indexName,String tableName,String columnName)
    {
        String sqlStatement = String.format("create index %s on %s(%s);",indexName,tableName,columnName);
        
        return sqlStatement;
    }
    
   public String deleteIndex(String indexName)
   {
       String sqlStatement = String.format("drop index %s;",indexName);
       
       return sqlStatement;
   }
    //EndIndexes
   
   
   //Triggers
   
   public String showTriggers()
   {
       String sqlStatement = String.format("select * from sqlite_master where type='trigger';");
       
       return sqlStatement;
   }
   
   public String createTriggers(String triggerName, String triggerTime,String triggerAction,String tableName)
   {
       String sqlStatement = String.format("create trigger %s %s %s on %s begin insert into %s ;",triggerName,triggerTime,triggerAction,tableName);
       
       return sqlStatement;
   }
   
   
   
   //    public String createTriggers(String triggerName, String triggerTime,String triggerAction,String tableName,String destinationTrigger, String param1,String param2, String param3,String param4)
//   {
//       String sqlStatement = String.format("create trigger %s %s %s on %s begin insert into %s (%s , %s); values (%s,%s);",
//               triggerName,triggerTime,triggerAction,tableName,destinationTrigger,param1,param2,param3,param4);
//       
//       return sqlStatement;
//   }
   
   
   public String deleteTrigger(String triggerName)
   {
       String sqlStatement = String.format("drop trigger %s;",triggerName);
       
       return sqlStatement;
   }
   //EndTrigger
   
   
   //Views
   public String showViews()
   {
       String sqlStatement = String.format("select * from sqlite_master where type='view';");
       
       return sqlStatement;
   }
   
   public String showView(String viewName)
   {
       String sqlStatement = String.format("select * from %s;",viewName);
       
       return sqlStatement;
   }
   
   public String createView(String viewName , String tableName)
   {
       String sqlStatement = String.format("create view %s as select * from %s;",viewName,tableName);
       
       return sqlStatement;
   }
   
   public String deleteView(String viewName)
   {
       String sqlStatement = String.format("drop view %s",viewName);
       
       return sqlStatement;           
   }
   //EndViews
 
    
    
    //functions
    
    public void populateComboBox(JComboBox CBox,String sqlStatement) throws Exception
    {
        st =  conn.prepareStatement(sqlStatement);
        res =  st.executeQuery();
        
        ArrayList<String> data = new ArrayList<String>();
        
        while(res.next())
        {
            data.add(res.getString(1));
        }
        
        String [] dataArr = new String[data.size()];
        dataArr = data.toArray(dataArr);
        
        CBox.setModel(new DefaultComboBoxModel(dataArr));
        
        st.close();
        res.close();
    }
    
    
    public void populateTable(JTable mainTable,String sqlStatement) throws Exception
    {
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        Vector columns = new Vector();
        st = conn.prepareStatement(sqlStatement);
        res = st.executeQuery();
        ResultSetMetaData metaData = res.getMetaData();
        
        //name of columns
        
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for(int column = 1 ; column <= columnCount; column++)
        {
            columnNames.add(metaData.getColumnName(column));
            columns.add(columnNames.get(column-1));
        }
        
        //data of table
        
        while(res.next())
        {
            Vector<Object> vector = new Vector<Object>();
            for(int columnIndex=1;columnIndex<=columnCount;columnIndex++)
            {
                vector.add(res.getObject(columnIndex));
            }
            
            data.add(vector);
        }
        
        DefaultTableModel tablemodel = new DefaultTableModel(data,columns);
        mainTable.setModel(tablemodel);
        
        st.close();
        res.close();
    }
    
    
    
    public void executeSQLQuery(String sqlStatement) throws Exception
    {
        st = conn.prepareStatement(sqlStatement);
        st.executeQuery();
        st.close();
    }
    
     public void executeSQL(String sqlStatement) throws Exception
    {
        st = conn.prepareStatement(sqlStatement);
        st.execute();
        st.close();
    }
    
    
    
    
    
    
}
