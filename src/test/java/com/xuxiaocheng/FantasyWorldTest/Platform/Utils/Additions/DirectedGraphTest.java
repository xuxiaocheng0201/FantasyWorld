package com.xuxiaocheng.FantasyWorldTest.Platform.Utils.Additions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions.DirectedGraph;
import com.xuxiaocheng.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DirectedGraphTest {
    @Test
    public void sort() {
        final DirectedGraph<String> graph = new DirectedGraph<>();
        graph.addEdge("1", "2");
        TestUtil.assetsEquals(graph.sort(), List.of("1", "2"));
        graph.addEdge("0", "2");
        graph.addEdge("2", "3");
        graph.addEdge("2", "4");
        TestUtil.assetsEquals(graph.sort(), List.of("0", "1", "2", "4", "3"));
        graph.addEdge("3", "4");
        TestUtil.assetsEquals(graph.sort(), List.of("0", "1", "2", "3", "4"));
    }

    @Test
    public void reverse() {
        final DirectedGraph<String> graph = new DirectedGraph<>();
        graph.addEdge("1", "2");
        graph.addEdge("0", "2");
        graph.addEdge("2", "3");
        graph.addEdge("2", "4");
        graph.addEdge("3", "4");
        TestUtil.assetsEquals(graph.toString(), "DirectedGraph{graph={0=[2], 1=[2], 2=[4, 3], 3=[4]}, inDegree={0=0, 1=0, 2=2, 3=1, 4=2}}");
        TestUtil.assetsEquals(graph.reverse().reverse(), graph);
    }
}
