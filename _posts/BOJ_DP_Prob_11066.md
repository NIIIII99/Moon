# 11066 FAIL


```java
package DP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Prob_11066 {
	static int T, N;
	static int[] cost, sum;
	static int[][] dp;
	static final int MAX_VAL = 100001;
	//dp[i,j] = dp[i,k]+dp[k,j]+(i~j까지 전체 비용)
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		StringTokenizer st;
		T = Integer.parseInt(br.readLine());
		for(int t=1;t<=T;t++) {
			N = Integer.parseInt(br.readLine());
			cost = new int[N+1];
			sum = new int[N+1];
			dp = new int[N+1][N+1];
			//dp MAX
			for(int i=0;i<=N;i++) {
				Arrays.fill(dp[i], MAX_VAL);
			}
			
			st = new StringTokenizer(br.readLine());
			for(int i=1;i<=N;i++) {
				cost[i] = Integer.parseInt(st.nextToken());
				sum[i] = sum[i-1]+cost[i];
			}
			bw.write(String.valueOf(check(1,N)));
			bw.newLine();
//			System.out.println(check(1,N));

		}
		bw.flush();
		
	}
	private static int check(int i, int j) {
		if(i==j) return 0;
		
		if(j == i+1) {
			return dp[i][j] = cost[i]+cost[j];
		}
		
		if(dp[i][j] < MAX_VAL) {
			return dp[i][j];
		}		
		
		for(int k=i ; k<j ; k++) {
			dp[i][j] = Math.min(dp[i][j], check(i,k)+check(k+1,j));
		}
		return dp[i][j] = dp[i][j] + sum[j]-sum[i-1]; 
	}

}

```
