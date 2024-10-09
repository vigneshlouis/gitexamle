/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.jdbcdemo;

/**
 *
 * @author ELCOT
 */
import java.sql.*;
import java.util.*;
public class JDBCdemo {

    public static void main(String[] args) throws SQLException {
        String url="jdbc:mysql://localhost:3306/jdbcdemodb";
        String username="root";
        String password="Vignesh546&";
       
        Connection con=DriverManager.getConnection(url,username,password);
        insertUsingPst(con);
        
        //deleteRecord(con,400);
        //updateSalary(con,200,100000);
        //call_sp(con);
        //call_sp_getEmpById(con,100);
        readRecords(con);
       
        //call_sp_getNameById(con,200);
        //batchdemo(con);
        con.close();
       
    }
    static void readRecords(Connection con) throws SQLException
    { String query="select * from employees";
         Statement st=con.createStatement();
        ResultSet rs=st.executeQuery(query);
        ResultSetMetaData rsmd=rs.getMetaData();
        
         System.out.print(rsmd.getColumnName(1));
         System.out.print(rsmd.getColumnName(2));
         System.out.println(rsmd.getColumnName(3));
         
        while(rs.next()){
        System.out.print(rs.getInt(1));
        System.out.print(rs.getString(2)   );
        System.out.print(rs.getInt(3));
        System.out.println();
        }
        
    }
    static void insertRecord(Connection con,int id,String name,int salary)throws SQLException
    {
        String query="insert into employees values("+id+",'"+name+"',"+salary+");";
        Statement st=con.createStatement();
        int rows=st.executeUpdate(query);
        System.out.println(rows+" rows affected");
    }
     static void insertUsingPst(Connection con)throws SQLException
    {
        String query="insert into employees values(?,?,? )";
        con.setAutoCommit(false);
       PreparedStatement pst=con.prepareStatement(query);
       Scanner sc=new Scanner(System.in);
       
       while(true)
       {
           System.out.println("enter employee id");
       int id=sc.nextInt();
        System.out.println("enter employee name");
       String name=sc.next();
        System.out.println("enter salary");
       int salary=sc.nextInt();
       pst.setInt(1, id);
       pst.setString(2, name);
       pst.setInt(3, salary);
       int rows=pst.executeUpdate();
        System.out.println(rows+" rows inserted");
        System.out.println("are you want to commit (y/n)");
                String opt=sc.next();
                if(opt.equals("n"))
                    con.rollback();
                if(opt.equals("y"))
                    con.commit();
        
                System.out.println("are you want to insert another record(y/n)");
                String option=sc.next();
                if(option.equals("n"))
                    break;
        
       }
       con.commit();
    }
     
     static void deleteRecord(Connection con,int id) throws SQLException
     {
         String query="delete from employees where employee_id=?";
         PreparedStatement pst=con.prepareStatement(query);
         pst.setInt(1, id);
         int rows=pst.executeUpdate();
         System.out.println(rows+" rows affected");
     }
     static void updateSalary(Connection con,int id,int salary)throws SQLException
     {
          String query="update employees set emp_salary=? where employee_id=?";
         PreparedStatement pst=con.prepareStatement(query);
         pst.setInt(1, salary);
         pst.setInt(2, id);
         int rows=pst.executeUpdate();
         System.out.println(rows+" rows affected");
     }
     
     static void call_sp(Connection con)throws SQLException
     {
         String query="{call getEmp()}";
         CallableStatement cst=con.prepareCall(query);
         ResultSet rs=cst.executeQuery();
           while(rs.next()){
        System.out.print(rs.getInt(1)+" ");
        System.out.print(rs.getString(2)+" ");
        System.out.print(rs.getInt(3));
        System.out.println();
        }
         
     }
      static void call_sp_getEmpById(Connection con,int id)throws SQLException
     {
         String query="{call getEmpById(?)}";
         CallableStatement cst=con.prepareCall(query);
         cst.setInt(1, id);
         ResultSet rs=cst.executeQuery();
           while(rs.next()){
        System.out.print(rs.getInt(1)+" ");
        System.out.print(rs.getString(2)+" ");
        System.out.print(rs.getInt(3));
        System.out.println();
        }
         
     }
       static void call_sp_getNameById(Connection con,int id)throws SQLException
     {
         String query="{call getNameById(?,?)}";
         CallableStatement cst=con.prepareCall(query);
         cst.setInt(1, id);
         cst.registerOutParameter(2, Types.VARCHAR);
         cst.executeUpdate();
         
         System.out.println(cst.getString(2));
             
                 
         
        
         
     }
       static void batchdemo(Connection con) throws SQLException
       {
           Statement st=con.createStatement();
           con.setAutoCommit(false);
           String query1="update employees set emp_salary=300000 where employee_id=100";
           String query2="update employees set emp_salary=300000 where employee_id=200";
           
           st.addBatch(query1);
           st.addBatch(query2);
           
           int[]res=st.executeBatch();
          
           for(int i:res )
           {
               System.out.println(i+" rows affected");
               if(i>0)
                  continue;
               else
                   con.rollback();
           }
         
               con.commit();
           
       }
}
