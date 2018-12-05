# ddd
##


3
3 
2 3 1 
4 
2 3 4 2 
8
3 3 6 6 6 7 8 6 

답 ->
#1 9
#2 15
#3 47

```java
package swcert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Prob_0050 {
	static int count, cycleCnt, wayCnt, ansCnt;
	static int[] data, dataRev, visit, ans;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		
		int T = Integer.parseInt(br.readLine());
		for(int t=1;t<=T;t++) {
			int N = Integer.parseInt(br.readLine());
			data = new int[N+1];
			visit = new int[N+1];
			ans = new int[N+1];
			StringTokenizer st = new StringTokenizer(br.readLine());
			for(int i=1;i<=N;i++) {
				int to = Integer.parseInt(st.nextToken());
				data[i] = to;
			}
			
			//find cycle
			wayCnt=0;
			count=0;
			ansCnt = 0;
			cycleCnt = 0;
			
			for(int i=1;i<=N;i++) {
				if(visit[i] > 0) continue;
//				dfs(i, i);
				dfs2(i, i, 0);
			}
			
			//cal
			int rlst = (cycleCnt*(cycleCnt-1)*cycleCnt/2);
			if(N-cycleCnt > 0) {
//				rlst += (N-cycleCnt)*N;
				rlst += ansCnt;
			}			 
//			System.out.println(rlst);
			bw.write("#"+t+" "+rlst);
			bw.newLine();

		}
		bw.flush();
	}

	private static void dfs(int now, int start) {
		if(visit[now] > 0) return;
		
		wayCnt++;
		count++;
		visit[now] = count;
		
		//now~ data[now] 가 사이클..
		if(visit[data[now]] >= visit[start]) {
			ans[start] = wayCnt;
			if(cycleCnt ==0) {
				cycleCnt = visit[now] - visit[data[now]] + 1;				
				ansCnt = wayCnt - cycleCnt;
			}

			
			
			wayCnt = 0;
			
			int cur = now;
			int num = 0;
//			while(true) {
//				if(num == cycleCnt) break;
//				ans[cur] = cycleCnt;
//				num++;
//				cur = data[cur];
//			}
		}else {			 
	//		ansCnt += (wayCnt+(cycleCnt-1));
			ansCnt = (wayCnt+(cycleCnt-1));
	
			System.out.println(start+","+now+":"+ansCnt);			
			dfs(data[now], start);
		}
		
		
	}
	
	private static int dfs2(int now, int start, int dist) {
		if(visit[now] > 0) return dist;
		
		wayCnt++;
		count++;
		visit[now] = count;
		
		//now ~ data[now] 가 사이클..
		if(visit[data[now]] >= visit[start]) {
			if(cycleCnt ==0) {
				cycleCnt = visit[now] - visit[data[now]] + 1;				
			}
			dist = wayCnt-cycleCnt;
			wayCnt = 0;
			System.out.println(start+":"+dist);
			return dist;
		}else {			 
			return dfs2(data[now], start, dist);
		}
		
		
	}	
}

```
