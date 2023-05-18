package leaveapp.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import leaveapp.bean.LeaveDetails;
import leaveapp.dao.LeaveDetailsDAO;

public class LeaveTrackingView
{
	Scanner sc;
	LeaveDetailsDAO leaveDetailsDAO;
	
	public LeaveTrackingView()
	{
	 sc = new Scanner(System.in);
	 leaveDetailsDAO = new LeaveDetailsDAO();
	}
	public void startApp() 
	{
		int choice;
		Lable1:
		do
		{
			System.out.println("\t\t Leave Tracking System");
			System.out.println("\t____Login As____");
			System.out.println("\t    1.Admin");
			System.out.println("\t    2.Employee");
			System.out.println("\t    3.Exit From App");
			System.out.println("\t_______________"); // add 3. exit
			System.out.println("\tEnter your choice : ");
			choice = sc.nextInt();
		
			if (choice==1)
			{
				String empname;
				userloop1:
				do
				{
				System.out.println("Enter your User ID");
				int userID = sc.nextInt();
				empname=leaveDetailsDAO.validateUser("Admin", userID);
				if(empname==null) 
					{
						System.out.println("Invalid User ... Please rennetr UserID");
						continue userloop1; 
					}
				}while(empname==null);
				if(empname!=null)
				{
					System.out.println("Welcome "+empname);
					int choice1=1;
					do
					{
						showAllLeaveRecords();
						System.out.println("\t____Options for You____");
						System.out.println("\t    1.Approve OR Reject Leave ");
						System.out.println("\t    2.Go to Main Menu ");
						System.out.println("\t_______________"); // add 3. exit
						System.out.println("\tEnter your choice : ");
						choice1 = sc.nextInt();
										
						if(choice1==1)
							takeActionOnLeave(empname);
					} while(choice1!=2);
					if (choice1==2) 
						continue Lable1;
				}
				
			}
			if (choice ==2)
			{
				String empname;
				int userID;
				userloop2:
				do
				{
				System.out.println("Enter your User ID");
				userID = sc.nextInt();
				empname=leaveDetailsDAO.validateUser("Employee", userID);
				if(empname==null) 
					{
						System.out.println("Invalid User ... Please rennetr UserID");
						continue userloop2; 
					}
				}while(empname==null);
				if(empname!=null)
				{
					System.out.println("Welcome "+empname);
					int choice1=1;
					do
					{
						showEmployeeLeaveRecords(userID);
						System.out.println("\t____Options List____");
						System.out.println("\t    1.Apply Leave");
						System.out.println("\t    2.Update Or Delete Leave application");
						System.out.println("\t    3.Go to Main Menu");
						System.out.println("\t_______________");
						System.out.println("\tEnter your choice : ");
						choice1 = sc.nextInt();
			
						switch(choice1)
						{
							case 1:	applyForLeave(userID);
									break;
							case 2:	changeLeaveApplication(empname,userID);
									break;									
			 			}
		
					} while(choice1!=3);
					if (choice1==3) 
						continue Lable1;
				}
			}
			else
			{
				      leaveDetailsDAO.closeResources();
				      break;
			}
					continue Lable1;
			}while(choice!=3);
	    }
	
		public void showAllLeaveRecords()
		{
			System.out.println("\t ------ Leave records for you to take action OR already taken action ----- ");
			List<LeaveDetails> leaveObjects = leaveDetailsDAO.getAllLeaveDetails();
			System.out.print("Row No |Empid|" +"      Emp Name    |"+ "   DOA    |" + "   From    |" + "  ToDate   |" + 
					"Days  | Approval Status|"+"      Reason       \n" );
			System.out.println("_____________________________________________________________________________________________\n");
			Iterator<LeaveDetails> itr = leaveObjects.iterator(); 
			int row=1;
			while(itr.hasNext())
			{
					LeaveDetails obj = itr.next();
					System.out.println("  " + row + "    |" + obj.getEmpid() + " | "+obj.getEmpname()+"     |" + obj.getDateapplied() + "|" + obj.getFromdate() 
					+ " | " + obj.getTodate() +	"|  " + (int)obj.getNoofdays() + "  |    " + obj.getApprovalstatus()
					+"    |"+obj.getReason());
					row++;
			}
			System.out.println("_____________________________________________________________________________________________\n");
		}
		public void showEmployeeLeaveRecords(int empid)
		{
			int row=1;
			
			List<LeaveDetails> leaveObjects = leaveDetailsDAO.getAllLeaveDetails(empid);
			Iterator<LeaveDetails> itr = leaveObjects.iterator();
			System.out.println("\tFollowing are your leave appliaction details ... ");
			System.out.println("_____________________________________________________________________________________________\n");
			System.out.print("Row No |   Empid  |" + "   DOA    |" + "   From    |" + "  ToDate   |" + 
			"Days  | Approval Status |"+"      Reason       \n" );
			System.out.println("_____________________________________________________________________________________________\n");
			while(itr.hasNext())
			{
					LeaveDetails obj = itr.next();
					System.out.println("  " + row + "   |" + obj.getEmpid() + " |" + obj.getDateapplied() + "|" + obj.getFromdate() 
					+ " | " + obj.getTodate() +	"|  " + (int)obj.getNoofdays() + "  |    " + obj.getApprovalstatus()
					+"    |"+obj.getReason());
					row++;
			}
			System.out.println("_____________________________________________________________________________________________\n");
		
			
		}
		
