package deptHeadCounter;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private int count;
    private Department parent;
    private List<Department> children = new ArrayList<>();

    Department(){}

    public Department(String name, int count){
        this.name = name;
        this.count = count;
    }

    public void addChild(Department dept) {
        if (dept == this) {
            throw new IllegalArgumentException("Cannot add a node as its own child.");
        }

        dept.setParent(this);
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

    private void setParent(Department department) {
        this.parent = department;
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

    public Department getParent(){
        return parent;
    }

    private boolean isUnderThousand(int count) {
        return count <= 1000;
    }
}
