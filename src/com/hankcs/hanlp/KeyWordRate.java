package com.hankcs.hanlp;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 输入设定关键词
 * 根据所设定的关键词更改相应的CoNLL列表，在程序中可查看（TecWord注释部分）
 * 读出更新过的CoNLL关键词所对应的词性，并设定一个double的值，当关键词对应的词性与老师一致时，认为当前关键词命中
 * 返回的是rate值，即关键词词性的命中率
 */

public class KeyWordRate {

    //将当前教师设定的关键词拆分为CoNLL分词，并将所分得的词性写进数组中
    public static List<String> GetKey(String Text, String key){
        CoNLLWord[] Array = KeyOrder.CoNLLAnswer(Text);
        List<String> StrDEP = new ArrayList<String>();
        for (int i=0; i<=Array.length-1; i++){
            if (key.indexOf(Array[i].LEMMA)!= -1 || key.equals(Array[i].LEMMA)){
                StrDEP.add(Array[i].DEPREL);
            }
        }
        System.out.println(StrDEP);
        return StrDEP;
    }

    //将关键词写入map，其中各个关键词的权值皆为0.2
/*    public static HashMap<String,Double> MyHash( String Key1, String Key2, String Key3, String Key4, String Key5){
        HashMap<String,Double> map = new HashMap<String,Double>();
        map.put(Key1,0.2);
        map.put(Key2,0.2);
        map.put(Key3,0.2);
        map.put(Key4,0.2);
        map.put(Key5,0.2);
        return map;
    }*/

    public static double BeginRate(String TecText, String StuText, HashMap<String,Double>map){
        List<String> TecKeyList = KeyList(TecText,map);
        double TecSize = TecKeyList.size();
        System.out.println("Teacher key:"+TecKeyList);
        List<String> StuKeyList = KeyList(StuText,map);
        double StuSize = StuKeyList.size();
        System.out.println("Student key:"+StuKeyList);
        double rate = StuSize/TecSize;
        System.out.println(rate);
        return rate;
    }
    //判断当前所获取的关键词词性是否一致，直接读取map即可
    public static double KeyWord(String TecText, String StuText, HashMap<String,Double>map){
        List<String> TecKeyList = KeyList(TecText,map);//获取当前关键词，并完成对CoNLL结果的重写
        //System.out.println("教师中所有关键词为："+TecKeyList);
        List<String> StuStrDEP = new ArrayList<String>();
        List<String> TecStrDEP = new ArrayList<String>();
        CoNLLWord[] StuArray = KeyOrder.CoNLLAnswer(StuText);
        //输出学生答案CoNLL格式
/*        for (int i=0;i<=StuArray.length-1; i++){
            CoNLLWord StuWord=StuArray[i];
            System.out.println(StuWord);
        }*/
        CoNLLWord[] TecArray = KeyOrder.CoNLLAnswer(TecText);
        //输出教师答案CoNLL格式
/*        for (int i=0;i<=TecArray.length-1; i++){
            CoNLLWord TecWord=TecArray[i];
            System.out.println(TecWord);
        }*/
        for (int i=0; i<=TecKeyList.size()-1; i++){
            String NewStr = TecKeyList.get(i);//获取学生列表中关键词
            //System.out.println(NewStr);
            for (int j=0; j<=StuArray.length-1; j++){
                if(NewStr.equals(StuArray[j].LEMMA)){
                    StuStrDEP.add(StuArray[j].DEPREL);
                }
            }
            for (int k=0; k<=TecArray.length-1; k++){
                if(NewStr.equals(TecArray[k].LEMMA)){
                    TecStrDEP.add(TecArray[k].DEPREL);
                }
            }
        }
//        System.out.println(StuStrDEP);
//        System.out.println(TecStrDEP);
        double same=0;
        for (int i=0; i<=StuStrDEP.size()-1; i++){
            for(int j=0; j<=TecStrDEP.size()-1; j++){
                if(StuStrDEP.get(i).equals(TecStrDEP.get(j))){
                    same++;
                }
            }
        }

        if(same>StuStrDEP.size()){//删除句子中包含的重复词语！
            same=StuStrDEP.size();
        }

        double EndRate = same/StuStrDEP.size();
        DecimalFormat df = new DecimalFormat("0.##");
        System.out.println("当前关键词词性正确率:" + df.format(EndRate));
        return EndRate;
    }

    //仅为调试使用，查看当前CONLL结果
    public static void Test (String TecText, String StuText){
        CoNLLSentence TecAnswer= HanLP.parseDependency(TecText);
        System.out.println(TecAnswer);
        CoNLLSentence StuAnswer= HanLP.parseDependency(StuText);
        System.out.println(StuAnswer);
    }

    //获取当前关键词列表，并将CoNLL中相关词替换为自定义的关键词
    public static List<String> KeyList(String Text,HashMap<String,Double> map){
        YKTKeyword ykt = new YKTKeyword(Text);
        List<String> hitKeyword=new ArrayList<String>();
        ykt.yktCorrectRate(map,hitKeyword);
        //System.out.print("hit Keyword:"+hitKeyword);
        return hitKeyword;
    }

/*
    //主程序进行测试
    public static void main(String[] args){
        String TecText= "上层建筑是社会意识形态和政治法律制度，包含了阶级关系和维持这种关系的国家机器和社会意识形态，及其政治法律制度，组织等";
        String StuText= "上层建筑是社会意识形态和政治法律制度";

        HashMap<String,Double> map = new HashMap<String,Double>();
        map.put("社会意识形态",0.25);
        map.put("国家机器",0.25);
        map.put("政治法律制度",0.25);
        map.put("基础关系不包括", 0.15);
        map.put("香蕉", 0.1);
        YKTKeyword YKT = new YKTKeyword(StuText);
        List<String> hitKeyword=new ArrayList<String>();
        double grade = YKT.yktCorrectRate(map,hitKeyword);
        KeyWord(TecText,StuText,map);
    }
    */
}
