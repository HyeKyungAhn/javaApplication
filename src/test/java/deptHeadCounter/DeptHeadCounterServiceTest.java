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
        boolean resultCorrectInput1 = dhcService.saveDeptPersonnelInfo(deptContainer, processedCorrectInput1);
        boolean resultCorrectInput2 = dhcService.saveDeptPersonnelInfo(deptContainer, processedCorrectInput2);
        boolean resultCorrectInput3 = dhcService.saveDeptPersonnelInfo(deptContainer, processedCorrectInput3);

        boolean resultFaultyInput2 = dhcService.saveDeptPersonnelInfo(deptContainer, processedFaultyInput2);
        boolean resultFaultyInput3 = dhcService.saveDeptPersonnelInfo(deptContainer, processedFaultyInput3);

        //then
        assertTrue(resultCorrectInput1);
        assertTrue(resultCorrectInput2);
        assertTrue(resultCorrectInput3);

        assertNull(processedFaultyInput1); //입력값 검증 단계에서 음수가 걸러짐
        assertFalse(resultFaultyInput2);
        assertFalse(resultFaultyInput3);
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
        boolean resultCorrectInput1 = dhcService.saveDeptPersonnelInfo(deptContainer, processedCorrectInput1);
        int devDeptCount1 = deptContainer.getDeptIfExist("DEV").getCount();
        boolean resultCorrectInput2 = dhcService.saveDeptPersonnelInfo(deptContainer, processedCorrectInput2);
        int devDeptCount2 = deptContainer.getDeptIfExist("DEV").getCount();

        //then
        assertTrue(resultCorrectInput1);
        assertTrue(resultCorrectInput2);
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
        DHCServiceStatus status= dhcService.updateDeptStructure(deptContainer, processedRelation);

        //then
        assertSame(status, DHCServiceStatus.FAIL_DEPT_NOT_EXIST);
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
        boolean saveResultOfDevDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedDevDeptInput);
        boolean saveResultOfDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedHrDeptInput);

        DHCServiceStatus status= dhcService.updateDeptStructure(deptContainer, processedRelation);

        //then
        assertTrue(saveResultOfDevDeptInput);
        assertTrue(saveResultOfDeptInput);
        assertSame(status, DHCServiceStatus.FAIL_PARENT_NOT_ASSIGNED);
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
        boolean saveResultOfAaDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedAaDeptInput);
        boolean saveResultOfBbDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedBbDeptInput);
        boolean saveResultOfCcDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedCcDeptInput);

        DHCServiceStatus statusOfCorrectRelation1 = dhcService.updateDeptStructure(deptContainer, processedCorrectRelation1);
        DHCServiceStatus statusOfCorrectRelation2 = dhcService.updateDeptStructure(deptContainer, processedcorrectRelation2);
        DHCServiceStatus statusOfCorrectRelation3 = dhcService.updateDeptStructure(deptContainer, processedcorrectRelation3);
        DHCServiceStatus statusOfFaultRelation = dhcService.updateDeptStructure(deptContainer, processedfaultRelation);

        //then
        assertTrue(saveResultOfAaDeptInput);
        assertTrue(saveResultOfBbDeptInput);
        assertTrue(saveResultOfCcDeptInput);

        assertSame(statusOfCorrectRelation1, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfCorrectRelation2, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfCorrectRelation3, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfFaultRelation, DHCServiceStatus.FAIL_ALREADY_HAVE_PARENT);
    }

    @Test
    public void testUpdateFailForSelfInclusiveDept(){
        //given
        DepartmentContainer deptContainer = new DepartmentContainer();

        String aaDeptInput = "AA, 10";

        String correctRelation = "* > AA";
        String faultyRelation = "AA > AA";

        String[] processedAaDeptInput = inputProcessor.processInput(aaDeptInput);
        String[] processedCorrectRelation = inputProcessor.processInput(correctRelation);
        String[] processedFaultyRelation = inputProcessor.processInput(faultyRelation);


        //when
        boolean saveResultOfAaDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedAaDeptInput);

        DHCServiceStatus statusOfCorrectRelation = dhcService.updateDeptStructure(deptContainer, processedCorrectRelation);
        DHCServiceStatus statusOfFaultyRelation = dhcService.updateDeptStructure(deptContainer, processedFaultyRelation);

        //then
        assertTrue(saveResultOfAaDeptInput);

        assertSame(statusOfCorrectRelation, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfFaultyRelation, DHCServiceStatus.FAIL_ALREADY_HAVE_PARENT);
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
        boolean saveResultOfAaDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedAaDeptInput);
        boolean saveResultOfBbDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedBbDeptInput);
        boolean saveResultOfCcDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedCcDeptInput);
        boolean saveResultOfDdDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedDdDeptInput);
        boolean saveResultOfEeDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedEeDeptInput);
        boolean saveResultOfFfDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedFfDeptInput);

        DHCServiceStatus statusOfRelation1 = dhcService.updateDeptStructure(deptContainer, processedRelation1);
        DHCServiceStatus statusOfRelation2 = dhcService.updateDeptStructure(deptContainer, processedRelation2);
        DHCServiceStatus statusOfRelation3 = dhcService.updateDeptStructure(deptContainer, processedRelation3);
        DHCServiceStatus statusOfRelation4 = dhcService.updateDeptStructure(deptContainer, processedRelation4);
        DHCServiceStatus statusOfRelation5 = dhcService.updateDeptStructure(deptContainer, processedRelation5);
        DHCServiceStatus statusOfRelation6 = dhcService.updateDeptStructure(deptContainer, processedRelation6);

        //then
        assertTrue(saveResultOfAaDeptInput);
        assertTrue(saveResultOfBbDeptInput);
        assertTrue(saveResultOfCcDeptInput);
        assertTrue(saveResultOfDdDeptInput);
        assertTrue(saveResultOfEeDeptInput);
        assertTrue(saveResultOfFfDeptInput);

        assertSame(statusOfRelation1, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation2, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation3, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation4, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation5, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation6, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);

        Department ddDept = deptContainer.getDeptIfExist("EE").getParent();
        assertEquals("DD", ddDept.getName());
        Department bbDept1 = ddDept.getParent();
        assertEquals("BB", bbDept1.getName());
        Department bbDept2 = deptContainer.getDeptIfExist("CC").getParent();
        assertEquals(bbDept1, bbDept2);
        Department aaDept1 = bbDept1.getParent();
        Department aaDept2 = deptContainer.getDeptIfExist("FF").getParent();
        assertEquals(aaDept1, aaDept2);
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
        boolean saveResultOfAaDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedAaDeptInput);
        boolean saveResultOfBbDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedBbDeptInput);
        boolean saveResultOfCcDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedCcDeptInput);
        boolean saveResultOfDdDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedDdDeptInput);
        boolean saveResultOfEeDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedEeDeptInput);
        boolean saveResultOfFfDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedFfDeptInput);

        DHCServiceStatus statusOfRelation1 = dhcService.updateDeptStructure(deptContainer, processedRelation1);
        DHCServiceStatus statusOfRelation2 = dhcService.updateDeptStructure(deptContainer, processedRelation2);
        DHCServiceStatus statusOfRelation3 = dhcService.updateDeptStructure(deptContainer, processedRelation3);
        DHCServiceStatus statusOfRelation4 = dhcService.updateDeptStructure(deptContainer, processedRelation4);
        DHCServiceStatus statusOfRelation5 = dhcService.updateDeptStructure(deptContainer, processedRelation5);
        DHCServiceStatus statusOfRelation6 = dhcService.updateDeptStructure(deptContainer, processedRelation6);

        //then
        assertTrue(saveResultOfAaDeptInput);
        assertTrue(saveResultOfBbDeptInput);
        assertTrue(saveResultOfCcDeptInput);
        assertTrue(saveResultOfDdDeptInput);
        assertTrue(saveResultOfEeDeptInput);
        assertTrue(saveResultOfFfDeptInput);

        assertSame(statusOfRelation1, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation2, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation3, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation4, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation5, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation6, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);

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
        boolean saveResultOfAaDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedAaDeptInput);

        DHCServiceStatus statusOfRelation1 = dhcService.updateDeptStructure(deptContainer, processedRelation1);

        //then
        assertTrue(saveResultOfAaDeptInput);

        assertSame(statusOfRelation1, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);

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
        boolean saveResultOfAaDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedAaDeptInput);
        boolean saveResultOfBbDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedBbDeptInput);
        boolean saveResultOfCcDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedCcDeptInput);
        boolean saveResultOfDdDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedDdDeptInput);
        boolean saveResultOfEeDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedEeDeptInput);
        boolean saveResultOfFfDeptInput = dhcService.saveDeptPersonnelInfo(deptContainer, processedFfDeptInput);

        DHCServiceStatus statusOfRelation1 = dhcService.updateDeptStructure(deptContainer, processedRelation1);
        DHCServiceStatus statusOfRelation2 = dhcService.updateDeptStructure(deptContainer, processedRelation2);
        DHCServiceStatus statusOfRelation3 = dhcService.updateDeptStructure(deptContainer, processedRelation3);
        DHCServiceStatus statusOfRelation4 = dhcService.updateDeptStructure(deptContainer, processedRelation4);
        DHCServiceStatus statusOfRelation5 = dhcService.updateDeptStructure(deptContainer, processedRelation5);
        DHCServiceStatus statusOfRelation6 = dhcService.updateDeptStructure(deptContainer, processedRelation6);

        //then
        assertTrue(saveResultOfAaDeptInput);
        assertTrue(saveResultOfBbDeptInput);
        assertTrue(saveResultOfCcDeptInput);
        assertTrue(saveResultOfDdDeptInput);
        assertTrue(saveResultOfEeDeptInput);
        assertTrue(saveResultOfFfDeptInput);

        assertSame(statusOfRelation1, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation2, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation3, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation4, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation5, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);
        assertSame(statusOfRelation6, DHCServiceStatus.SAVE_DEPT_COMPOSITION_SUCCESS);

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

        Map<String, Integer> totalCount = dhcService.calculateTopLevelDeptPersonnelSum(deptContainer);

        assertEquals(0,totalCount.size());
    }
}
