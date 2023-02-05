package com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/*
Note that {@code DirectedGraph} is not synchronized.
 */
public class DirectedGraph<T> implements Iterable<T> {
    private final @NotNull Multimap<@NotNull T, @NotNull T> graph = HashMultimap.create();
    private final @NotNull Map<@NotNull T, @NotNull Integer> inDegree = new HashMap<>();

    public DirectedGraph() {
        super();
    }

    public DirectedGraph(@Nullable final DirectedGraph<T> graph) {
        super();
        if (graph == null)
            return;
        this.graph.putAll(graph.graph);
        this.inDegree.putAll(graph.inDegree);
    }

    public void addNode(@NotNull final T node) {
        if (this.inDegree.containsKey(node))
            return;
        this.inDegree.put(node, 0);
    }

    public boolean isNodeExists(@NotNull final T node) {
        return this.inDegree.containsKey(node);
    }

    public void addEdge(@NotNull final T from, @NotNull final T to) {
        this.addNode(from);
        this.addNode(to);
        this.graph.put(from, to);
        final int in = this.inDegree.get(to).intValue() + 1;
        this.inDegree.put(to, in);
    }

    public boolean isEdgeExists(@NotNull final T from, @NotNull final T to) {
        if (!this.graph.containsKey(from))
            return false;
        return this.graph.get(from).contains(to);
    }

    public @NotNull @UnmodifiableView Set<T> listEdgesFrom(@NotNull final T from) {
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

    @Override
    public @NotNull Iterator<T> iterator() {
        return this.inDegree.keySet().iterator();
    }

    @Override
    public boolean equals(@Nullable final Object o) {
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
                "graph=" + this.graph +
                ", inDegree=" + this.inDegree +
                '}';
    }

    // BFS
    public @NotNull List<T> sort() {
        final Map<T, Integer> inDegree = new HashMap<>(this.inDegree);
        final List<T> result = new ArrayList<>(this.inDegree.size());
        final Deque<T> queue = new ArrayDeque<>();
        for (final Map.Entry<T, Integer> entry: inDegree.entrySet())
            if (entry.getValue().intValue() == 0)
                queue.add(entry.getKey());
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
}
