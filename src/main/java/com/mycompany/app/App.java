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
    public static Map theMap(Map<String,String> params){
      String names = params.get("names");
      java.util.Scanner sc1 = new java.util.Scanner(names);
      sc1.useDelimiter("[;\r\n]+");
      java.util.ArrayList<Integer> hcodesList = new java.util.ArrayList<>();
      java.util.ArrayList<String> nameList = new java.util.ArrayList<>();
      while (sc1.hasNext())
      {
        String name=sc1.next().trim();
        if(name.replaceAll("(\\s+)","").length()>0){
          nameList.add(name);
          hcodesList.add(name.hashCode());
        }
      }

      String luckyNumbers = params.get("luckyNumbers");
      java.util.Scanner sc2 = new java.util.Scanner(luckyNumbers);
      sc2.useDelimiter("[;\r\n]+");
      java.util.ArrayList<Integer> luckyNumberList = new java.util.ArrayList<>();
      while (sc2.hasNext())
      {
        try{
          int value = Integer.parseInt(sc2.next().replaceAll("\\s",""));
          luckyNumberList.add(value);
        }catch(NumberFormatException e){

        }
      }
      int hcodesPercent=Integer.parseInt(params.get("hcodesPercent").replaceAll("\\s",""));
      int luckyNumberPercent=Integer.parseInt(params.get("luckyNumberPercent").replaceAll("\\s",""));

      Map map = new HashMap();
      Integer[][] colors=App.colors(hcodesList.toArray(new Integer[hcodesList.size()]),luckyNumberList.toArray(new Integer[luckyNumberList.size()]),hcodesPercent,luckyNumberPercent);
      if(colors==null)
        map.put("result","<b>Wrong format...</b>");
      else{
        String s="";
        for(int i=0; i<colors.length; i++){
          s+="<div class=\"row\">"+
              "<div class=\"box\">"+nameList.get(i)+"</div>"+
              "<div class=\"box\">"+luckyNumberList.get(i)+"</div>"+
              "<div class=\"box\"><div class=\"colorbox\" style=\"background:rgb("+colors[i][0]+","+colors[i][1]+","+colors[i][2]+");\"></div></div>"+
              "</div>";
        }
        map.put("result", s);
      }
      System.out.println(map.get("result"));
      return map;
    }
    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        get("/", (req, res) -> "Hello, World");

        post("/compute", (req, res) -> {
          Map map = new HashMap();
          map.put("names",req.queryParams("names"));
          map.put("luckyNumbers",req.queryParams("luckyNumbers"));
          map.put("hcodesPercent",req.queryParams("hcodesPercent"));
          map.put("luckyNumberPercent",req.queryParams("luckyNumberPercent"));
          return new ModelAndView(App.theMap(map), "compute.mustache");
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