		public void takeActionOnLeave(String empname)
		{
			
			System.out.println("\t"+ empname + ", enter the Row No to take action ");
			int row = sc.nextInt(); 
			System.out.println("\tYou have selected leave record " + row);
			System.out.println("\tEnter (1) to approve and (0) to reject leave. ");
			int action = sc.nextInt();
			int rowsupdated =0;
			if (action == 1)
				rowsupdated =leaveDetailsDAO.updateLeaveDetails(row,  "Approved");
			else if (action == 0)
				rowsupdated =leaveDetailsDAO.updateLeaveDetails(row,  "Rejected");
			
			System.out.println("\t" + rowsupdated + " Leave Record Updated.");
		}

		public void applyForLeave(int empid)
		{
			int rowinserted;
			
			LeaveDetails leaveDeatailsObject = acceptLeaveDetails(empid);
			rowinserted = leaveDetailsDAO.addLeaveDetails(leaveDeatailsObject);
			System.out.println(rowinserted + " Leave application submitted successfully.");
		}
		public LeaveDetails acceptLeaveDetails(int empid)
		{
			LeaveDetails leaveDetailsObject = new LeaveDetails();
			leaveDetailsObject.setEmpid(empid);
			boolean result=false;
			dateloop:
			do
		    {
			
			System.out.println("\t______Enter Leave Application Details_________  ");
			System.out.println("Enter From date in (yyyy/mm/dd) format :");
			leaveDetailsObject.setFromdate(sc.next());
			System.out.println("Enter To date in (yyyy/mm/dd) format :");
			leaveDetailsObject.setTodate(sc.next());
			try
			{
			result=compareDates(leaveDetailsObject.getFromdate(),leaveDetailsObject.getTodate());
			}
			catch(StringIndexOutOfBoundsException e)
			{
				System.out.println("Please reenter Date in proper format like  yyyy/mm/dd ");
				continue dateloop;
			}
			
			if(result!= true)
			{
			System.out.println("From date can not be less than current Date  And To Date can not be less that From Date.. please reenter ");
				continue dateloop  ;
			}
			sc.nextLine();
			
				reasonloop:
				do	
				{
					System.out.println("Enter the Leave Reason :");
					 leaveDetailsObject.setReason(sc.nextLine());
					if(leaveDetailsObject.getReason().length()==0)
					{
						System.out.println("Reason can not be blank...please eenter");
						continue reasonloop ;
					}
					
				}while(leaveDetailsObject.getReason().length()==0);
		    }while(result==false);
			return leaveDetailsObject;
		}
		
		public boolean compareDates(String frmDate,String toDate)
		{
			
			int yy = Integer.parseInt(frmDate.substring(0,4));
			int mm = Integer.parseInt(frmDate.substring(5,7));
			int dd = Integer.parseInt(frmDate.substring(8,10));
			LocalDate fdate = LocalDate.of(yy,mm ,dd);
		
			yy = Integer.parseInt(toDate.substring(0,4));
			mm = Integer.parseInt(toDate.substring(5,7));
			dd = Integer.parseInt(toDate.substring(8,10));
			LocalDate tdate = LocalDate.of(yy,mm,dd);
			
			if ((tdate.isAfter(fdate) || tdate.equals(fdate)) &&  fdate.isAfter(LocalDate.now()))
				return true;
			
			return false;
			
				
		}
		public void changeLeaveApplication(String empname,int empid)
		{
			System.out.println("\tEnter Row No to update : ");
			int row = sc.nextInt();
			if(!leaveDetailsDAO.checkIfActionTaken(row))
			{
				System.out.println("\t" + empname +" you have selected Leave Record No " + row);
				System.out.println("\tEnter (e) to edit and (d) to cancle leave application ");
				char ans =sc.next().charAt(0);
				if(ans=='e')
				{
					
					LeaveDetails leaveDetailsObject = acceptLeaveDetails(empid);
					leaveDetailsDAO.updateLeaveDetails(row, leaveDetailsObject.getFromdate(),
														   leaveDetailsObject.getTodate(),
														   leaveDetailsObject.getReason());
					System.out.println("Changes in leave application done successfully ");
					
				}
				else if(ans == 'd')
				{
					leaveDetailsDAO.deleteLeaveDetails(row);
						System.out.println("\t" + empname + "your Leave Application is canclelled.");
				}
			}
			else
			{
					System.out.println("\t" + empname + "Action is already taken on your Leave Application.\n\t\t  It can't be changed now");
			}
		}
			
		
}
