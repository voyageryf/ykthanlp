package com.hankcs.hanlp;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.hankcs.hanlp.KeyWordRate.*;
/**
 * 此处为各个函数汇总后的结果，在Test中调用输出最终成绩
 */

public class SubjectEval {
    public static double SubEval(String TecText, String StuText, HashMap<String,Double>map) {
        YKTKeyword YKT = new YKTKeyword(StuText);
        List<String> hitKeyword=new ArrayList<String>();
        double grade = YKT.yktCorrectRate(map,hitKeyword);
        System.out.println("初始成绩为:"+ grade);
        System.out.println("学生命中关键词为:"+hitKeyword);

        //结合分数中整合关键词词性正确率
        double key=KeyWord(TecText,StuText,map);
        grade=grade*(0.9+0.1 * key);

        //判断词性
        if(WordProperty.Property(StuText)>=0.8){
            grade=grade*0.9;
        }
        //判断主谓
        if(SenCPST.Cpst(TecText,StuText)==false){
            grade=grade*0.9;
        }
        //判断句子中被动与并列是否正确！
        if(KeyOrder.KeyOrder(TecText,StuText)==false){
            grade=grade*0.9;
        }

        DecimalFormat Newdf = new DecimalFormat("0.##");
        System.out.println("最终成绩为："+Newdf.format(grade));
        return grade;
    }
}

