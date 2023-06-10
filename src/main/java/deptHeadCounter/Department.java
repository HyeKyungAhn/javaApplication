package deptHeadCounter;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private int count;
    private List<Department> children;

    Department(){}

    public Department(String name, int count){
        this.name = name;
        this.count = count;
        this.children = new ArrayList<>();
    }

    public void addChild(Department dept) {
        if (dept == this) {
            throw new IllegalArgumentException(dept.getName() +" 부서를 " + dept.getName() + " 부서의 하위부서로 둘 수 없습니다.");
        }

        this.children.add(dept);
    }

    public boolean addChild(String name, int count) {
        if (isUnderThousand(count)) {
            this.addChild(new Department(name, count));
            return true;
        }
        return false;
    }

    public List<Department> getChildren(){
        return children;
    }

    public String getName(){
        return name;
    }

    public boolean setCount(int count){
        if (isUnderThousand(count)) {
            this.count = count;
            return true;
        }
        return false;
    }

    public int getCount() {
        return count;
    }

    private boolean isUnderThousand(int count) {
        return count <= 1000;
    }
}
