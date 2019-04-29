package com.hankcs.hanlp;

import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;
import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 *此程序在于判断句子中是否含有被动或并列成分！
 */
public class KeyOrder {

    //获取当前教师与学生的CoNLLSentence输出结果
    public static CoNLLWord[] CoNLLAnswer(String Text){
        CoNLLSentence Answer= HanLP.parseDependency(Text);
        CoNLLWord[] Array = Answer.getWordArray();
        return Array;
    }

    //判断主被动关系成分是否一致，PassiveSen调用！
    public static boolean RelaPassive(String TecText, String StuText, String TecProperty, String StuProperty){
        Boolean result= new Boolean("true");
        CoNLLWord[] TecArray = CoNLLAnswer(TecText);
        CoNLLWord[] StuArray = CoNLLAnswer(StuText);
        for (int i = 0; i <= TecArray.length-1; i++)
        {
            CoNLLWord TecWord = TecArray[i];
            //前置宾语与主谓关系判断
            if (TecWord.DEPREL.equals(TecProperty)){
                String TecLEMA= TecWord.LEMMA;
                for (int j = 0;j <= StuArray.length-1;j++){
                    CoNLLWord StuWord = StuArray[j];
                    if (StuWord.DEPREL.equals(StuProperty)){
                        String StuLEMA= StuWord.LEMMA;
                        if(CoreSynonymDictionary.similarity(TecLEMA,StuLEMA)>0.99||StuLEMA.equals(TecLEMA)){
                            result = true;
                        }else{
                            result = false;
                        }
                    }
                }
            }
        }
        return result;
    }

    //判断当前学生答案中的前置宾语与主谓关系以及二者的介宾关系是否一致
    public static boolean PassiveSen(String TecText, String StuText){
        Boolean result;
        Boolean ResultA = RelaPassive(TecText,StuText,"前置宾语","主谓关系");
        Boolean ResultB = RelaPassive(TecText,StuText,"介宾关系","介宾关系");
        if(ResultA==true && ResultB==true){
            result= true;
        }else{
            result= false;
        }
        System.out.println(result);
        return result;
    }

    //提取当前词性间关系的函数，Parallel调用！
    public static String RelaParallel (String Text, String Property){
        String Relation = new String("关系");
        CoNLLSentence Answer = HanLP.parseDependency(Text);
        CoNLLWord[] Array = Answer.getWordArray();
        for (int i = 0; i <= Array.length - 1; i++) {
            CoNLLWord Word = Array[i];
            //并列关系判断
            if (Word.DEPREL.equals(Property))
                Relation = Word.LEMMA;
        }
        return Relation;
    }

    //判断并列成分中词性关系是否与教师一致
    public static boolean Parallel(String TecText, String StuText) {
        Boolean result;
        CoNLLWord[] TecWord=CoNLLAnswer(TecText);
        List<String> TecDEP= new ArrayList<String>();
        //判断当前成分中是否包含左附加关系，并将其两侧的词性写入数组
        for (int i=0; i<TecWord.length-1; i++){
            if(TecWord[i].DEPREL.equals("左附加关系")){
            TecDEP.add(TecWord[i-1].DEPREL);
            TecDEP.add(TecWord[i+1].DEPREL);
            }
        }
        //System.out.println(TecDEP);

        CoNLLWord[] StuWord=CoNLLAnswer(StuText);
        List<String> StuDEP= new ArrayList<String>();
        for (int i=0; i<StuWord.length-1; i++){
            if(StuWord[i].DEPREL.equals("左附加关系")){
                StuDEP.add(StuWord[i-1].DEPREL);
                StuDEP.add(StuWord[i+1].DEPREL);
            }
        }
        //System.out.println(StuDEP);
        int same=0;
        //关系成分数组长度不等则直接为false，如果相等，则只有数组完全相等时才为true！
        if(StuDEP.size()!=TecDEP.size()){
            result=false;
        }else{
            for(int i=0; i<=StuDEP.size()-1; i++){
                for(int j=0; j<=TecDEP.size()-1; j++){
                    if(StuDEP.get(i).equals(TecDEP.get(j))){
                        same++;
                    }
                }
            }
            //System.out.println("当前相同词性为："+same);
            if(same==StuDEP.size()){
                result=true;
            }else{
                result=false;
            }
        }
        //System.out.println(result);
        return result;
    }

    /**
     * 构造一个总函数，调用被动关系以及并列关系对句子进行判断！
     */
    public static boolean KeyOrder(String TecText, String StuText){
        Boolean result= new Boolean("true");
        CoNLLWord[] TecArray = CoNLLAnswer(TecText);
        int P=0;
        for (int i = 0; i<= TecArray.length-1; i++){
            CoNLLWord Word = TecArray[i];
            if(Word.DEPREL.equals("前置宾语")){
                System.out.println("发现前置宾语");
                if (PassiveSen(TecText,StuText)==true){
                    result = true;
                }else {
                    result = false;
                }
            }
            if(Word.DEPREL.equals("左附加关系")){
                P++;
            }
        }
        if(P>=1){//判断系统中是否有并列关系
            //System.out.println("发现并列关系：");
            if (Parallel(TecText,StuText)==true){
                result = true;
            }else{
                result = false;
            }
        }
        System.out.println("并列或被动是否一致："+result);
        return result;
    }

    //仅为调试使用，查看当前CONLL结果
    public static void Test (String TecText, String StuText){
        CoNLLSentence TecAnswer= HanLP.parseDependency(TecText);
        System.out.println(TecAnswer);
        CoNLLSentence StuAnswer= HanLP.parseDependency(StuText);
        System.out.println(StuAnswer);
    }

/*
    //主程序进行测试
    public static void main(String[] args){
        String TecText="苹果和菠萝一样甜";
        String StuText="香蕉和西瓜一样甜";
        //Test(TecText,StuText);
        KeyOrder(TecText,StuText);
    }
*/

}
