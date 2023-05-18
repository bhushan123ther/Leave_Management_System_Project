package leaveapp.bean;

public class Employee {
	private int empid;
	private String empname;
	private String designation;
	private double empsalary;
	private String dateofjoining;
	private int roleid;
	private int leavebalance;

	public int getEmpid() {
		return empid;
	}

	public void setEmpid(int empid) {
		this.empid = empid;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getEmpsalary() {
		return empsalary;
	}

	public void setEmpsalary(double empsalary) {
		this.empsalary = empsalary;
	}

	public String getDateofjoining() {
		return dateofjoining;
	}

	public void setDateofjoining(String dateofjoining) {
		this.dateofjoining = dateofjoining;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}

	public int getLeavebalance() {
		return leavebalance;
	}

	public void setLeavebalance(int leavebalance) {
		this.leavebalance = leavebalance;
	}

	@Override
	public String toString() {
		return "Employee [empid=" + empid + ", empname=" + empname + ", designation=" + designation + ", empsalary="
				+ empsalary + ", dateofjoining=" + dateofjoining + ", roleid=" + roleid + ", leavebalance="
				+ leavebalance + "]";
	}

	public Employee() {
	}
}
