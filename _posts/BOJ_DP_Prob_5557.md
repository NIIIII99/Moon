# 5557

https://www.acmicpc.net/problem/5557

* 모든 식에 +,- 경우의 수를 따져야하므로 원래는 2의 100제곱승 걸림
* 중간에 나오는 숫자가 0이상 20이하 라는 조건 때문에 계산할 수 있음.
* 경우의 수이므로 전에 나왔던 것과 현재 나오는 것을 더하면 됨.

D[i][j] = D[i-1][j-A[i]] + D[i][j+A[i]]
-> i까지 수를 사용하여 j를 만들 수 있는 경우의 수

```java
public class Main {
	static int N;
	static int[] data;
	static long[][] dp;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		
		data = new int[N+1];
		dp = new long[N+1][21];
		
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		for(int i=1;i<=N;i++) {
			data[i] = Integer.parseInt(st.nextToken());
		}

		dp[1][data[1]] = 1;
		for(int i=2;i<N;i++) {
			for(int j=0;j<=20;j++) {
				if(j-data[i] >=0) {
					dp[i][j] = dp[i][j] + dp[i-1][j-data[i]];
				}
				if(j+data[i]<=20) {
					dp[i][j] = dp[i][j] + dp[i-1][j+data[i]];
				}
			}
		}
		System.out.println(dp[N-1][data[N]]);
		
	}
}

```
