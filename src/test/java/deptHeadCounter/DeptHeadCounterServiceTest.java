package deptHeadCounter;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class DeptHeadCounterServiceTest extends TestCase {
    DepartmentContainer deptContainer;
    DeptHeadCounterService dhcService;

    @Before
    public void setup(){
        deptContainer = new DepartmentContainer();
        dhcService = new DeptHeadCounterService();
    }


    @Test
    public void testHeadcountInfoPresence(){

    }


}