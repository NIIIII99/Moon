
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
/*
1
10/22 02:52:48 
 */
public class Prob_A_0022 {
	static int count;
	static long dfsCnt;
	static int minNumCnt, fixUpperCnt;
	
	static int[][] combiArr = {
			{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},//0
			{1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},//1
			{1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0},//2
			{1, 3, 3, 1, 0, 0, 0, 0, 0, 0, 0},//3
			{1, 4, 6, 4, 1, 0, 0, 0, 0, 0, 0},//4
			{1, 5, 10, 10, 5, 1, 0, 0, 0, 0, 0},//5
			{1, 6, 15, 20, 15, 6, 1, 0, 0, 0, 0},//6
			{1, 7, 21, 35, 35, 21, 7, 1, 0, 0, 0},//7
			{1, 8, 28, 56, 70, 56, 28, 8, 1, 0, 0},//8
			{1, 9, 36, 84, 126, 126, 84, 36, 9, 1, 0},//9
			{1, 10, 45, 120, 210, 252, 210, 120, 45, 10, 1}//10
	};
	static int[][] dp = new int[36][36]; 
	static List<Integer>[] data = new List[36];
	static int[] distance, numCnt, engCnt;
	static boolean[] visited1, visited2;
	static Stack<Integer> stack;
	public static void main(String[] args) throws Exception {
		for(int i=0;i<=35;i++) {
			data[i] = new ArrayList<Integer>();
		}
		setKeyboard();
		
		

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		int T = Integer.parseInt(br.readLine());
		for(int t=1;t<=T;t++) {
			String input = br.readLine();
			int month = Integer.parseInt(input.substring(0, 2));
			int day = Integer.parseInt(input.substring(3, 5));
			int hour = Integer.parseInt(input.substring(6, 8));
			int minute = Integer.parseInt(input.substring(9, 11));
			int second = Integer.parseInt(input.substring(12, 14));

			int first = (day+second) % 36;
			int last = (month+hour+minute)%36;
			minNumCnt = minute/10;
			fixUpperCnt = second/10;
//			bw.write(first +" "+last+" "+minNumCnt+" "+fixUpperCnt+" ");

			
			visited1 = new boolean[36];
			visited2 = new boolean[36];
			stack = new Stack();
			dfsCnt = 0;
			count = 1;
			dfs(first, last);	
			bw.write("#"+t+" "+dfsCnt);
			bw.newLine();
		}
		bw.flush();

	}

	private static void dfs(int cur, int last) {
		if(visited1[cur]) {
			visited2[cur]=true;
		}else {
			visited1[cur] = true;		
		}
		
		stack.push(cur);
		if(cur==last && count==10) {
			int numCnt = 0;
			int chkCnt = 0;
			int check[] = new int[36];
			for(int i=0;i<stack.size();i++) {
				int element = stack.elementAt(i);
				check[element]++;
				if(check[element]>1) chkCnt += check[element]; 
				if(element <=9) numCnt++;
			}
			int engCnt= 10-numCnt;	//10-numCnt engCnt		
			if(numCnt>=minNumCnt && engCnt >= fixUpperCnt) {
				if(engCnt-chkCnt < fixUpperCnt && chkCnt>0) return;
//				if(engCnt == fixUpperCnt && chkCnt>0) return;
//				System.out.println(numCnt+"+"+engCnt+"("+fixUpperCnt+")"+combiArr[engCnt][fixUpperCnt]);
				int rslt=0;
				if(chkCnt>0 ) {
//					System.out.println(engCnt+":"+chkCnt+":"+fixUpperCnt);
					if(fixUpperCnt>chkCnt) {
						int tmp = chkCnt/2;
						rslt = combiArr[engCnt-tmp][fixUpperCnt-tmp];
//						rslt = rslt - combiArr[engCnt-chkCnt][fixUpperCnt-chkCnt];						
					}else {
						rslt = combiArr[chkCnt/2][fixUpperCnt];						
					}
				}else {
					rslt = combiArr[engCnt][fixUpperCnt];
				}

				dfsCnt += rslt;
//				for(int i=0;i<stack.size();i++) {
//					System.out.println(stack.elementAt(i));
//				}
//				System.out.println(rslt+"----------");
			}
			return;
		}
		for(int next:data[cur]) {
			if(!visited1[next] || !visited2[next]){
				if(next<=9 && visited1[next]) {
					continue;
				}
				count++;
				if(count<=10) {
					dfs(next, last);					
					if(visited2[next]) {
						visited2[next] = false;						
					}else{
						visited1[next] = false;
					}
					stack.pop();
				}
				count--;
			}
		}
	}

	private static void setKeyboard() {
		//0
		data[0].add(1);
		data[0].add(10);
		//1~8
		for(int i=1;i<=8;i++) {
			data[i].add(i-1);
			data[i].add(i+1);
			data[i].add(i+9);
			data[i].add(i+10);
		}
		//9
		data[9].add(8);
		data[9].add(18);
		data[9].add(19);
		//10
		data[10].add(0);
		data[10].add(1);
		data[10].add(11);
		data[10].add(20);

		//11~18
		for(int i=11;i<=18;i++) {
			data[i].add(i-10);
			data[i].add(i-9);
			data[i].add(i-1);
			data[i].add(i+1);
			data[i].add(i+9);
			data[i].add(i+10);
		}

		//19
		data[19].add(9);
		data[19].add(18);
		data[19].add(28);

		//20
		data[20].add(10);
		data[20].add(11);
		data[20].add(21);
		data[20].add(29);

		//21~26
		for(int i=21;i<=26;i++) {
			data[i].add(i-10);
			data[i].add(i-9);
			data[i].add(i-1);
			data[i].add(i+1);
			data[i].add(i+8);
			data[i].add(i+9);
		}

		//27
		data[27].add(17);
		data[27].add(18);
		data[27].add(26);
		data[27].add(28);
		data[27].add(35);

		//28
		data[28].add(18);
		data[28].add(19);
		data[28].add(27);

		//29
		data[29].add(20);
		data[29].add(21);
		data[29].add(30);

		//30~34
		for(int i=30;i<=34;i++) {
			data[i].add(i-9);
			data[i].add(i-8);
			data[i].add(i-1);
			data[i].add(i+1);
		}

		//35
		data[35].add(26);
		data[35].add(27);
		data[35].add(34);


	}

}
