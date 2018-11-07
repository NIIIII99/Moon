# 1495

N : play 곡수
S : 최초 볼륨
M : 최대 가능한 볼륨
V[] : 곡당 +,- 가능한 볼륭

dp[i][j] : i곡일때 j 볼륨을 틀수 있다.(1), 틀수 없다(0)

```java
public class Main {
	static int N, S, M;
	static int[] V;
	static int[][] dp;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		S = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		V = new int[N+1];
		st = new StringTokenizer(br.readLine());
		for(int i=1;i<=N;i++) {
			V[i] = Integer.parseInt(st.nextToken());
		}
		
		dp = new int[1001][1001];
		
		dp[0][S] = 1;
		if(S+V[1] <= M) dp[1][S+V[1]] = 1; 
		if(S-V[1] >= 0) dp[1][S-V[1]] = 1;
		
		for(int i=2;i<=N;i++) {
			for(int j=0;j<=M;j++) {
				if(dp[i-1][j] == 1) {
					if(j+V[i] <=M) {
						dp[i][j+V[i]] = 1;
					}
					if(j-V[i] >=0) {
						dp[i][j-V[i]] = 1;
					}
				}
			}
		}
		
		int max = -1;
		for(int j=0;j<=M;j++) {
			if(dp[N][j] == 1) {
				max = Math.max(max, j);
			}			
		}
		
		System.out.println(max);
	}

}

```
