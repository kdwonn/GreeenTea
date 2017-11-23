package greentea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Metric {
	public static class HalsteadVolume{	
			
			private static final ArrayList<String> keyword = new ArrayList<String>(Arrays.asList(
					"abstract","continue","for","new","switch","assert","default","goto","package","synchronized"
					,"boolean","do","if","private","this","break","double","implements","protected","throw","byte"
					,"else","import","public","throws","case","enum","instanceof","return","transient","catch","extends"
					,"int","short","try","char","final","interface","static","void","class","finally","long","strictfp"
					,"volatile","const","float","native","super","while"
					));
			
			private static final String[] ops = {
					"++","--","*",".",";","/","%","!",">","<",">=","<=","==",":"
					,"{","}","(",")","[","]","<",">"};
		
			private static final String arithOps="++--*.;/%!><>=<==:{}()[]<>";
			
			private int operatorCNT =0, operandCNT =0, operatorDist =0, operandDist=0;
			
			
			public int countWord(String source, String findStr) {
				int lastIndex = 0;
				int count = 0;

				while(lastIndex != -1){
				    lastIndex = source.indexOf(findStr,lastIndex);

				    if(lastIndex != -1){
				        count ++;
				        lastIndex += findStr.length();
				    }
				}
				return count;
			}
			
			
			public void arithOpCheck(String source) {
				for(int i=0; i<ops.length; i++) {
					if(source.contains(ops[i])) {
						operatorDist++;
						operatorCNT+=countWord(source, ops[i]);
					}
				}
			}
			
			
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
			
			public void classifyKeword(StringTokenizer StrTokens) {
				while(StrTokens.hasMoreTokens()) { 
					if(keyword.contains(StrTokens.nextToken())) {
						operatorDist++;
						operatorCNT++;
					}
					else
						operandDist++;
						operandCNT++;
				}
			}
			public double calHalsteadVol() {
				return (operatorCNT + operandCNT) * Math.log(operandDist+operatorDist);
			}
		}
		
		public static void implementation() {
			final String source= "public class practice {"
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
			
			hals.arithOpCheck(source);
			System.out.println(hals.operatorCNT);
			System.out.println(hals.operatorDist);
			System.out.println(hals.operandCNT);
			System.out.println(hals.operandDist);
			
			String tmpSource1=hals.rmStringOperand(source);
			
			StringTokenizer StrTokens = new StringTokenizer(tmpSource1,HalsteadVolume.arithOps+", \n");
			
			hals.classifyKeword(StrTokens);
			System.out.println(hals.operatorCNT);
			System.out.println(hals.operatorDist);
			System.out.println(hals.operandCNT);
			System.out.println(hals.operandDist);
			
			System.out.println(hals.calHalsteadVol());
			
		}
		
		public static void main(String[] args) {
			implementation();
		}
}
