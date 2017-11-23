package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Metric {
public static class HalsteadVolume{	
		
		private static final ArrayList<String> ops = new ArrayList<String>(Arrays.asList("abstract","continue","for","new","switch","assert","default","goto","package","synchronized"
				,"boolean","do","if","private","this","break","double","implements","protected","throw","byte"
				,"else","import","public","throws","case","enum","instanceof","return","transient","catch","extends"
				,"int","short","try","char","final","interface","static","void","class","finally","long","strictfp"
				,"volatile","const","float","native","super","while"
				,"+","++","-","--","*",".",";","/","%","!",">","<",">=","<=","==","=",":"
				,"{","}","(",")","[","]","<",">"));
		
		private static final String arithOps="++--*.;/%!><>=<==:{}()[]<>";
		
		private int operatorCNT =0, operandCNT =0, operatorDist =0, operandDist=0;
		
		public String rmStringOperand(String source) {
			String[] StrOperandRemoved=source.split("\"");
			String rmStringOperandSource="";
			
			//System.out.println(StrTokens);
			for(int i=0; i<StrOperandRemoved.length; i++) {
				if(i%2==1)
					operandCNT++;
				else
					rmStringOperandSource+=StrOperandRemoved[i];
			}
			return rmStringOperandSource;
		}
	}
	
	public static void commit01() {
		final String source=	"public class practice {"
				+"\npublic static void main(String[] args) {"
				+	"\nString data = \"happy new     year   djkdl!\";"
				+	"\nStringTokenizer strToken = new StringTokenizer(data,\" \");"
				+	"\nSystem.out.println(strToken.countTokens());"
				+	"\nwhile(strToken.hasMoreTokens()) {"
				+		"\nSystem.out.println(strToken.nextToken());"
				+	"\n}"
				+	"\nSystem.out.println(\"well done!\");"
				+"\n}"
			+"\n}";
		
		HalsteadVolume hals = new HalsteadVolume();
		
		String tmpSource1=hals.rmStringOperand(source);
		
		StringTokenizer StrTokens = new StringTokenizer(tmpSource1,HalsteadVolume.arithOps+", \n");
		
		System.out.println(StrTokens.countTokens());
		while(StrTokens.hasMoreTokens()) { System.out.println(StrTokens.nextToken()); }
	}
	
	public static void main(String[] args) {
		commit01();
	}
}
