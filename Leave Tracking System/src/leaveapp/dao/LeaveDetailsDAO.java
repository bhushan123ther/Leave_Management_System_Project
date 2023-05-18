package leaveapp.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import leaveapp.bean.LeaveDetails;

public class LeaveDetailsDAO   {

	public static Connection con;
	ResultSet rs,rs1;
	static Statement st,st1;
	PreparedStatement pst,pst1;
	CallableStatement cst;
	Scanner sc;
	int id;
	static {
		con = DBConnection.getConnection();
		System.out.println("Database Connection established...");
		try {
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			st1 = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public int addLeaveDetails(LeaveDetails leaveDetailsObject) {
		int rowsAffected=0;
	
		try(PreparedStatement pst = con.prepareStatement("insert into leavedetails(empid,fromdate,todate,reason)"
				+ "values(?,?,?,?)"))
		{
			
			pst.setInt(1, leaveDetailsObject.getEmpid());
			pst.setString(2, leaveDetailsObject.getFromdate());
			pst.setString(3, leaveDetailsObject.getTodate());
			pst.setString(4, leaveDetailsObject.getReason());
			rowsAffected =pst.executeUpdate(); 
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return rowsAffected;
	}

	
	public int updateLeaveDetails(int rowNo, String action) 
	{
		int rowsupdated=0;
		try
		{
			 pst = con.prepareStatement("update leavedetails set approvalstatus=?" + "where leaveid = ? ");
			rs.absolute(rowNo);
		
			pst.setInt(2, rs.getInt("leaveID"));
			if (action.equals("Approved"))
					pst.setString(1, "Approved");
			else if (action.equals("Rejected"))
					pst.setString(1, "Rejected");
			rowsupdated = pst.executeUpdate();
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		
		return rowsupdated;
	}

	
	public boolean deleteLeaveDetails(int rowNo) {
		boolean flag=false;
		try
		{
			rs.absolute(rowNo);
			rs.deleteRow();
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}
		return flag;
	}
	
	public boolean checkIfActionTaken(int row)
	{
		boolean flag=false;
		try
		{
		rs.absolute(row);
		if(rs.getString(8).equalsIgnoreCase("pending"))
			flag = false;
		else
			flag = true;
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return flag;
	}
	

	
	public List<LeaveDetails> getAllLeaveDetails() {
		LeaveDetails leaveDetailsObject=null;;
		List<LeaveDetails> leavesList = new ArrayList<>();
		try
		{
			rs = st.executeQuery("select * from leavedetails ");
			rs1 = st1.executeQuery("select empid,empname from employee ");
			while (rs.next()) 
			{
				while(rs1.next())
				{
					if(rs.getInt("empid") == rs1.getInt("empid"))
					{
						leaveDetailsObject  = new LeaveDetails();
						leaveDetailsObject.setEmpid(rs.getInt("empid"));
						leaveDetailsObject.setEmpname(rs1.getString("empname"));
						leaveDetailsObject.setDateapplied(rs.getString("dateapplied"));
						leaveDetailsObject.setFromdate(rs.getString("fromdate"));
						leaveDetailsObject.setTodate(rs.getString("todate"));
						leaveDetailsObject.setNoofdays(rs.getFloat("noofdays"));;
						leaveDetailsObject.setReason(rs.getString("Reason"));
						leaveDetailsObject.setApprovalstatus(rs.getString("approvalstatus"));
					}
				}
				leavesList.add(leaveDetailsObject);
				rs1.beforeFirst();
			 }
		
	    }
		catch(SQLException e)
		{
			System.out.println("Runtime Error generated is : " + e.getMessage());
		}
		
		return leavesList;
	}

	
	public List<LeaveDetails> getAllLeaveDetails(int empid)
	{
	
		LeaveDetails leavedetailsObject=null;
		List<LeaveDetails> leavesList = new ArrayList<>();
		String query1 = "select * from leavedetails where empid = ?";
		try 
		{
			
			checkLeaveBalance(empid);
			pst = con.prepareStatement(query1,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			pst.setInt(1,empid);
			rs = pst.executeQuery();
			
			while (rs.next()) 
			{
				leavedetailsObject  = new LeaveDetails();
				
				leavedetailsObject.setEmpid(rs.getInt("empid"));
				leavedetailsObject.setDateapplied(rs.getString("dateapplied"));
				leavedetailsObject.setFromdate(rs.getString("fromdate"));
				leavedetailsObject.setTodate(rs.getString("todate"));
				leavedetailsObject.setNoofdays(rs.getFloat("noofdays"));;
				leavedetailsObject.setReason(rs.getString("Reason"));
				leavedetailsObject.setApprovalstatus(rs.getString("approvalstatus"));
				leavesList.add(leavedetailsObject);
			}	
			
			
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return leavesList;
	}
	public String validateUser(String role,int userID) 
	{
		boolean signal = true;
		String empname = null,emprole=null;
		
		try(CallableStatement cst = con.prepareCall("{call validateUser(?,?,?)}");)
		{
				cst.setInt(1, userID);
				cst.registerOutParameter(2, Types.VARCHAR);
				cst.registerOutParameter(3, Types.VARCHAR);
				cst.execute();
				empname = cst.getString(2);
				emprole = cst.getString(3);
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		if(emprole.equals("Admin"))
			return empname;
		
		if(empname!=null && emprole!=null && emprole.equals(role))
				return empname;
		
		else 
			return null;
		
	}
	
	public void checkLeaveBalance(int empid) throws SQLException
	{
		
		cst = con.prepareCall("{ call Leaves_Availed_Balance(?,?,?) }");
		cst.setInt(1,empid);
		cst.registerOutParameter(2,Types.FLOAT);
		cst.registerOutParameter(3,Types.FLOAT);
		boolean status = cst.execute();
		float leaveBalance = cst.getFloat(2);
		float leaveAvailed = cst.getFloat(3);
		System.out.println("\tYour Leave Balance is  "+leaveBalance);
		System.out.println("\tTotal Leaves availed  "+leaveAvailed);
	}

	
	public void updateLeaveDetails(int rowNo, String fromDate, String toDate, String reason) {
		
		int yy = Integer.parseInt(fromDate.substring(0,4));
		int mm = Integer.parseInt(fromDate.substring(5,7));
		int dd = Integer.parseInt(fromDate.substring(8,10));
		LocalDate fdate = LocalDate.of(yy,mm ,dd);
	
		yy = Integer.parseInt(toDate.substring(0,4));
		mm = Integer.parseInt(toDate.substring(5,7));
		dd = Integer.parseInt(toDate.substring(8,10));
		LocalDate tdate = LocalDate.of(yy,mm,dd);
		Period p = Period.between(fdate, tdate);
		int noofdays = p.getDays();
		System.out.println("Changed no of days " + (noofdays+1));
		try
		{
			rs.absolute(rowNo);
			rs.updateString(4, fromDate);
			rs.updateString(5, toDate);
			rs.updateFloat(6, (float)noofdays+1);
			rs.updateString(7, reason);
			rs.updateRow();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void closeResources()
	{
		try
		{
			if(rs1!= null)
					rs1.close();
			if(rs!= null)
					rs.close();
			if (pst != null)
					pst.close();
			if(st!=null)
					st.close();
			if(con!=null)
				con.close();
		System.out.println("Connection with Database closed successfully!");
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
}

