package code;

import java.io.UnsupportedEncodingException;

import com.sun.jna.Library;
import com.sun.jna.Native;

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
		new Server();
	}
}




