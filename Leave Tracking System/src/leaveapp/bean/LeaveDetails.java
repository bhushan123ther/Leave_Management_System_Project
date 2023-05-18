package leaveapp.bean;

public class LeaveDetails
{
	private int leaveId;
	private int empid;
	private String dateapplied;
	private String fromdate;
	private String todate;
	private float noofdays;
	private String Reason;
	private String approvalstatus;
	private String empname;
	public String getEmpname() {
		return empname;
	}
	public void setEmpname(String empname) {
		this.empname = empname;
	}
	public int getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}
	public int getEmpid() {
		return empid;
	}
	public void setEmpid(int empid) {
		this.empid = empid;
	}
	public String getDateapplied() {
		return dateapplied;
	}
	public void setDateapplied(String dateapplied) {
		this.dateapplied = dateapplied;
	}
	public String getFromdate() {
		return fromdate;
	}
	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}
	public String getTodate() {
		return todate;
	}
	public void setTodate(String todate) {
		this.todate = todate;
	}
	public float getNoofdays() {
		return noofdays;
	}
	public void setNoofdays(float noofdays) {
		this.noofdays = noofdays;
	}
	public String getReason() {
		return Reason;
	}
	public void setReason(String reason) {
		Reason = reason;
	}
	public String getApprovalstatus() {
		return approvalstatus;
	}
	public void setApprovalstatus(String approvalstatus) {
		this.approvalstatus = approvalstatus;
	}
	@Override
	public String toString() {
		return "LeaveDetails [leaveId=" + leaveId + ", empid=" + empid + ", dateapplied=" + dateapplied + ", fromdate="
				+ fromdate + ", todate=" + todate + ", noofdays=" + noofdays + ", Reason=" + Reason
				+ ", approvalstatus=" + approvalstatus + "]";
	}
	
	public LeaveDetails() {}

}
