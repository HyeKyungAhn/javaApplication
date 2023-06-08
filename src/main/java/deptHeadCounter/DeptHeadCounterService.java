package deptHeadCounter;

import java.util.HashMap;
import java.util.Map;

public class DeptHeadCounterService {
    public static final String NODE_NAME_STORE_UNASSIGNED_DEPT = "unassigned";

    public boolean saveDeptPersonnelInfo(DepartmentContainer container, String[] input){
        String deptName = input[0];
        int count = Integer.parseInt(input[2]);

        Department savedDeptInfo = container.getDeptIfExist(deptName);

        if(savedDeptInfo != null){
            return savedDeptInfo.setCount(count);
        }

        Department dept = container.getDeptIfExist(NODE_NAME_STORE_UNASSIGNED_DEPT);
        return dept.addChild(deptName, count);
    }

    public DHCServiceStatus updateDeptStructure(DepartmentContainer container, String[] inputs){
        String operation = inputs[1];
        String parentName = operation.equals("<") ? inputs[2] : inputs[0];
        String childName = operation.equals("<") ? inputs[0] : inputs[2];

        Department parentDept = container.getDeptIfExist(parentName);
        Department childDept = container.getDeptIfExist(childName);

        if (parentDept == null || childDept == null) {
            return DHCServiceStatus.FAIL_DEPT_NOT_EXIST; //먼저 부서 입력해달라 부서 정보가 없다.
        }

        if (!parentDept.getName().equals("*")
                && (parentDept.getParent() == null || isUnassignedDept(parentDept.getParent()))) {
            return DHCServiceStatus.FAIL_PARENT_NOT_ASSIGNED; //상위부서가 트리구조 내에 있어야한다.
        }


        if (childDept.getParent() != null && !isUnassignedDept(childDept.getParent())) {
            return DHCServiceStatus.FAIL_ALREADY_HAVE_PARENT; //이미 상위부서가 있다.
        }

        childDept.getParent().getChildren().remove(childDept); //unassiged에서 자기자신 지우기
        parentDept.addChild(childDept);

        return DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS;
    }

    public Map<String, Integer> calculateTopLevelDeptPersonnelSum(DepartmentContainer container){
        Map<String, Integer> result = new HashMap<>();
        for (Department highestDept : container.getRoot().getChildren()) {

            if(isUnassignedDept(highestDept)) continue;

            result.put(highestDept.getName(), container.getTotalHeadCount(highestDept));
        }
        return result;
    }

    private boolean isUnassignedDept(Department dept) {
        return dept.getName().equals(NODE_NAME_STORE_UNASSIGNED_DEPT);
    }
}
