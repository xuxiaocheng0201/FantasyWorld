package HeadLibs.DataStructures;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Basic binary tree.
 * @param <T> the type of data
 */
@SuppressWarnings("unused")
public class Tree<T> {
    private @Nullable T data;
    private @Nullable Tree<T> leftChild;
    private @Nullable Tree<T> rightChild;

    public Tree(@Nullable T data) {
        super();
        this.data = data;
    }

    public Tree(@Nullable T data, @Nullable Tree<T> leftChild, @Nullable Tree<T> rightChild) {
        super();
        this.data = data;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public @Nullable T getData() {
        return this.data;
    }

    public void setData(@Nullable T data) {
        this.data = data;
    }

    public @Nullable Tree<T> getLeftChild() {
        return this.leftChild;
    }

    public void setLeftChild(@Nullable Tree<T> leftChild) {
        this.leftChild = leftChild;
    }

    public @Nullable Tree<T> getRightChild() {
        return this.rightChild;
    }

    public void setRightChild(@Nullable Tree<T> rightChild) {
        this.rightChild = rightChild;
    }

    @Override
    public @NotNull String toString() {
        return "Tree{" +
                "date=" + (this.data == null ? "null" : this.data) +
                ", leftChild=" + (this.leftChild == null ? "null" : this.leftChild) +
                ", rightChild=" + (this.rightChild == null ? "null" : this.rightChild) +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Tree<?> tree = (Tree<?>) o;
        return Objects.equals(this.data, tree.data) && Objects.equals(this.leftChild, tree.leftChild) && Objects.equals(this.rightChild, tree.rightChild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.data, this.leftChild, this.rightChild);
    }
}
