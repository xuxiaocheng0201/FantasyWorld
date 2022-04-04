package HeadLibs;

import HeadLibs.Helper.HStringHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@SuppressWarnings("unused")
public class Tree<T> {
    private @Nullable T date;
    private @Nullable Tree<T> leftChild;
    private @Nullable Tree<T> rightChild;

    public Tree(@Nullable T date) {
        super();
        this.date = date;
    }

    public Tree(@Nullable T date, @Nullable Tree<T> leftChild, @Nullable Tree<T> rightChild) {
        super();
        this.date = date;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public @Nullable T getDate() {
        return this.date;
    }

    public void setDate(@Nullable T date) {
        this.date = date;
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
    public String toString() {
        return HStringHelper.concat("Tree{",
                "date=", this.date,
                ", leftChild=", this.leftChild,
                ", rightChild=", this.rightChild,
                '}');
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Tree<?> tree = (Tree<?>) o;
        return Objects.equals(this.date, tree.date) && Objects.equals(this.leftChild, tree.leftChild) && Objects.equals(this.rightChild, tree.rightChild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.date, this.leftChild, this.rightChild);
    }
}
