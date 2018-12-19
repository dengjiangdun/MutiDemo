package com.example.djd.fingertest;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
   /* @Test
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
    }*/
    @Mock
    Context mContext;

    @Test
    public void test_is(){
        StringBuilder sb = new StringBuilder();
        System.out.println("mathtest"+Math.round(12.5));
        System.out.println("mathtest"+Math.round(-12.5));
    }


}