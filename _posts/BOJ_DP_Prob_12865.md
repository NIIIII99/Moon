# 12865

	* D[i][j] = i번째 물건까지 고려, 배낭무게합이 j 일때 가치 최대값
	* i번째 물건을 가방에 넣지않음 -> D[i-1][j]
	* i번째 물건을 가방에 넣음 -> D[i-1][j-w[i]] + v[i]
  
  * 주의사항 : w와 v를 1부터 인덱스 시작. i, j를 1부터 인덱스 시작. for문의 범위...

```java
public class Main {
	//D[i][j] = i번째 물건까지 고려, 배낭무게합이 j 일때 가치 최대값
	//i번째 물건을 가방에 넣지않음 -> D[i-1][j]
	//i번째 물건을 가방에 넣음 -> D[i-1][j-w[i]] + v[i]
	static int N, K;
	static int[] w;
	static int[] v;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		w = new int[N+1];
		v = new int[N+1];
		for(int i=1;i<=N;i++) {
			st = new StringTokenizer(br.readLine());
			w[i] = Integer.parseInt(st.nextToken());
			v[i] = Integer.parseInt(st.nextToken());
		}
		
		int[][] dp = new int[N+1][K+1];
		dp[0][0] = 0;
		for(int i=1;i<=N;i++) {
			for(int j=1;j<=K;j++) {
				dp[i][j] = dp[i-1][j];
				if(j-w[i]>=0) {
					dp[i][j] = Math.max(dp[i][j], dp[i-1][j-w[i]]+v[i]);					
				}
			}
		}
		System.out.println(dp[N][K]);
		
	}
}

```
