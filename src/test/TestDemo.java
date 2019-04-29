package test;

import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.SubjectEval;

import java.util.HashMap;

public class TestDemo {

    public static void main(String[] args){
        testDemo1();
    }

    public static  void testDemo1(){
        /*模拟参数*/
        String TecText="上层建筑是社会意识形态和政治法律制度，包含了阶级关系和维持这种关系的国家机器和社会意识形态，及其政治法律制度，组织等";
        String StuText="上层建筑是社会意识形态和政治法律制度";
        HashMap<String,Double> map = new HashMap<String,Double>();
        map.put("社会意识形态",0.25);
        map.put("国家机器",0.25);
        map.put("政治法律制度",0.25);
        map.put("阶级关系", 0.15);
        map.put("上层建筑", 0.1);
        /*模拟调用*/
        JSONObject jsonObject = SubjectEval.SubEval(TecText,StuText,map);
//        ========= End ========
        System.err.println(jsonObject.toString());
    }
}
