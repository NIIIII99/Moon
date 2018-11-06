# 11066

* MAX_VAL = Integer.MAX_VALUE
* 최대값 부족으로 10번 이상 실패한 문제.

```java
public class Main {
	static int T, N;
	static int[] cost, sum;
	static int[][] dp;
	static final int MAX_VAL = Integer.MAX_VALUE;
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
			dp = new int[501][501];
			//dp MAX
			for(int i=1;i<=N;i++) {
				for(int j=1;j<=N;j++) {
					dp[i][j] = -1;
				}
			}
			
			st = new StringTokenizer(br.readLine());
			for(int i=1;i<=N;i++) {
				cost[i] = Integer.parseInt(st.nextToken());
				sum[i] = sum[i-1]+cost[i];
//				System.out.println(i+":"+sum[i]);
			}
			bw.write(String.valueOf(check(1,N)));
			bw.newLine();
//			System.out.println(check(1,N));

		}
		bw.flush();
		
	}
	private static int check(int i, int j) {
		if(i>=j) return 0;
		
		if(dp[i][j] != -1) {
			return dp[i][j];
		}
		
		if(j == i+1) {
			return dp[i][j] = cost[i]+cost[j];
		}
		
		dp[i][j] = MAX_VAL;
		
		for(int k=i ; k<=j ; k++) {
			int tmp = check(i,k)+check(k+1,j)+sum[j] -sum[i-1];
			dp[i][j]=Math.min(dp[i][j], tmp);
//			dp[i][j] = Math.min(dp[i][j], check(i,k)+check(k+1,j));
		}
//		return dp[i][j] = dp[i][j] + sum[j]-sum[i-1]; 
		return dp[i][j];
	}

}

```
