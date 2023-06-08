package deptHeadCounter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeptHeadCounterInputProcessorTest {
    DeptHeadCounterInputProcessor inputProcessor;

    @Before
    public void setup(){
        inputProcessor = new DeptHeadCounterInputProcessor();
    }

    @Test
    public void testOnlyUppercaseAlpabetInput(){
        //given
        String faultyInput1 = "123 , 000";
        String faultyInput2 = "123>432";
        String faultyInput3 = "123 < sdf";
        String faultyInput4 = "as < df";

        String correctInput1 = "AS > DF";
        String correctInput2 = "AS < DF";
        String correctInput3 = "AS , 10";

        //when
        String[] faultyResult1 = inputProcessor.processInput(faultyInput1);
        String[] faultyResult2 = inputProcessor.processInput(faultyInput2);
        String[] faultyResult3 = inputProcessor.processInput(faultyInput3);
        String[] faultyResult4 = inputProcessor.processInput(faultyInput4);

        String[] correctResult1 = inputProcessor.processInput(correctInput1);
        String[] correctResult2 = inputProcessor.processInput(correctInput2);
        String[] correctResult3 = inputProcessor.processInput(correctInput3);

        //then
        assertNull(faultyResult1);
        assertNull(faultyResult2);
        assertNull(faultyResult3);
        assertNull(faultyResult4);

        assertEquals(3, correctResult1.length);
        assertEquals(3, correctResult2.length);
        assertEquals(3, correctResult3.length);
    }

    @Test
    public void testOnlyNaturalNumForHeadCount(){
        //given
        String faultyInput1 = "AS , 001";
        String faultyInput2 = "AS , 0sd";
        String faultyInput3 = "AS , 0AD";
        String faultyInput4 = "AS , ";


        String correctInput1 = "AS , 100";


        //when
        String[] faultyResult1 = inputProcessor.processInput(faultyInput1);
        String[] faultyResult2 = inputProcessor.processInput(faultyInput2);
        String[] faultyResult3 = inputProcessor.processInput(faultyInput3);
        String[] faultyResult4 = inputProcessor.processInput(faultyInput4);

        String[] correctResult1 = inputProcessor.processInput(correctInput1);

        //then
        assertNull(faultyResult1);
        assertNull(faultyResult2);
        assertNull(faultyResult3);
        assertNull(faultyResult4);

        assertEquals(3, correctResult1.length);
    }

    @Test
    public void testAvailableInputValue(){
        //given
        String faultyInput1 = "AS ) 100";
        String faultyInput2 = "AS , 10  00";
        String faultyInput3 = "   ";
        String faultyInput4 = "A S , 100";
        String faultyInput5 = "AS * HR";
        String faultyInput6 = "AS <> HR";
        String faultyInput7 = "AS - HR";
        String faultyInput8 = "AS + HR";

        String correctInput1 = "AS , 100";
        String correctInput2 = "AS < HR";
        String correctInput3 = "AS > HR";


        //when
        String[] faultyResult1 = inputProcessor.processInput(faultyInput1);
        String[] faultyResult2 = inputProcessor.processInput(faultyInput2);
        String[] faultyResult3 = inputProcessor.processInput(faultyInput3);
        String[] faultyResult4 = inputProcessor.processInput(faultyInput4);
        String[] faultyResult5 = inputProcessor.processInput(faultyInput5);
        String[] faultyResult6 = inputProcessor.processInput(faultyInput6);
        String[] faultyResult7 = inputProcessor.processInput(faultyInput7);
        String[] faultyResult8 = inputProcessor.processInput(faultyInput8);

        String[] correctResult1 = inputProcessor.processInput(correctInput1);
        String[] correctResult2 = inputProcessor.processInput(correctInput2);
        String[] correctResult3 = inputProcessor.processInput(correctInput3);

        //then
        assertNull(faultyResult1);
        assertNull(faultyResult2);
        assertNull(faultyResult3);
        assertNull(faultyResult4);
        assertNull(faultyResult5);
        assertNull(faultyResult6);
        assertNull(faultyResult7);
        assertNull(faultyResult8);

        assertEquals(3, correctResult1.length);
        assertEquals(3, correctResult2.length);
        assertEquals(3, correctResult3.length);
    }

    @Test
    public void testInputFormForSavingPersonnelInfo(){
        //given
        String faultyInput1 = "AS < 100";
        String faultyInput2 = "AS , 10  00";
        String faultyInput3 = "AS + 90";
        String faultyInput4 = "A S , 100";
        String faultyInput5 = "AS , HR";
        String faultyInput6 = "AS <         ";
        String faultyInput7 = "AS - 90";

        String correctInput1 = "AS , 10";

        //when
        String[] faultyResult1 = inputProcessor.processInput(faultyInput1);
        String[] faultyResult2 = inputProcessor.processInput(faultyInput2);
        String[] faultyResult3 = inputProcessor.processInput(faultyInput3);
        String[] faultyResult4 = inputProcessor.processInput(faultyInput4);
        String[] faultyResult5 = inputProcessor.processInput(faultyInput5);
        String[] faultyResult6 = inputProcessor.processInput(faultyInput6);
        String[] faultyResult7 = inputProcessor.processInput(faultyInput7);


        String[] correctResult1 = inputProcessor.processInput(correctInput1);

        //then
        assertNull(faultyResult1);
        assertNull(faultyResult2);
        assertNull(faultyResult3);
        assertNull(faultyResult4);
        assertNull(faultyResult5);
        assertNull(faultyResult6);
        assertNull(faultyResult7);

        assertEquals(3, correctResult1.length);
    }

    @Test
    public void testInputFormForUpdatingDeptStructure(){
        //given
        String faultyInput1 = "AS < 100";
        String faultyInput2 = "AS , HR";
        String faultyInput3 = "AS <> HR";
        String faultyInput4 = "A S * HR";
        String faultyInput5 = "AS < 10";
        String faultyInput6 = "AS > 10";
        String faultyInput7 = "AS > ";
        String faultyInput8 = "< HR";


        String correctInput1 = "AS < HR";
        String correctInput2 = "AS > HR";

        //when
        String[] faultyResult1 = inputProcessor.processInput(faultyInput1);
        String[] faultyResult2 = inputProcessor.processInput(faultyInput2);
        String[] faultyResult3 = inputProcessor.processInput(faultyInput3);
        String[] faultyResult4 = inputProcessor.processInput(faultyInput4);
        String[] faultyResult5 = inputProcessor.processInput(faultyInput5);
        String[] faultyResult6 = inputProcessor.processInput(faultyInput6);
        String[] faultyResult7 = inputProcessor.processInput(faultyInput7);
        String[] faultyResult8 = inputProcessor.processInput(faultyInput8);


        String[] correctResult1 = inputProcessor.processInput(correctInput1);
        String[] correctResult2 = inputProcessor.processInput(correctInput2);

        //then
        assertNull(faultyResult1);
        assertNull(faultyResult2);
        assertNull(faultyResult3);
        assertNull(faultyResult4);
        assertNull(faultyResult5);
        assertNull(faultyResult6);
        assertNull(faultyResult7);
        assertNull(faultyResult8);

        assertEquals(3, correctResult1.length);
        assertEquals(3, correctResult2.length);
    }

    @Test
    public void testOutputFormForPrintTotalHeadcount(){
        //given
        String faultyInput1 = "AS < 100";
        String faultyInput2 = "AS , HR";
        String faultyInput3 = "AS <> HR";
        String faultyInput4 = "A S * HR";
        String faultyInput5 = "AS < 10";
        String faultyInput6 = "AS > 10";
        String faultyInput7 = "AS > ";
        String faultyInput8 = "< HR";


        String correctInput1 = "AS < HR";
        String correctInput2 = "AS > HR";

        //when
        String[] faultyResult1 = inputProcessor.processInput(faultyInput1);
        String[] faultyResult2 = inputProcessor.processInput(faultyInput2);
        String[] faultyResult3 = inputProcessor.processInput(faultyInput3);
        String[] faultyResult4 = inputProcessor.processInput(faultyInput4);
        String[] faultyResult5 = inputProcessor.processInput(faultyInput5);
        String[] faultyResult6 = inputProcessor.processInput(faultyInput6);
        String[] faultyResult7 = inputProcessor.processInput(faultyInput7);
        String[] faultyResult8 = inputProcessor.processInput(faultyInput8);


        String[] correctResult1 = inputProcessor.processInput(correctInput1);
        String[] correctResult2 = inputProcessor.processInput(correctInput2);

        //then
        assertNull(faultyResult1);
        assertNull(faultyResult2);
        assertNull(faultyResult3);
        assertNull(faultyResult4);
        assertNull(faultyResult5);
        assertNull(faultyResult6);
        assertNull(faultyResult7);
        assertNull(faultyResult8);

        assertEquals(3, correctResult1.length);
        assertEquals(3, correctResult2.length);
    }
}