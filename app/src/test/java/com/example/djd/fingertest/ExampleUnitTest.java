package com.example.djd.fingertest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(6, 2 + 2);

    }

    @Test(timeout = 2)
    public void testTimeOut(){
        int i = 100;
        while (i >0) {
            System.out.println("i = "+i);
            i--;
        }
    }

}