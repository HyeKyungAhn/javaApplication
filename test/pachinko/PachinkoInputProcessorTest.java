package pachinko;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class PachinkoInputProcessorTest {
    PachinkoInputProcessor inputProcessor = new PachinkoInputProcessor();

    @Test
    public void inputStringRegexTest(){
        //given
        String faultyInput1 = "mone^";
        String faultyInput2 = "Draw ++++";
        String faultyInput3 = "deposit 00$";

        String correctInput1 = "money";
        String correctInput2 = "draw 12";
        String correctInput3 = "deposit 10000";

        //when
        String[] resultOfFaultyInput1 = inputProcessor.processInput(faultyInput1);
        String[] resultOfFaultyInput2 = inputProcessor.processInput(faultyInput2);
        String[] resultOfFaultyInput3 = inputProcessor.processInput(faultyInput3);

        String[] resultOfCorrectInput1 = inputProcessor.processInput(correctInput1);
        String[] resultOfCorrectInput2 = inputProcessor.processInput(correctInput2);
        String[] resultOfCorrectInput3 = inputProcessor.processInput(correctInput3);
        //then
        assertNull(resultOfFaultyInput1);
        assertNull(resultOfFaultyInput2);
        assertNull(resultOfFaultyInput3);

        assertEquals(1, resultOfCorrectInput1.length); // money (array length = 1)
        assertEquals(2, resultOfCorrectInput2.length); // draw 12 (array length = 2)
        assertEquals(2, resultOfCorrectInput3.length); // charge 10000 (array length = 2)

    }

    @Test
    public void testInputLengthLimit(){
        //given
        String faultyInput1 = "                                        ";
        String faultyInput2 = "money                     22222";
        String faultyInput3 = "deposit 12222222222222222222222222222222";

        String correctInput1 = "money";
        String correctInput2 = "draw 12";
        String correctInput3 = "deposit 10000";
        //when
        String[] resultOfFaultyInput1 = inputProcessor.processInput(faultyInput1);
        String[] resultOfFaultyInput2 = inputProcessor.processInput(faultyInput2);
        String[] resultOfFaultyInput3 = inputProcessor.processInput(faultyInput3);

        String[] resultOfCorrectInput1 = inputProcessor.processInput(correctInput1);
        String[] resultOfCorrectInput2 = inputProcessor.processInput(correctInput2);
        String[] resultOfCorrectInput3 = inputProcessor.processInput(correctInput3);

        //then
        assertNull(resultOfFaultyInput1);
        assertNull(resultOfFaultyInput2);
        assertNull(resultOfFaultyInput3);

        assertEquals(1, resultOfCorrectInput1.length); // money (array length = 1)
        assertEquals(2, resultOfCorrectInput2.length); // draw 12 (array length = 2)
        assertEquals(2, resultOfCorrectInput3.length); // charge 10000 (array length = 2)
    }

    @Test
    public void testCommandForm(){
        //given
        String faultyInput1 = "money asdf";
        String faultyInput2 = "draw";
        String faultyInput3 = "deposit asdf asdf";
        String faultyInput4 = "deposit 1000 0";

        String correctInput1 = "money";
        String correctInput2 = "draw 12";
        String correctInput3 = "deposit 10000";
        //when
        String[] resultOfFaultyInput1 = inputProcessor.processInput(faultyInput1);
        String[] resultOfFaultyInput2 = inputProcessor.processInput(faultyInput2);
        String[] resultOfFaultyInput3 = inputProcessor.processInput(faultyInput3);
        String[] resultOfFaultyInput4 = inputProcessor.processInput(faultyInput4);

        String[] resultOfCorrectInput1 = inputProcessor.processInput(correctInput1);
        String[] resultOfCorrectInput2 = inputProcessor.processInput(correctInput2);
        String[] resultOfCorrectInput3 = inputProcessor.processInput(correctInput3);

        //then
        assertNull(resultOfFaultyInput1);
        assertNull(resultOfFaultyInput2);
        assertNull(resultOfFaultyInput3);
        assertNull(resultOfFaultyInput4);

        assertEquals(1, resultOfCorrectInput1.length); // money (array length = 1)
        assertEquals(2, resultOfCorrectInput2.length); // draw 12 (array length = 2)
        assertEquals(2, resultOfCorrectInput3.length); // charge 10000 (array length = 2)
    }

    @Test
    public void testDrawNumNaturalValue(){
        //given
        String faultyInput1 = "draw 100 000";
        String faultyInput2 = "draw 12a1";
        String faultyInput3 = "draw    ";
        String faultyInput4 = "draw 0001111";

        String correctInput1 = "draw 100000";

        //when
        String[] resultOfFaultyInput1 = inputProcessor.processInput(faultyInput1);
        String[] resultOfFaultyInput2 = inputProcessor.processInput(faultyInput2);
        String[] resultOfFaultyInput3 = inputProcessor.processInput(faultyInput3);
        String[] resultOfFaultyInput4 = inputProcessor.processInput(faultyInput4);

        String[] resultOfCorrectInput = inputProcessor.processInput(correctInput1);

        //then
        assertNull(resultOfFaultyInput1);
        assertNull(resultOfFaultyInput2);
        assertNull(resultOfFaultyInput3);
        assertNull(resultOfFaultyInput4);

        assertEquals(2, resultOfCorrectInput.length); // draw 100000 (array length = 2)
    }

    @Test
    public void testMoneyInputNaturalValue(){
        //given
        String faultyInput1 = "deposit 100 000";
        String faultyInput2 = "deposit 12a1";
        String faultyInput3 = "deposit    ";
        String faultyInput4 = "deposit 001000";

        String correctInput = "deposit 100000";

        //when
        String[] resultOfFaultyInput1 = inputProcessor.processInput(faultyInput1);
        String[] resultOfFaultyInput2 = inputProcessor.processInput(faultyInput2);
        String[] resultOfFaultyInput3 = inputProcessor.processInput(faultyInput3);
        String[] resultOfFaultyInput4 = inputProcessor.processInput(faultyInput4);

        String[] resultOfCorrectInput = inputProcessor.processInput(correctInput);

        //then
        assertNull(resultOfFaultyInput1);
        assertNull(resultOfFaultyInput2);
        assertNull(resultOfFaultyInput3);
        assertNull(resultOfFaultyInput4);

        assertEquals(2, resultOfCorrectInput.length); //charge 100000 (array length = 2)
    }

}