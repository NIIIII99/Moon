# ddd
##

1 1 0 0 #1 0
34 28 5 4 #2 10
0 9 5 5 #3 0
8 2 5 5 #4 212
5 0 2 1 #5 8597 **
4 22 3 1 #6 3907 ** 7453
20 16 0 5 #7 493521 **
18 1 4 2 #8 784 
23 14 2 4 #9 36465 **
18 26 3 1 #10 1868 ** 4232
8 20 0 1 #11 3252 
26 16 0 0 #12 728 
26 16 0 2 #13 20888 ** 130050
20 29 0 1 #14 3609 ** 7005
32 5 1 5 #15 75224 ** 800998
35 3 5 1 #16 601 ** 693
34 27 0 5 #17 96937 ** 1536353
31 0 2 1 #18 6805 ** 12801
8 6 5 5 #19 33
15 2 1 2 #20 78121 **

20
01/01 00:00:00
10/22 02:52:48
09/13 18:54:59
12/28 12:50:52
10/24 01:25:17
04/22 22:32:18
09/01 05:02:55
05/28 19:49:26
12/12 13:25:47
11/07 21:30:11
07/31 09:04:13
09/21 05:02:05
09/01 05:02:25
06/01 20:03:19
09/17 20:12:51
08/16 12:55:19
08/12 18:01:58
10/14 01:25:17
09/22 17:52:58
07/24 19:12:27

#1 0
#2 10
#3 0
#4 212
#5 11251
#6 7453
#7 878385
#8 784
#9 309781
#10 4232
#11 3252
#12 728
#13 130050
#14 7005
#15 800998
#16 693
#17 1536353
#18 12801
#19 33
#20 204663

```
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
```

```java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main {
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

```
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;




class Main {

 
 static int[] sum = new int[100000+1] ;
 static int[] node,  pathValue, loopArr; 
 static int[] visit ;
 static int T, N , Loop;
 
 public static void main(String[] args) throws Exception{
  // TODO Auto-generated method stub
  
  
  for(int j=1 ; j<=100000 ; j++) {
   if(j==1) sum[j] = j ;
   else sum[j] = sum[j-1]+j ;
  }
  
  BufferedReader br = new BufferedReader(new InputStreamReader(System.in)) ;
  StringTokenizer st = new StringTokenizer(br.readLine());
  
  T = Integer.parseInt(st.nextToken()) ;
  
  for(int t=1 ; t<=T ; t++) {
   st = new StringTokenizer(br.readLine());
   N = Integer.parseInt(st.nextToken()) ;
   node = new int[N+1] ;
   
   pathValue = new int[N+1] ;
   st = new StringTokenizer(br.readLine());
   for(int j=1; j<=N ; j++) node[j] = Integer.parseInt(st.nextToken()) ;
   
   searchB(t) ;
   
   
  }
 }
    public static void searchB(int tcase) { 
  
  loopArr = new int[N+1];
  visit = new int[N+1] ;
  int cycleNum = 0 ;
  
  
  for(int j=1; j<=N ; j++) {
   
   if(visit[j]>0) continue ;   // 방문했으면 아래 로직에서 갈수 있는 거리 저장했으니 패스
   
   Loop = 0 ;   
   visit[j]=1;
   cycleNum= find(j) ;      // 이미 방문한 배열 구하기
   
   if(pathValue[cycleNum] > 0) pathValue[j]= Loop + pathValue[cycleNum] +1;
   else pathValue[j] = Loop ;
   
   if(cycleNum == j) {
    for(int i=0 ; i<Loop ; i++) pathValue[loopArr[i]] = pathValue[j] ;   // 시작과 동일한 사이클이면(즉 원형 사이클) 전부 같은 값 저장
   } else {
    for(int i=0 ; i<Loop ; i++) {
     if(pathValue[loopArr[i]]==0) pathValue[loopArr[i]] = pathValue[j]-(i+1) ;   
     
     if(loopArr[i] == cycleNum) {
      for(int k=(i+1) ; k<Loop ; k++) 
       if(pathValue[loopArr[k]]==0) pathValue[loopArr[k]] = pathValue[loopArr[i]] ;

     break ;      


     }
    }
   }   
  }
  
  long ans =0 ;
  for(int j=1; j<=N ; j++) ans += sum[pathValue[j]] ;
  System.out.println("#"+tcase+" "+ans) ;
 }
 
   
 
 public static int find(int s) {




        int newN = node[s] ;
        
        if(visit[newN] == 1) {
         return newN ;
        }
        else {
         visit[newN]=1;
         loopArr[Loop++] = newN ;
         return find(newN) ;
        }
  
 }
}
```
