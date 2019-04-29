/*
 * <summary></summary>
 * <author>hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2015/5/6 11:11</create-date>
 *
 * <copyright file="DemoStopWordEx.java">
 * Copyright (c) 2003-2015, hankcs. All Right Reserved, http://www.hankcs.com/
 * </copyright>
 */
package com.hankcs.hanlp;

import com.hankcs.hanlp.dictionary.CoreSynonymDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 演示如何去除停用词
 *
 * @author hankcs
 */
public class YKTKeyword
{
	  public String origText; 
	  public List<Term> termList ;
	  public List<String> strList;
	  
	  
	
	public YKTKeyword(String text){
		origText = text;
//		try {
//			segmentText("CRF");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
//	public static void setPropertiesDir(String dir){
//		HanLP.propertiesDir = dir;
//	}
	
	public double yktCorrectRate(HashMap<String,Double> standardText,List<String> hitKeyword){
		for(String key : standardText.keySet()){
			CustomDictionary.add(key);//都以nz词性来命名，具体正确性还有待测试
		}
		try {
			segmentText("CRF");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CoreStopWordDictionary.apply(termList);
		
		keyWordExtract();
		
		double rate = similar(strList,standardText,hitKeyword);
//		System.out.print("hitKeyword:");
//		System.out.println(hitKeyword);
		return rate;
	}
	
    
    @SuppressWarnings("null")
	public void termL2strL(List<Term> termL){
    	List<String> strL=new ArrayList<String>();
    	for(Term term : termL){   		
    		strL.add(term.word);  		
    	}
    	strList = strL;
    }
    
    /*
     * strL 是学生答案 ， map是标准答案，map的String是标准答案的关键词 ，Float是该关键词的权重
     */
    public static double similar(List<String> strL,HashMap<String,Double> map,List<String> hitKeyword){
    	double grade = 0;
    	for(String str : strL){
    		for( String key : map.keySet()){
    			
    			if(CoreSynonymDictionary.similarity(str,key)>0.99999 || str.equals(key)){
    				double tmp = CoreSynonymDictionary.similarity(str,key);
    				if(str.equals(key)){
    					tmp = 1.0;
    				}
//    				System.out.println(str+"\t"+key+"\t"+tmp+"\t"+map.get(key));
    				grade += map.get(key);
    				hitKeyword.add(str);
    			}
    		}
    	}
    	if(grade>1.0)
    		return 1.0;
    	
		return grade;
    		
    }

	public void segmentText(String str) throws IOException{
		//HanLP中有一系列“开箱即用”的静态分词器，以Tokenizer结尾
		
		if(str == "NLP"){
			//NLP分词NLPTokenizer会执行全部命名实体识别和词性标注
			termList = NLPTokenizer.segment(origText);
		}
		else if(str == "HanLP" || str == "Standard"){
			termList = StandardTokenizer.segment(origText);
		}
		else if(str == "CRF"){
			HanLP.Config.ShowTermNature = false;    // 
			
			CRFLexicalAnalyzer analyzer = new CRFLexicalAnalyzer();
			//CRT分词 词性标注
			
            //seg ：例如将[上海 华安 工业 集团 公司 ]合并成一个短语
            termList = analyzer.seg(origText);
			
		}
		
		termL2strL(termList);
		
	}
	
	
	
	public void keyWordExtract(String str){
		if(str == "TextRank"){
			 strList = HanLP.extractKeyword(origText, 10);
		}
		removeDuplicate();//strList删除重复的
	}
	
	public void keyWordExtract(){
		termL2strL(termList);
		removeDuplicate();//strList删除重复复的
//		System.out.println("keyword:"+strList);
	    
	}

	
	public void removeDuplicate() {
	    List<String> result = new ArrayList<String>(strList.size());
	    for (String str : strList) {
	        if (!result.contains(str)) {
	            result.add(str);
	        }
	    }
	    strList.clear();
	    strList.addAll(result);
	}

/*
    //主程序进行测试
    public static void main(String[] args)
    {
    	System.out.println("main");
    	
    	String text1 = "苹果，上层建筑是社会意识形态和政治法律制度，包含了阶级关系和维持这种关系的国家机器和社会意识形态，及其政治法律制度，组织等";
    	HashMap<String,Double> map = new HashMap<String,Double>();
    	map.put("社会意识形态",0.25);
    	map.put("国家机器",0.25);
    	map.put("政治法律制度",0.25);
    	map.put("基础关系不包括", 0.15);
    	map.put("香蕉", 0.1);
    	

//    	System.out.println("args[0]:"+args[0]);
//    	if (args[0] != "") {
//    		HanLP.Config.propertiesDir = args[0];
//    		System.out.println("HanLP启动了");
//    	}
//    	System.out.println("设置后："+HanLP.Config.propertiesDir);
//
    	YKTKeyword ykt = new YKTKeyword(text1);
    	List<String> hitKeyword=new ArrayList<String>();
    	double grade = ykt.yktCorrectRate(map,hitKeyword);
    	System.out.println("grade rate:"+grade);
    	System.out.print("hit Keyword:"+hitKeyword);
    }
    */
}
