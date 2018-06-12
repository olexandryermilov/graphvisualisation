package сontroller;

import model.HungaryGraph;

import java.util.*;

public class HungarySolver {
    HungaryGraph hungaryGraph;

    public HungarySolver(HungaryGraph hungaryGraph) {
        this.hungaryGraph=hungaryGraph;
    }

    private final static int INF = 100000000;

    public void solve() {
        int size = hungaryGraph.size;
        int[][] a = hungaryGraph.adjacencyMatrix;
        List<Integer> u = new ArrayList<>();
        List<Integer> v = new ArrayList<>();
        List<Integer> p = new ArrayList<>();
        List<Integer> way = new ArrayList<>();
        for (int i = 0; i <= size; i++) {
            u.add(0);
            v.add(0);
            p.add(0);
            way.add(0);
        }
        for (int i = 1; i <= size; ++i) {
            p.set(0,i);
            int j0 = 0;
            List<Integer> minv = new ArrayList<>();
            for (int qq = 0; qq <= size; qq++) minv.add(INF);
            List<Boolean> used = new ArrayList<>();
            for (int qq = 0; qq <= size; qq++) used.add(false);
            do {
                used.set(j0, true);
                int i0 = p.get(j0), delta = INF, j1=0;
                for (int j = 1; j <= size; ++j)
                    if (!used.get(j)) {
                        int cur = a[i0][j] - u.get(i0) - v.get(j);
                        if (cur < minv.get(j)) {
                            minv.set(j, cur);
                            way.set(j, j0);
                        }
                        if (minv.get(j) < delta) {
                            delta = minv.get(j);
                            j1 = j;
                        }
                    }
                for (int j = 0; j <= size; ++j)
                    if (used.get(j)) {
                        u.set(p.get(j), u.get(p.get(j)) + delta);
                        v.set(j, v.get(j)-delta);
                    } else
                        minv.set(j,minv.get(j) - delta);
                j0 = j1;
            } while (p.get(j0) != 0);
            do {
                int j1 = way.get(j0);
                p.set(j0,p.get(j1));
                j0 = j1;
            } while (j0>0);
        }
        int[] ans = new int[size+1];
        for (int j = 1; j <= size; ++j)
            ans[p.get(j)] = j;
        ArrayList<Integer> anss= new ArrayList<>();
        for(int i=1;i<=size;i++){
            anss.add(ans[i]);
        }
        int cost = -v.get(0);
        anss.forEach(integer -> System.out.println("Worker #"+(anss.indexOf(integer)+1)+" should take job #"+integer));
        System.out.println("Cost is " + cost);
    }
}
