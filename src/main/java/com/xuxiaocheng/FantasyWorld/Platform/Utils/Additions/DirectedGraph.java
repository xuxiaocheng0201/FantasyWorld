package com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
Note that {@code DirectedGraph} is not synchronized.
 */
public class DirectedGraph<T> {
    protected final @NotNull Multimap<@NotNull T, @NotNull T> graph = HashMultimap.create();
    protected final @NotNull Map<@NotNull T, @NotNull Integer> inDegree = new HashMap<>();

    public DirectedGraph() {
        super();
    }

    public DirectedGraph(final @Nullable DirectedGraph<T> graph) {
        super();
        if (graph == null)
            return;
        this.graph.putAll(graph.graph);
        this.inDegree.putAll(graph.inDegree);
    }

    public void addNode(final @NotNull T node) {
        this.inDegree.putIfAbsent(node, 0);
    }

    public boolean isNodeExists(final @NotNull T node) {
        return this.inDegree.containsKey(node);
    }

    public void addEdge(final @NotNull T from, final @NotNull T to) {
        this.addNode(from);
        this.addNode(to);
        this.graph.put(from, to);
        this.inDegree.put(to, this.inDegree.get(to).intValue() + 1);
    }

    public boolean isEdgeExists(final @NotNull T from, final @NotNull T to) {
        if (!this.graph.containsKey(from))
            return false;
        return this.graph.get(from).contains(to);
    }

    @NotNull
    public @UnmodifiableView Set<T> listEdgesFrom(final @NotNull T from) {
        if (!this.graph.containsKey(from))
            return Set.of();
        return Collections.unmodifiableSet((Set<T>) this.graph.get(from));
    }

    public int size() {
        return this.inDegree.size();
    }

    public boolean isEmpty() {
        return this.inDegree.isEmpty();
    }

    public @NotNull DirectedGraph<T> reverse() {
        final DirectedGraph<T> that = new DirectedGraph<>();
        for (final T node: this.inDegree.keySet())
            that.addNode(node);
        for (final Map.Entry<T, T> entry: this.graph.entries())
            that.addEdge(entry.getValue(), entry.getKey());
        return that;
    }

    // BFS
    public @NotNull List<T> sort() {
        final Map<T, Integer> inDegree = new HashMap<>(this.inDegree);
        final List<T> result = new ArrayList<>(this.inDegree.size());
        final Deque<T> queue = new LinkedList<>(inDegree.entrySet().stream()
                .filter(entry -> entry.getValue().intValue() == 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toUnmodifiableSet()));
        while (!queue.isEmpty()) {
            final T node = queue.poll();
            result.add(node);
            for (final T to: this.graph.get(node)) {
                final int in = inDegree.get(to).intValue() - 1;
                inDegree.put(to, in);
                if (in == 0)
                    queue.add(to);
            }
        }
        return this.inDegree.size() == result.size() ? result : List.of();
    }

    @Override
    public boolean equals(final @Nullable Object o) {
        if (this == o) return true;
        if (!(o instanceof final DirectedGraph<?> that)) return false;
        return this.graph.equals(that.graph) && this.inDegree.equals(that.inDegree);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.graph, this.inDegree);
    }

    @Override
    public @NotNull String toString() {
        return "DirectedGraph{" +
                "node=" + this.inDegree.keySet() +
                ", edge=" + this.graph +
                '}';
    }
}
