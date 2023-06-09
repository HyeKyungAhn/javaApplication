package deptHeadCounter;

import java.util.HashMap;
import java.util.Map;

public class DeptHeadCounterService {
    public static final String NODE_NAME_STORE_UNASSIGNED_DEPT = "unassigned";

    public Map<String, Object> saveDeptPersonnelInfo(DepartmentContainer container, String[] input){
        Map<String, Object> saveResult = new HashMap<>();
        String deptName;
        int count;

        try {
            deptName = input[0];
            count = Integer.parseInt(input[2]);
        } catch (NumberFormatException | NullPointerException e) {
            saveResult.put("saveResult", DHCOutputStatus.SAVE_PERSONNEL_INFO_FAIL);
            return saveResult;
        }

        Department savedDeptInfo = container.getDeptIfExist(deptName);

        if(savedDeptInfo != null){
            saveResult.put("saveResult", getResultStatus(savedDeptInfo.setCount(count)));
            return saveResult;
        }

        Department dept = container.getDeptIfExist(NODE_NAME_STORE_UNASSIGNED_DEPT);
        saveResult.put("saveResult", getResultStatus(dept.addChild(deptName, count)));

        return saveResult;
    }

    public Map<String, Object> updateDeptStructure(DepartmentContainer container, String[] inputs){
        Map<String, Object> updateResult = new HashMap<>();
        String operation = inputs[1];
        String parentName = operation.equals("<") ? inputs[2] : inputs[0];
        String childName = operation.equals("<") ? inputs[0] : inputs[2];

        Department parentDept = container.getDeptIfExist(parentName);
        Department childDept = container.getDeptIfExist(childName);

        if (parentDept == null || childDept == null) {
            updateResult.put("updateResult", DHCOutputStatus.UPDATE_FAIL_DEPT_NOT_EXIST);
            return updateResult;
        }

        if (!parentDept.getName().equals("*")
                && (parentDept.getParent() == null || isUnassignedDept(parentDept.getParent()))) {
            updateResult.put("updateResult", DHCOutputStatus.UPDATE_FAIL_PARENT_NOT_ASSIGNED);
            return updateResult;
        }


        if (childDept.getParent() != null && !isUnassignedDept(childDept.getParent())) {
            if (childDept.getParent().getName().equals(childName)) {
                updateResult.put("updateResult", DHCOutputStatus.UPDATE_FAIL_CANNOT_ADD_ITSELF_AS_CHILD);
            }
            updateResult.put("updateResult", DHCOutputStatus.UPDATE_FAIL_ALREADY_HAVE_PARENT);
            return updateResult;
        }

        childDept.getParent().getChildren().remove(childDept); //unassiged에서 자기자신 지우기
        parentDept.addChild(childDept);
        updateResult.put("updateResult", DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);

        return updateResult;
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

    private DHCOutputStatus getResultStatus(boolean isSuccess) {
        if (isSuccess) {
            return DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS;
        } else {
            return DHCOutputStatus.SAVE_PERSONNEL_INFO_FAIL;
        }
    }
}
