package HeadLibs.Registerer;

import java.util.Collection;
import java.util.HashSet;

/**
 * Elements registerer.
 * @param <T> The type of elements
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HSetRegisterer<T> {
    /**
     * The registered elements' set.
     */
    protected final Collection<T> set = new HashSet<>();

    /**
     * Register a new element.
     * @param element the element
     * @throws HElementRegisteredException element has been registered.
     */
    public void register(T element) throws HElementRegisteredException {
        if (this.set.contains(element))
            throw new HElementRegisteredException("Registered element.", element);
        this.set.add(element);
    }

    /**
     * Deregister element.
     * @param element the element
     */
    public void deregister(T element) {
        this.set.remove(element);
    }

    /**
     * Deregister all elements.
     */
    public void deregisterAll() {
        this.set.clear();
    }

    /**
     * If a element has been registered.
     * @param element the element
     * @return true - registered.
     */
    public boolean isRegistered(T element) {
        return this.set.contains(element);
    }

    /**
     * Get the count of registered elements.
     * @return the count of registered elements.
     */
    public int getRegisteredCount() {
        return this.set.size();
    }

    /**
     * Get registerer set. {@link HSetRegisterer#set}
     * @return registerer set
     */
    public Collection<T> getSet() {
        return this.set;
    }
}
