package Daikstra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringTokenizer;

class Tank{
	int x, y, cost;
	public Tank(int x, int y, int cost) {
		this.x = x;
		this.y = y;
		this.cost = cost;
	}

}
public class Prob_0058_mine {
	static int T, N, firstIdx, base;
	static Tank[] tank;
	static long[] tree;
	static final int MAX = 4000000;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st;
		T = Integer.parseInt(br.readLine());
		for(int t =1;t<=T;t++) {
			N = Integer.parseInt(br.readLine());
			for(base = 1; base<=1000000;base*=2);
			tank = new Tank[N+1];
			for(int i=1;i<=N;i++) {
				st = new StringTokenizer(br.readLine());
				int x = Integer.parseInt(st.nextToken());
				int y = Integer.parseInt(st.nextToken());
				int cost = Integer.parseInt(st.nextToken());

				tank[i] = new Tank(x, y, cost);
			}
			Arrays.sort(tank, 1, N+1, new Comparator<Tank>() {

				@Override
				public int compare(Tank o1, Tank o2) {
					if(o1.x -o2.x >0) {
						return -1;
					}else if(o1.x - o2.x < 0) {
						return 1;
					}else {
						return 0;
					}
				}

			});

			tree = new long[base+1000002];
			long rslt = 0;
//			firstIdx = findIndex();
//			firstIdx--;
			for(int i=1;i<=N;i++) {
				update(tank[i].y+base-1, tank[i].cost);
				rslt += query(tank[i].y+base, base+1000001 );
			}
//			for(int i=2;i<=N;i++) {
//				tree=new long[4000000];
//				for(int j=i;j<=N;j++) {
//					update(tank[j].y+firstIdx, tank[j].cost);
//				}
//				rslt += query(tank[i-1].y+firstIdx, firstIdx+N);
//			}
			System.out.println("#"+t+" "+rslt);
		}
	}
	private static long query(int s, int e) {
		long rslt = 0;
		while(s<=e) {
			if(s%2 == 1) rslt += tree[s];
			if(e%2 == 0) rslt += tree[e];
			s = (s+1)/2;
			e = (e-1)/2;
		}
		return rslt;
	}
	private static void update(int idx, int val) {
		int idxTmp = idx;
		while(idxTmp >0) {
			tree[idxTmp] = tree[idxTmp]+val;
			idxTmp = idxTmp/2;
		}

	}
	private static int findIndex() {
		int idx =1;
		while(idx<N) {
			idx = idx*2;
		}
		return idx;
	}
}
