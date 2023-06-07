package deptHeadCounter;

public class Department {
    private String deptCode;
    private String upperDeptCode;
    private int headCount;
    private boolean isHighestDept;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getUpperDeptCode() {
        return upperDeptCode;
    }

    public void setUpperDeptCode(String upperDeptCode) {
        this.upperDeptCode = upperDeptCode;
    }

    public int getHeadCount() {
        return headCount;
    }

    public boolean setHeadCount(int headCount) {
        if(isMoreThanThousand(headCount)){
            this.headCount = headCount;
            return true;
        }
        return false;
    }

    public boolean isHighestDept() {
        return isHighestDept;
    }

    public void setHighestDept(boolean highestDept) {
        isHighestDept = highestDept;
    }

    private boolean isMoreThanThousand(int headCount){
        return headCount <= 1000;
    }
}
