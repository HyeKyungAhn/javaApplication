package deptHeadCounter;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

public class DepartmentContainer {
    private Department root;

    public static final String NODE_NAME_STORE_UNASSIGNED_DEPT = "unassigned";

    DepartmentContainer(){
        init();
    }

    public void init(){
        root = new Department("*", 0);
        root.addChild("unassigned", 0);
    }

    public Department getRoot(){
        return root;
    }

    public Department getDeptIfExist(String deptName) {
        Queue<Department> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            Department currentDept = queue.poll();

            deptName = !deptName.equals(NODE_NAME_STORE_UNASSIGNED_DEPT) ?
                    deptName.toUpperCase(Locale.ROOT) : deptName;

            if (currentDept.getName().equals(deptName)) {
                return currentDept;
            }

            queue.addAll(currentDept.getChildren());
        }

        return null;
    }

    public int getTotalHeadCount(Department highestDept){
        Queue<Department> queue = new LinkedList<>();
        int totalCount = 0;
        queue.offer(highestDept);

        while (!queue.isEmpty()) {
            Department currentDept = queue.poll();

            totalCount += currentDept.getCount();
            queue.addAll(currentDept.getChildren());
        }

        return totalCount;
    }
}
