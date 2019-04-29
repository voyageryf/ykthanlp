package com.hankcs.hanlp;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

/**
 * 此程序的作用是在于判断答案中的句子成分是否过于单一
 * 即当老师的句子成分中有主谓关系时，学生的答案句子成分中也必须有主谓关系
 * 程序输入的为句子，输出的为判断结果，后期可将输出改为分数的权重值
 */

public class SenCPST {
    //此函数的启动类，包含对主谓关系的判断，后期可根据需要进行扩展
    public static boolean Cpst (String TecText,String StuText) {
        boolean result;
        if (Sub_Pre(TecText,StuText)==true){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
    //判断是否主谓关系一致
    public static boolean Sub_Pre (String TecText,String StuText){
        boolean result;
        CoNLLSentence TecAnswer= HanLP.parseDependency(TecText);
        //System.out.println("教师答案:\n"+(TecAnswer));
        CoNLLWord[] TecArray = TecAnswer.getWordArray();
        int TecTemp=0;
        for (int i = 0; i <= TecArray.length-1; i++)
        {
            CoNLLWord Tword = TecArray[i];
            if(Tword.DEPREL.equals("主谓关系")){
                TecTemp=1;
            }
        }

        CoNLLSentence StuAnswer=HanLP.parseDependency((StuText));
        //System.out.println("学生答案：\n"+(StuAnswer));
        CoNLLWord[] StuArray= StuAnswer.getWordArray();
        int StuTemp=0;
        for (int i = 0; i <= StuArray.length-1; i++)
        {
            CoNLLWord Sword = StuArray[i];
            if(Sword.DEPREL.equals("主谓关系")){
                StuTemp=1;
            }
        }
        if(TecTemp == StuTemp){
            result = true;//当前主谓关系一致
        }else{
            result = false;//当前主谓关系不一致
        }
        System.out.println("当前主谓关系是否一致："+result);
        return result;
    }
}
