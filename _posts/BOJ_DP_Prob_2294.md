# 2294
동전 N개의 숫자를 입력받아서 K 숫자를 만들때 사용하는 최소 동전 갯수
https://www.acmicpc.net/problem/2294

```java
package DP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Prob_2294 {
	static int N, K;
	static int[] data;
	static int[] dp; 
	static final int INF = 1000000;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		data = new int[N];
		for(int i=0;i<N;i++) {
			data[i] = Integer.parseInt(br.readLine());
		}
		dp = new int[K+1];
		
		for(int i=1;i<=K;i++) {
			dp[i] = INF;
			for(int j=0;j<N;j++) {
				int before = i-data[j];
				if(before >= 0 && dp[before] != INF) {
					dp[i] = Math.min(dp[i], dp[before]+1);
				}				
			}
		}
		
		if(dp[K] == INF) {
			System.out.println("-1");
		}else {
			System.out.println(dp[K]);
		}
	}
}
```
