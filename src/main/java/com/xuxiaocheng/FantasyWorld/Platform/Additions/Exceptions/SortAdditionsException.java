package com.xuxiaocheng.FantasyWorld.Platform.Additions.Exceptions;

import com.xuxiaocheng.FantasyWorld.Platform.Utils.Additions.DirectedGraph;
import org.jetbrains.annotations.Nullable;

import java.io.Serial;
import java.util.Objects;

public class SortAdditionsException extends IllegalAdditionErrorException {
    @Serial
    private static final long serialVersionUID = 3547209905327086028L;

    private static final String DEFAULT_MESSAGE = "Fail to sort additions.";

    public SortAdditionsException(@Nullable final String message) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE));
    }

    public SortAdditionsException(@Nullable final String message, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE), cause);
    }

    public SortAdditionsException(@Nullable final String message, @Nullable final DirectedGraph<?> graph) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE) +
                " graph: " + graph);
    }

    public SortAdditionsException(@Nullable final String message, @Nullable final DirectedGraph<?> graph, @Nullable final Throwable cause) {
        super(Objects.requireNonNullElse(message, SortAdditionsException.DEFAULT_MESSAGE) +
                " graph: " + graph, cause);
    }
}
