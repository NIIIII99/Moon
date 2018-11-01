# 11058
크리보드. 4개의 버튼을 총 N번만 눌러서 출력된 A의 최대 개수
1. A출력
2. Ctrl+A
3. Ctrl+C
4. Ctrl+V

* 주의할점
N의 최대값이 100으로 결과값이 integer 범위를 넘는다.

* 문제 : https://www.acmicpc.net/problem/11058


```java
package DP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Prob_11058 {
	//D[i] = max(D[i-1]+1,D[i-(j+2)]*(j+1)])
	static int N;
	static long[] dp;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());

		dp = new long[101];
		dp[1] = 1;
		dp[2] = 2;
		for(int i=3;i<=N;i++) {
			for(int j=1;j<i-1;j++) {
				long max = 0;
				max = getMax(dp[i-1]+1, dp[i-(j+2)]*(j+1));
				dp[i] = getMax(max, dp[i]);
			}
		}
		System.out.println(dp[N]);
	}

	private static long getMax(long a, long b) {
		return (a>b ? a : b);
	}
}

```
