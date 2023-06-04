package pachinko;

import org.junit.Test;

import static org.junit.Assert.*;

public class PachinkoInputProcessorTest {
    PachinkoInputProcessor inputProcessor = new PachinkoInputProcessor();

    //0~9, a~z, A~Z, SPACE 외의 문자 입력 불가?
    @Test
    public void inputStringRegexTest(){
        //given
        String faultyInput1 = "mone^";
        String faultyInput2 = "Draw ++++";
        String faultyInput3 = "charge 00$";

        String correctInput1 = "money";
        String correctInput2 = "draw 12";
        String correctInput3 = "charge 10000";

        //when
        String[] result1 = inputProcessor.processInput(faultyInput1);
        String[] result2 = inputProcessor.processInput(faultyInput2);
        String[] result3 = inputProcessor.processInput(faultyInput3);

        String[] result4 = inputProcessor.processInput(correctInput1);
        String[] result5 = inputProcessor.processInput(correctInput2);
        String[] result6 = inputProcessor.processInput(correctInput3);
        //then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);

        assertEquals(1, result4.length);
        assertEquals(2, result5.length);
        assertEquals(2, result6.length);

    }

    //20문자까지만 입력 가능?
    @Test
    public void input30LengthTest(){
        //given
        String faultyInput1 = "                                        ";
        String faultyInput2 = "money                     ";
        String faultyInput3 = "charge 12222222222222222222222222222222";
        //when
        String[] result1 = inputProcessor.processInput(faultyInput1);
        String[] result2 = inputProcessor.processInput(faultyInput2);
        String[] result3 = inputProcessor.processInput(faultyInput3);

        //then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
    }

    //명령어와 맞지 않는 형식 입력 불가?
    @Test
    public void commandFormTest(){
        //given
        String faultyInput1 = "money asdf";
        String faultyInput2 = "draw";
        String faultyInput3 = "charge asdf asdf";
        String faultyInput4 = "charge 1000 0";

        String correctInput1 = "money";
        String correctInput2 = "draw 12";
        String correctInput3 = "charge 10000";
        //when
        String[] result1 = inputProcessor.processInput(faultyInput1);
        String[] result2 = inputProcessor.processInput(faultyInput2);
        String[] result3 = inputProcessor.processInput(faultyInput3);
        String[] result4 = inputProcessor.processInput(faultyInput4);

        String[] result5 = inputProcessor.processInput(correctInput1);
        String[] result6 = inputProcessor.processInput(correctInput2);
        String[] result7 = inputProcessor.processInput(correctInput3);

        //then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);

        assertEquals(1, result5.length);
        assertEquals(2, result6.length);
        assertEquals(2, result7.length);
        //then
    }

    //뽑기 횟수 중간에 space없이 자연수만 입력 가능
    @Test
    public void drawNumTest(){
        //given
        String faultyInput1 = "draw 100 000";
        String faultyInput2 = "draw 12a1";
        String faultyInput3 = "draw    ";
        String faultyInput4 = "draw 0001111";


        String correctInput1 = "draw 100000";

        //when
        String[] result1 = inputProcessor.processInput(faultyInput1);
        String[] result2 = inputProcessor.processInput(faultyInput2);
        String[] result3 = inputProcessor.processInput(faultyInput3);
        String[] result4 = inputProcessor.processInput(faultyInput4);

        String[] result5 = inputProcessor.processInput(correctInput1);

        //then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);

        assertEquals(2, result5.length);
    }

    //충전 금액 중간에 space없이 자연수만 입력 가능
    @Test
    public void moneyInputForChargeTest(){
        //given
        String faultyInput1 = "charge 100 000";
        String faultyInput2 = "charge 12a1";
        String faultyInput3 = "charge    ";
        String faultyInput4 = "charge 001000";

        String correctInput1 = "charge 100000";

        //when
        String[] result1 = inputProcessor.processInput(faultyInput1);
        String[] result2 = inputProcessor.processInput(faultyInput2);
        String[] result3 = inputProcessor.processInput(faultyInput3);
        String[] result4 = inputProcessor.processInput(faultyInput4);

        String[] result5 = inputProcessor.processInput(correctInput1);

        //then
        assertNull(result1);
        assertNull(result2);
        assertNull(result3);
        assertNull(result4);

        assertEquals(2, result5.length);
    }

}