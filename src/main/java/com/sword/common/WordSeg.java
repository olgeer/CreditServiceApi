package com.sword.common;

import com.alibaba.fastjson.JSONObject;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;

import java.util.List;

public class WordSeg {
    static {
        init();
    }

    public static void init(){
        WordSegmenter.seg("初始化");
    }

    public static String getWordSeg(JSONObject reqObject){
        String ret="[]";
        if(reqObject!=null && reqObject.getString("text")!=null) {
            ret = WordSegmenter.segWithStopWords(reqObject.getString("text")).toString();
        }
        return ret;
    }

    public static String analyseWordSeg(JSONObject reqObject){
        String ret="[]";
        if(reqObject!=null && reqObject.getString("text")!=null) {
            ret = WordSegmenter.seg(reqObject.getString("text")).toString();
        }
        return ret;
    }

    public static List<Word> seg(String txt){
        return WordSegmenter.seg(txt);
    }

    public static void main(String[] args){
        String txt="有一张面额100万的2020年5月到期的美的开出的商票，希望能贷款95万，年化利率不高于9%，到期一次还清。能提供的可以联系我，谢谢！";
//        System.out.println(JSONObject.toJSONString(WordSegmenter.seg(txt)));
        System.out.println(WordSegmenter.segWithStopWords(txt));
    }
}
