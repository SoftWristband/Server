package code;

import java.io.UnsupportedEncodingException;

import com.sun.jna.Library;
import com.sun.jna.Native;

import code.NlpirTest.CLibrary;

public class NlpirTest
{
	public interface CLibrary extends Library
	{
		CLibrary Instance = (CLibrary) Native.loadLibrary("NLPIR", CLibrary.class);

		public int NLPIR_Init(String sDataPath, int encoding, String sLicenceCode);

		public String NLPIR_ParagraphProcess(String sSrc, int bPOSTagged);

		public String NLPIR_GetKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);

		public String NLPIR_GetFileKeyWords(String sLine, int nMaxKeyLimit, boolean bWeightOut);

		public int NLPIR_AddUserWord(String sWord);

		public int NLPIR_DelUsrWord(String sWord);

		public String NLPIR_GetLastErrorMsg();

		public void NLPIR_Exit();
	}

	public static String transString(String aidString, String ori_encoding, String new_encoding)
	{
		try
		{
			return new String(aidString.getBytes(ori_encoding), new_encoding);
		} catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) throws Exception
	{
		//new Server();
		
		
		String argu = "";
		String system_charset = "UTF-8";
		String s="我今天很开心";
		int charset_type = 1;
		int init_flag = CLibrary.Instance.NLPIR_Init(argu, charset_type, "0");
		String nativeBytes = null;

		if (0 == init_flag)
		{
			nativeBytes = CLibrary.Instance.NLPIR_GetLastErrorMsg();
			System.err.println("初始化失败fail reason is " + nativeBytes);
		}
		try
		{
			nativeBytes = CLibrary.Instance.NLPIR_ParagraphProcess(s,0);
			System.out.println("分词结果" + nativeBytes);
			CLibrary.Instance.NLPIR_Exit();
		} 
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
}




