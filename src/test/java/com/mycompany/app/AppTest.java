package com.mycompany.app;
import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.HashMap;
import java.util.Map;
/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }
    public static void testHcodesNull(){
        Integer[][] t1=App.colors(null,new Integer[1],50,50);
        assertEquals(null,t1);
        Integer[][] t2=App.colors(null,new Integer[3],20,80);
        assertEquals(null,t2);
        Integer[][] t3=App.colors(null,new Integer[5],1,99);
        assertEquals(null,t3);
    }
    public static void testLuckyNumberNull(){
        Integer[][] t1=App.colors(new Integer[1],null,50,50);
        assertEquals(null,t1);
        Integer[][] t2=App.colors(new Integer[4],null,2,98);
        assertEquals(null,t2);
        Integer[][] t3=App.colors(new Integer[10],null,12,88);
        assertEquals(null,t3);
    }
    public static void testLength(){
        Integer[][] t1=App.colors(new Integer[6],new Integer[4],50,50);
        assertEquals(null,t1);
    }
    public static void testPercents(){
        Integer[][] t1=App.colors(new Integer[4],new Integer[4],38,52);
        assertEquals(null,t1);
        Integer[][] t2=App.colors(new Integer[4],new Integer[4],-38,52);
        assertEquals(null,t2);
        Integer[][] t3=App.colors(new Integer[4],new Integer[4],3,-22);
        assertEquals(null,t3);
    }
    public static void testColors(){
        Integer[] h={"Zumrut".hashCode(),"Sevval".hashCode(),"Rabia".hashCode(),"Fatma".hashCode()};
        Integer[] l={9, 4, 6, 3};
        Integer[][] t1=App.colors(h,l,86,14);

        assertEquals(132,(int)t1[0][0]);
        assertEquals(120,(int)t1[0][1]);
        assertEquals(140,(int)t1[0][2]);

        assertEquals(9,(int)t1[1][0]);
        assertEquals(193,(int)t1[1][1]);
        assertEquals(197,(int)t1[1][2]);

        assertEquals(45,(int)t1[2][0]);
        assertEquals(60,(int)t1[2][1]);
        assertEquals(234,(int)t1[2][2]);

        assertEquals(107,(int)t1[3][0]);
        assertEquals(101,(int)t1[3][1]);
        assertEquals(232,(int)t1[3][2]);
    }
    public static void testHandler(){
        Map<String,String> m = new HashMap<String,String>();
        m.put("names","Zumrut\nSevval");
        m.put("luckyNumbers","10\n13");
        m.put("hcodesPercent","89");
        m.put("luckyNumberPercent","11");
        App.handler(m);
    }
}
