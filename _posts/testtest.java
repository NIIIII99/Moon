package CodePlus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Prob_1339 {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		int N = Integer.parseInt(br.readLine());
		String[] data = new String[10]; //0~9까지 A~I를 넣음 
		List<String>[] list = new List[10];
		for(int i=0;i<10;i++) {
			list[i] = new ArrayList<String>();
		}
		
		int maxLength = 0;
		for(int i=0;i<N;i++) {
//			data[i] = br.readLine();
			String str = br.readLine();
			maxLength = Math.max(maxLength, str.length());
			for(int j=0;j<str.length();j++) {
				list[j].add(String.valueOf(str.charAt(str.length()-j-1)));
			}
		}
		
		System.out.println(list[maxLength-1]);
		Map<String, Integer> check = new HashMap<String, Integer>();
		for(int j=1;j<=list[maxLength-1].size();j++) {
			if(check.get(list[maxLength-1].get(j-1)) != null) {
				
			}
			check.put(list[maxLength-1].get(j-1), 10-j);
			data[10-j] = list[maxLength-1].get(j-1);			
		}
		
		
	}
}
