package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

//import utils.SystemParas;

import com.sun.jna.Library;
import com.sun.jna.Native;

import code.NlpirTest.CLibrary;

public class SentimentAnalysis
{

	static String filename1="lib/dictionary/adverbs of degree dictionary/insufficiently.txt";
	static String filename2="lib/dictionary/adverbs of degree dictionary/inverse.txt";
	static String filename3="lib/dictionary/adverbs of degree dictionary/ish.txt";
	static String filename4="lib/dictionary/adverbs of degree dictionary/more.txt";
	static String filename5="lib/dictionary/adverbs of degree dictionary/most.txt";
	static String filename6="lib/dictionary/adverbs of degree dictionary/over.txt";
	static String filename7="lib/dictionary/adverbs of degree dictionary/very.txt";
	static String filename8="lib/dictionary/positive and negative dictionary/negdict.txt";
	static String filename9="lib/dictionary/positive and negative dictionary/posdict.txt";
	
	static String[] insufficientlyDic =new String[12];
	static String[] inverseDic=new String[20];
	static String[] ishDic=new String[31];
	static String[] moreDic=new String[38];
	static String[] mostDic=new String[65];
	static String[] overDic=new String[30];
	static String[] veryDic=new String[41];
	static String[] negtiveDic=new String[10271];
	static String[] positiveDic=new String[6062];
	
	public double getEmotion(String s) throws Exception
	{
		//鍒濆鍖栬瘝琛�
		input(filename1,insufficientlyDic);
		input(filename2,inverseDic);
		input(filename3,ishDic);
		input(filename4,moreDic);
		input(filename5,mostDic);
		input(filename6,overDic);
		input(filename7,veryDic);
		input(filename8,negtiveDic);
		input(filename9,positiveDic);		
		
		String s2=insufficientlyDic[0];
		
		if(s.equals(s2))
			System.out.println("0000");
		
		System.out.println(s);
		System.out.println(s2);
				
		ArrayList<String> group=new ArrayList<String>();
		ArrayList<String> word=new ArrayList<String>();
		double score=0;
		group=segmentByChar(',',s);
		for(int i=0;i<group.size();i++)
		{
			word=segmentByChar(' ',segment(group.get(i)));
			score+=groupScore(word);
		}
		System.out.println(score);
		return score;
	}
	
	protected void input(String filename,String[] s) throws FileNotFoundException
	{
		int i=0;
		File file=new File(filename);
		Scanner input =new Scanner(file,"utf-8");
		while(input.hasNext())
		{
			s[i]=input.nextLine();
			i++;
		}
		input.close();
	}
	protected String segment(String s)
	{
		String argu = "";
		// String system_charset = "GBK";//GBK----0
		String system_charset = "UTF-8";
		int charset_type = 1;
		//鍒濆鍖栧垎璇嶆帴鍙�
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;

		if (0 == init_flag)
		{
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("鍒濆鍖栧け璐ワ紒fail reason is " + nativeBytes);
			return nativeBytes;
		}
		try
		{
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(s,0);
			System.out.println("鍒嗚瘝缁撴灉涓猴細" + nativeBytes);
			CLibrary.Instance.NLPIR_Exit();
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return nativeBytes;
	}
	protected ArrayList<String> segmentByChar(char c,String s)
	{
		ArrayList<String> list =new ArrayList<String>();
		int begin=0;
		int end=s.indexOf(c,begin);
		
		while(end>0)
		{
			list.add(s.substring(begin,end));
			begin=end+1;
			end=s.indexOf(c,begin);
		}
		if(begin!=s.length())
			list.add(s.substring(begin,s.length()));
		return list;
	}
	protected double groupScore(ArrayList<String> word)
	{
		double groupscore=0;
		int last_sent_word=0;//涓婁竴涓儏鎰熻瘝鐨勪綅缃�
		int s=0;//鎯呮劅璇嶇殑浣嶇疆
		double count=0;
		for(int i=0;i<word.size();i++)
		{
			if((count=matchPosAndNeg(word.get(i)))!=0)
			{
				for(int j=last_sent_word;j<i;j++)
					count*=matchAdverb(word.get(j));
				groupscore+=count;
				count=0;
				last_sent_word=i;
			}	
		}
		System.out.println(groupscore);
		return groupscore;
	}
	protected double matchAdverb(String wd)
	{
		if(match(wd,mostDic))
			return 2.0;
		if(match(wd,overDic))
			return 1.5;
	    if(match(wd,veryDic))
			return 1.25;
		if(match(wd,moreDic))
			return 1.2;
		if(match(wd,ishDic))
			return 0.5;
		if(match(wd,insufficientlyDic))
			return 0.25;
		if(match(wd,inverseDic))
			return -1.0;
		return 1;
		
	}
	protected double matchPosAndNeg(String wd)
	{
		if(match(wd,positiveDic))
			return 1.0;
		else if(match(wd,negtiveDic))
			return -1.0;
		return 0;
	}
	protected boolean match(String wd,String[] list)
	{
		for(int i=0;i<list.length;i++)
		{
			if(wd.equals(list[i]))
				return true;
		}
		return false;
	}
	
}

