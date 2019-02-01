package com.mycompany.app;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App
{
    public static Integer[] nameToColor(int num){
      Integer[] color=new Integer[3];
      color[0]=((num<<24)>>24)&0xff;   //the least significiant 8-bit
      color[1]=((num<<16)>>24)&0xff;
      color[2]=((num<<8)>>24)&0xff;
      return color;
    }
    public static Integer[][] colors(Integer[] hcodes, Integer[] luckyNumber, int hcodesPercent, int luckyNumberPercent){
      if(hcodes==null || luckyNumber==null || hcodes.length!=luckyNumber.length || ((hcodesPercent+luckyNumberPercent)!=100) || hcodesPercent<0 || luckyNumberPercent<0)
        return null;
      
      Integer[][] result=new Integer[hcodes.length][3];
      for(int j=0; j<hcodes.length; j++){
        result[j]=nameToColor((hcodes[j]*hcodesPercent/100)+(luckyNumber[j]*luckyNumberPercent/100));
      }
      return result;
    }
    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        get("/", (req, res) -> "Hello, World");

        post("/compute", (req, res) -> {
          //System.out.println(req.queryParams("input1"));
          //System.out.println(req.queryParams("input2"));

          String names = req.queryParams("names");
          java.util.Scanner sc1 = new java.util.Scanner(names);
          sc1.useDelimiter("[;\r\n]+");
          java.util.ArrayList<Integer> hcodesList = new java.util.ArrayList<>();
          java.util.ArrayList<String> nameList = new java.util.ArrayList<>();
          while (sc1.hasNext())
          {
            String name=sc1.next();
            nameList.add(name);
            hcodesList.add(name.hashCode());
          }

          String luckyNumbers = req.queryParams("luckyNumbers");
          java.util.Scanner sc2 = new java.util.Scanner(luckyNumbers);
          sc2.useDelimiter("[;\r\n]+");
          java.util.ArrayList<Integer> luckyNumberList = new java.util.ArrayList<>();
          while (sc2.hasNext())
          {
            int value = Integer.parseInt(sc2.next().replaceAll("\\s",""));
            luckyNumberList.add(value);
          }
          int hcodesPercent=Integer.parseInt(req.queryParams("hcodesPercent").replaceAll("\\s",""));
          int luckyNumberPercent=Integer.parseInt(req.queryParams("luckyNumberPercent").replaceAll("\\s",""));

          Map map = new HashMap();
          Integer[][] colors=App.colors((Integer[])hcodesList.toArray(),(Integer[])luckyNumberList.toArray(),hcodesPercent,luckyNumberPercent);
          if(colors==null)
            map.put("result","Wrong format...");
          else
            map.put("result", colors);
          return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());


        get("/compute",
            (rq, rs) -> {
              Map map = new HashMap();
              map.put("result", "not computed yet!");
              return new ModelAndView(map, "compute.mustache");
            },
            new MustacheTemplateEngine());
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
