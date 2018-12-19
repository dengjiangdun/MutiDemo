package com.example.djd.fingertest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

       // assertEquals("com.example.djd.fingertest", appContext.getPackageName());
        char c = 's';
        assertEquals(c,'s');
        System.out.print(c == 's');

        final Vihicle vihicle = new Car();

        Vihicle vihicle1 = (Vihicle) Proxy.newProxyInstance(vihicle.getClass().getClassLoader(), Car.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object object;
                object = method.invoke(vihicle, args);
                return object;
            }
        });

        System.out.println(vihicle1.run()+"johndonhahahah");
    }


    public interface  Vihicle{
        public String run();
    }

    public class Car implements Vihicle{

        @Override
        public String run() {
            System.out.println("run car johndonhahahah");
            return "car";
        }

    }

}
