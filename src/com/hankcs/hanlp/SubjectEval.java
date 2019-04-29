package com.hankcs.hanlp;

import com.alibaba.fastjson.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 此处为各个函数汇总后的结果，在Test中调用输出最终成绩
 */

public class SubjectEval {

    public static JSONObject SubEval(String TecText, String StuText, HashMap<String,Double> map) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONObject data = new JSONObject();
            YKTKeyword YKT = new YKTKeyword(StuText);
            List<String> hitKeyword=new ArrayList<String>();
            double grade = YKT.yktCorrectRate(map,hitKeyword);
            data.put("grade:",grade);//初始成绩
            data.put("hitKeyword",hitKeyword);//学生命中关键词

            //结合分数中整合关键词词性正确率
            double key= KeyWordRate.KeyWord(TecText,StuText,map);
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
            data.put("score",Newdf.format(grade));//最终成绩

            jsonObject.put("code",200);
            jsonObject.put("status",true);
            jsonObject.put("message","ok");
            jsonObject.put("data",data);
        }catch (Exception e){
            jsonObject.put("code",30302);
            jsonObject.put("status",false);
            jsonObject.put("message",String.format("<ERROR-30302> demo出了 xxx 错误"));
        }finally {
            return jsonObject;
        }
    }
}

