package deptHeadCounter;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class DeptHeadCounterServiceTest {
    DepartmentContainer deptContainer;
    DeptHeadCounterService dhcService;
    DeptHeadCounterInputProcessor inputProcessor;

    @Before
    public void setup(){
        deptContainer = new DepartmentContainer();
        dhcService = new DeptHeadCounterService();
        inputProcessor = new DeptHeadCounterInputProcessor();
    }

    private DHCOutputStatus getResultFromSavePersonnelInfo(String[] input){
        return (DHCOutputStatus) dhcService.saveDeptPersonnelInfo(deptContainer, input).get("saveResult");
    }

    private DHCOutputStatus getResultFromUpdateDeptStructure(String[] input){
        return (DHCOutputStatus) dhcService.updateDeptStructure(deptContainer, input).get("updateResult");
    }
    @Test
    public void testHeadcountRangeBetweenZeroToThousand(){
        //given
        deptContainer.init();

        String correctInput1 = "DEV, 0";
        String correctInput2 = "DEV, 50";
        String correctInput3 = "DEV, 900";

        String faultyInput1 = "DEV, -1";
        String faultyInput2 = "DEV, 5000000000";
        String faultyInput3 = "DEV, 1001";

        String[] processedCorrectInput1 = inputProcessor.processInput(correctInput1);
        String[] processedCorrectInput2 = inputProcessor.processInput(correctInput2);
        String[] processedCorrectInput3 = inputProcessor.processInput(correctInput3);

        String[] processedFaultyInput1 = inputProcessor.processInput(faultyInput1);
        String[] processedFaultyInput2 = inputProcessor.processInput(faultyInput2);
        String[] processedFaultyInput3 = inputProcessor.processInput(faultyInput3);

        //when
        DHCOutputStatus resultCorrectInput1 = getResultFromSavePersonnelInfo(processedCorrectInput1);
        DHCOutputStatus resultCorrectInput2 = getResultFromSavePersonnelInfo(processedCorrectInput2);
        DHCOutputStatus resultCorrectInput3 = getResultFromSavePersonnelInfo(processedCorrectInput3);

        DHCOutputStatus resultFaultyInput2 = getResultFromSavePersonnelInfo(processedFaultyInput2);
        DHCOutputStatus resultFaultyInput3 = getResultFromSavePersonnelInfo(processedFaultyInput3);

        //then
        assertEquals(resultCorrectInput1, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(resultCorrectInput2, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(resultCorrectInput3, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);

        assertNull(processedFaultyInput1); //입력값 검증 단계에서 음수가 걸러짐
        assertEquals(resultFaultyInput2, DHCOutputStatus.SAVE_PERSONNEL_INFO_FAIL);
        assertEquals(resultFaultyInput3, DHCOutputStatus.SAVE_PERSONNEL_INFO_FAIL);
    }
    


    @Test
    public void testUpdateCountForExistDeptInStructure(){
        //given
        deptContainer.init();
        String correctInput1 = "DEV, 10";
        String correctInput2 = "DEV, 50";

        String[] processedCorrectInput1 = inputProcessor.processInput(correctInput1);
        String[] processedCorrectInput2 = inputProcessor.processInput(correctInput2);

        //when
        DHCOutputStatus resultCorrectInput1 = getResultFromSavePersonnelInfo(processedCorrectInput1);
        int devDeptCount1 = deptContainer.getDeptIfExist("DEV").getCount();

        DHCOutputStatus resultCorrectInput2 = getResultFromSavePersonnelInfo(processedCorrectInput2);
        int devDeptCount2 = deptContainer.getDeptIfExist("DEV").getCount();

        //then
        assertEquals(resultCorrectInput1, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(resultCorrectInput2, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(10, devDeptCount1);
        assertEquals(50, devDeptCount2);
    }


    @Test
    public void testUpdateFailsForUnsavedDeptInfo(){
        //given
        deptContainer.init();

        String relation = "AA > BB";
        String[] processedRelation = inputProcessor.processInput(relation);

        //when
        DHCOutputStatus status = getResultFromUpdateDeptStructure(processedRelation);

        //then
        assertSame(status, DHCOutputStatus.UPDATE_FAIL_DEPT_NOT_EXIST);
    }

    @Test
    public void testUpdateFailsForUnassignedParentDept(){
        //given
        deptContainer.init();

        String devDeptInput = "DEV, 100";
        String hrDeptInput = "HR, 50";
        String relation = "DEV > HR";

        String[] processedDevDeptInput = inputProcessor.processInput(devDeptInput);
        String[] processedHrDeptInput = inputProcessor.processInput(hrDeptInput);
        String[] processedRelation = inputProcessor.processInput(relation);

        //when
        DHCOutputStatus saveResultOfDevDeptInput = getResultFromSavePersonnelInfo(processedDevDeptInput);
        DHCOutputStatus saveResultOfHrDeptInput = getResultFromSavePersonnelInfo(processedHrDeptInput);

        DHCOutputStatus status= getResultFromUpdateDeptStructure(processedRelation);

        //then
        assertEquals(saveResultOfDevDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfHrDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertSame(status, DHCOutputStatus.UPDATE_FAIL_PARENT_NOT_ASSIGNED);
    }

    @Test
    public void testOnlySingleParentAvailable(){
        //given
        deptContainer.init();

        String aaDeptInput = "AA, 10";
        String bbDeptInput = "BB, 50";
        String ccDeptInput = "CC, 100";

        String correctRelation1 = "* > AA";
        String correctRelation2 = "AA > BB";
        String correctRelation3 = "* > CC";
        String faultRelation = "CC > BB";

        String[] processedAaDeptInput = inputProcessor.processInput(aaDeptInput);
        String[] processedBbDeptInput = inputProcessor.processInput(bbDeptInput);
        String[] processedCcDeptInput = inputProcessor.processInput(ccDeptInput);

        String[] processedCorrectRelation1 = inputProcessor.processInput(correctRelation1);
        String[] processedcorrectRelation2 = inputProcessor.processInput(correctRelation2);
        String[] processedcorrectRelation3 = inputProcessor.processInput(correctRelation3);
        String[] processedfaultRelation = inputProcessor.processInput(faultRelation);

        //when
        DHCOutputStatus saveResultOfAaDeptInput = getResultFromSavePersonnelInfo(processedAaDeptInput);
        DHCOutputStatus saveResultOfBbDeptInput = getResultFromSavePersonnelInfo(processedBbDeptInput);
        DHCOutputStatus saveResultOfCcDeptInput = getResultFromSavePersonnelInfo(processedCcDeptInput);

        DHCOutputStatus statusOfCorrectRelation1 = getResultFromUpdateDeptStructure(processedCorrectRelation1);
        DHCOutputStatus statusOfCorrectRelation2 = getResultFromUpdateDeptStructure(processedcorrectRelation2);
        DHCOutputStatus statusOfCorrectRelation3 = getResultFromUpdateDeptStructure(processedcorrectRelation3);
        DHCOutputStatus statusOfFaultRelation = getResultFromUpdateDeptStructure(processedfaultRelation);

        //then
        assertEquals(saveResultOfAaDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfBbDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfCcDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);

        assertSame(statusOfCorrectRelation1, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfCorrectRelation2, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfCorrectRelation3, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfFaultRelation, DHCOutputStatus.UPDATE_FAIL_ALREADY_HAVE_PARENT);
    }

    @Test
    public void testUpdateFailForSelfInclusiveDept(){
        //given
        deptContainer.init();

        String aaDeptInput = "AA, 10";

        String correctRelation = "* > AA";
        String faultyRelation = "AA > AA";

        String[] processedAaDeptInput = inputProcessor.processInput(aaDeptInput);
        String[] processedCorrectRelation = inputProcessor.processInput(correctRelation);
        String[] processedFaultyRelation = inputProcessor.processInput(faultyRelation);


        //when
        DHCOutputStatus saveResultOfAaDeptInput = getResultFromSavePersonnelInfo(processedAaDeptInput);

        DHCOutputStatus statusOfCorrectRelation = getResultFromUpdateDeptStructure(processedCorrectRelation);
        DHCOutputStatus statusOfFaultyRelation = getResultFromUpdateDeptStructure(processedFaultyRelation);

        //then
        assertEquals(saveResultOfAaDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);

        assertSame(statusOfCorrectRelation, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfFaultyRelation, DHCOutputStatus.UPDATE_FAIL_CANNOT_ADD_ITSELF_AS_CHILD);
    }


    @Test
    public void testDepartmentHierarchyCorrectness(){
        //given
        deptContainer.init();
        //     AA
        //     |   \
        //     BB  FF
        //     | \
        //     CC DD
        //        |
        //        EE

        String aaDeptInput = "AA, 10";
        String bbDeptInput = "BB, 50";
        String ccDeptInput = "CC, 100";
        String ddDeptInput = "DD, 100";
        String eeDeptInput = "EE, 100";
        String ffDeptInput = "FF, 100";

        String relation1 = "* > AA";
        String relation2 = "AA > BB";
        String relation3 = "BB > CC";
        String relation4 = "BB > DD";
        String relation5 = "DD > EE";
        String relation6 = "FF < AA";


        String[] processedAaDeptInput = inputProcessor.processInput(aaDeptInput);
        String[] processedBbDeptInput = inputProcessor.processInput(bbDeptInput);
        String[] processedCcDeptInput = inputProcessor.processInput(ccDeptInput);
        String[] processedDdDeptInput = inputProcessor.processInput(ddDeptInput);
        String[] processedEeDeptInput = inputProcessor.processInput(eeDeptInput);
        String[] processedFfDeptInput = inputProcessor.processInput(ffDeptInput);

        String[] processedRelation1 = inputProcessor.processInput(relation1);
        String[] processedRelation2 = inputProcessor.processInput(relation2);
        String[] processedRelation3 = inputProcessor.processInput(relation3);
        String[] processedRelation4 = inputProcessor.processInput(relation4);
        String[] processedRelation5 = inputProcessor.processInput(relation5);
        String[] processedRelation6 = inputProcessor.processInput(relation6);


        //when
        DHCOutputStatus saveResultOfAaDeptInput = getResultFromSavePersonnelInfo(processedAaDeptInput);
        DHCOutputStatus saveResultOfBbDeptInput = getResultFromSavePersonnelInfo(processedBbDeptInput);
        DHCOutputStatus saveResultOfCcDeptInput = getResultFromSavePersonnelInfo(processedCcDeptInput);
        DHCOutputStatus saveResultOfDdDeptInput = getResultFromSavePersonnelInfo(processedDdDeptInput);
        DHCOutputStatus saveResultOfEeDeptInput = getResultFromSavePersonnelInfo(processedEeDeptInput);
        DHCOutputStatus saveResultOfFfDeptInput = getResultFromSavePersonnelInfo(processedFfDeptInput);

        DHCOutputStatus statusOfRelation1 = getResultFromUpdateDeptStructure(processedRelation1);
        DHCOutputStatus statusOfRelation2 = getResultFromUpdateDeptStructure(processedRelation2);
        DHCOutputStatus statusOfRelation3 = getResultFromUpdateDeptStructure(processedRelation3);
        DHCOutputStatus statusOfRelation4 = getResultFromUpdateDeptStructure(processedRelation4);
        DHCOutputStatus statusOfRelation5 = getResultFromUpdateDeptStructure(processedRelation5);
        DHCOutputStatus statusOfRelation6 = getResultFromUpdateDeptStructure(processedRelation6);

        //then
        assertEquals(saveResultOfAaDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfBbDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfCcDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfDdDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfEeDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfFfDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);

        assertSame(statusOfRelation1, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation2, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation3, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation4, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation5, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation6, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);

        Department aaDept = deptContainer.getDeptIfExist("AA");
        Department bbDept = deptContainer.getDeptIfExist("BB");
        Department ccDept = deptContainer.getDeptIfExist("CC");
        Department ddDept = deptContainer.getDeptIfExist("DD");
        Department eeDept = deptContainer.getDeptIfExist("EE");
        Department ffDept = deptContainer.getDeptIfExist("FF");

        assertNotNull(aaDept);
        assertEquals(2, aaDept.getChildren().size());
        assertTrue(aaDept.getChildren().contains(bbDept));
        assertTrue(aaDept.getChildren().contains(ffDept));

        assertNotNull(bbDept);
        assertEquals(2, bbDept.getChildren().size());
        assertTrue(bbDept.getChildren().contains(ccDept));
        assertTrue(bbDept.getChildren().contains(ddDept));

        assertNotNull(ddDept);
        assertEquals(1, ddDept.getChildren().size());
        assertTrue(ddDept.getChildren().contains(eeDept));
    }

    @Test
    public void testTotalHeadcountInTopLevelDepts(){
        //given
        deptContainer.init();
        //     AA
        //     |   \
        //     BB  FF
        //     | \
        //     CC DD
        //        |
        //        EE

        String aaDeptInput = "AA, 10";
        String bbDeptInput = "BB, 50";
        String ccDeptInput = "CC, 100";
        String ddDeptInput = "DD, 100";
        String eeDeptInput = "EE, 100";
        String ffDeptInput = "FF, 100";

        String relation1 = "* > AA";
        String relation2 = "AA > BB";
        String relation3 = "BB > CC";
        String relation4 = "BB > DD";
        String relation5 = "DD > EE";
        String relation6 = "FF < AA";


        String[] processedAaDeptInput = inputProcessor.processInput(aaDeptInput);
        String[] processedBbDeptInput = inputProcessor.processInput(bbDeptInput);
        String[] processedCcDeptInput = inputProcessor.processInput(ccDeptInput);
        String[] processedDdDeptInput = inputProcessor.processInput(ddDeptInput);
        String[] processedEeDeptInput = inputProcessor.processInput(eeDeptInput);
        String[] processedFfDeptInput = inputProcessor.processInput(ffDeptInput);

        String[] processedRelation1 = inputProcessor.processInput(relation1);
        String[] processedRelation2 = inputProcessor.processInput(relation2);
        String[] processedRelation3 = inputProcessor.processInput(relation3);
        String[] processedRelation4 = inputProcessor.processInput(relation4);
        String[] processedRelation5 = inputProcessor.processInput(relation5);
        String[] processedRelation6 = inputProcessor.processInput(relation6);


        //when
        DHCOutputStatus saveResultOfAaDeptInput = getResultFromSavePersonnelInfo(processedAaDeptInput);
        DHCOutputStatus saveResultOfBbDeptInput = getResultFromSavePersonnelInfo(processedBbDeptInput);
        DHCOutputStatus saveResultOfCcDeptInput = getResultFromSavePersonnelInfo(processedCcDeptInput);
        DHCOutputStatus saveResultOfDdDeptInput = getResultFromSavePersonnelInfo(processedDdDeptInput);
        DHCOutputStatus saveResultOfEeDeptInput = getResultFromSavePersonnelInfo(processedEeDeptInput);
        DHCOutputStatus saveResultOfFfDeptInput = getResultFromSavePersonnelInfo(processedFfDeptInput);

        DHCOutputStatus statusOfRelation1 = getResultFromUpdateDeptStructure(processedRelation1);
        DHCOutputStatus statusOfRelation2 = getResultFromUpdateDeptStructure(processedRelation2);
        DHCOutputStatus statusOfRelation3 = getResultFromUpdateDeptStructure(processedRelation3);
        DHCOutputStatus statusOfRelation4 = getResultFromUpdateDeptStructure(processedRelation4);
        DHCOutputStatus statusOfRelation5 = getResultFromUpdateDeptStructure(processedRelation5);
        DHCOutputStatus statusOfRelation6 = getResultFromUpdateDeptStructure(processedRelation6);

        //then
        assertEquals(saveResultOfAaDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfBbDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfCcDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfDdDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfEeDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfFfDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);

        assertSame(statusOfRelation1, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation2, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation3, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation4, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation5, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation6, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);

        Map<String, Integer> totalCount = dhcService.calculateTopLevelDeptPersonnelSum(deptContainer);

        int expexcedTotalCount = deptContainer.getDeptIfExist("AA").getCount()
                + deptContainer.getDeptIfExist("BB").getCount()
                + deptContainer.getDeptIfExist("CC").getCount()
                + deptContainer.getDeptIfExist("DD").getCount()
                + deptContainer.getDeptIfExist("EE").getCount()
                + deptContainer.getDeptIfExist("FF").getCount();

        assertEquals((int)totalCount.get("AA"), expexcedTotalCount);
    }

    @Test
    public void testTotalHeadcountForOnlyOneTopLevelDept(){
        //given
        deptContainer.init();

        String aaDeptInput = "AA, 10";

        String relation1 = "* > AA";

        String[] processedAaDeptInput = inputProcessor.processInput(aaDeptInput);

        String[] processedRelation1 = inputProcessor.processInput(relation1);

        //when
        DHCOutputStatus saveResultOfAaDeptInput = getResultFromSavePersonnelInfo(processedAaDeptInput);

        DHCOutputStatus statusOfRelation1 = getResultFromUpdateDeptStructure(processedRelation1);

        //then
        assertEquals(saveResultOfAaDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);

        assertSame(statusOfRelation1, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);

        Map<String, Integer> totalCount = dhcService.calculateTopLevelDeptPersonnelSum(deptContainer);

        int expexcedTotalCount = deptContainer.getDeptIfExist("AA").getCount();

        assertEquals((int)totalCount.get("AA"), expexcedTotalCount);
    }

    @Test
    public void testNoOverlapInTotalHeadcountByTopLevelDepts(){
        //given
        deptContainer.init();
        //         *
        //      / |  \
        //     AA DD FF
        //     |  |
        //    BB  EE
        //    |
        //   CC

        String aaDeptInput = "AA, 10";
        String bbDeptInput = "BB, 50";
        String ccDeptInput = "CC, 100";
        String ddDeptInput = "DD, 100";
        String eeDeptInput = "EE, 100";
        String ffDeptInput = "FF, 100";


        String relation1 = "* > AA";
        String relation2 = "AA > BB";
        String relation3 = "BB > CC";
        String relation4 = "* > DD";
        String relation5 = "DD > EE";
        String relation6 = "FF < *";


        String[] processedAaDeptInput = inputProcessor.processInput(aaDeptInput);
        String[] processedBbDeptInput = inputProcessor.processInput(bbDeptInput);
        String[] processedCcDeptInput = inputProcessor.processInput(ccDeptInput);
        String[] processedDdDeptInput = inputProcessor.processInput(ddDeptInput);
        String[] processedEeDeptInput = inputProcessor.processInput(eeDeptInput);
        String[] processedFfDeptInput = inputProcessor.processInput(ffDeptInput);

        String[] processedRelation1 = inputProcessor.processInput(relation1);
        String[] processedRelation2 = inputProcessor.processInput(relation2);
        String[] processedRelation3 = inputProcessor.processInput(relation3);
        String[] processedRelation4 = inputProcessor.processInput(relation4);
        String[] processedRelation5 = inputProcessor.processInput(relation5);
        String[] processedRelation6 = inputProcessor.processInput(relation6);

        //when
        DHCOutputStatus saveResultOfAaDeptInput = getResultFromSavePersonnelInfo(processedAaDeptInput);
        DHCOutputStatus saveResultOfBbDeptInput = getResultFromSavePersonnelInfo(processedBbDeptInput);
        DHCOutputStatus saveResultOfCcDeptInput = getResultFromSavePersonnelInfo(processedCcDeptInput);
        DHCOutputStatus saveResultOfDdDeptInput = getResultFromSavePersonnelInfo(processedDdDeptInput);
        DHCOutputStatus saveResultOfEeDeptInput = getResultFromSavePersonnelInfo(processedEeDeptInput);
        DHCOutputStatus saveResultOfFfDeptInput = getResultFromSavePersonnelInfo(processedFfDeptInput);

        DHCOutputStatus statusOfRelation1 = getResultFromUpdateDeptStructure(processedRelation1);
        DHCOutputStatus statusOfRelation2 = getResultFromUpdateDeptStructure(processedRelation2);
        DHCOutputStatus statusOfRelation3 = getResultFromUpdateDeptStructure(processedRelation3);
        DHCOutputStatus statusOfRelation4 = getResultFromUpdateDeptStructure(processedRelation4);
        DHCOutputStatus statusOfRelation5 = getResultFromUpdateDeptStructure(processedRelation5);
        DHCOutputStatus statusOfRelation6 = getResultFromUpdateDeptStructure(processedRelation6);

        //then
        assertEquals(saveResultOfAaDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfBbDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfCcDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfDdDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfEeDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);
        assertEquals(saveResultOfFfDeptInput, DHCOutputStatus.SAVE_PERSONNEL_INFO_SUCCESS);

        assertSame(statusOfRelation1, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation2, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation3, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation4, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation5, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation6, DHCOutputStatus.UPDATE_DEPT_COMPOSITION_SUCCESS);

        Map<String, Integer> totalCount = dhcService.calculateTopLevelDeptPersonnelSum(deptContainer);

        int expexcedAATotalCount = deptContainer.getDeptIfExist("AA").getCount()
                + deptContainer.getDeptIfExist("BB").getCount()
                + deptContainer.getDeptIfExist("CC").getCount();

        int expexcedDDTotalCount = deptContainer.getDeptIfExist("DD").getCount()
                + deptContainer.getDeptIfExist("EE").getCount();

        int expexcedFFTotalCount = deptContainer.getDeptIfExist("FF").getCount();

        assertEquals((int)totalCount.get("AA"), expexcedAATotalCount);
        assertEquals((int)totalCount.get("DD"), expexcedDDTotalCount);
        assertEquals((int)totalCount.get("FF"), expexcedFFTotalCount);
    }

    @Test
    public void testNullIfNoDeptInStructure(){
        //given
        deptContainer.init();

        //when
        Map<String, Integer> totalCount = dhcService.calculateTopLevelDeptPersonnelSum(deptContainer);

        //then
        assertEquals(0,totalCount.size());
    }
    
}
