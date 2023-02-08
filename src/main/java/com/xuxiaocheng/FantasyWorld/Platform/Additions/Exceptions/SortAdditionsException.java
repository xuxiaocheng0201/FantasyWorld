package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions.DirectedGraph;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class SortAdditionsException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = 3547209905327086028L;

    private static final String DEFAULT_MESSAGE = "Fail to sort additions.";

    public SortAdditionsException(final @Nullable String message) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE));
    }

    public SortAdditionsException(final @Nullable String message, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE), cause);
    }

    public SortAdditionsException(final @Nullable String message, final @Nullable DirectedGraph<?> graph) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE) +
                " graph: " + graph);
    }

    public SortAdditionsException(final @Nullable String message, final @Nullable DirectedGraph<?> graph, final @Nullable Throwable cause) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE) +
                " graph: " + graph, cause);
    }
}
