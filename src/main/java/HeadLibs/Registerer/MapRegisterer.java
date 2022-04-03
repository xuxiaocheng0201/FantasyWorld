package HeadLibs.Registerer;

import java.util.HashMap;
import java.util.Map;

/**
 * Elements registerer with name.
 * @param <E> The type of elements
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class MapRegisterer<E> {
    /**
     * The registered elements' map.
     */
    protected final Map<String, E> map = new HashMap<>();

    /**
     * Register a new element.
     * @param name element's name
     * @param element the element
     * @throws ElementRegisteredException element or its name has been registered.
     */
    public void register(String name, E element) throws ElementRegisteredException {
        if (this.map.containsKey(name))
            throw new ElementRegisteredException("Registered name.", name, element);
        if (this.map.containsValue(element))
            throw new ElementRegisteredException("Registered element.", name, element);
        this.map.put(name, element);
    }

    /**
     * Deregister element by name.
     * @param name element's name
     */
    public void deregisterByName(String name) {
        this.map.remove(name);
    }

    /**
     * Deregister element.
     * @param element the element
     */
    public void deregister(E element) {
        if (!this.map.containsValue(element))
            return;
        for (Map.Entry<String, E> entry : this.map.entrySet())
            if (entry.getValue().equals(element))
                this.map.remove(entry.getKey());
    }

    /**
     * Deregister all elements.
     */
    public void deregisterAll() {
        this.map.clear();
    }

    /**
     * If a element's name has been registered.
     * @param name element's name
     * @return true - registered.
     */
    public boolean isRegistered(String name) {
        return this.map.containsKey(name);
    }

    /**
     * If a element has been registered.
     * @param element the element
     * @return true - registered.
     */
    public boolean isRegistered(E element) {
        return this.map.containsValue(element);
    }

    /**
     * Get a registered element by name.
     * @param name element's name
     * @return the registered element
     * @throws ElementNotRegisteredException No element with this name registered.
     */
    public E getElement(String name) throws ElementNotRegisteredException {
        if (!this.map.containsKey(name))
            throw new ElementNotRegisteredException(null, name);
        return this.map.get(name);
    }

    /**
     * Get the count of registered elements.
     * @return the count of registered elements.
     */
    public int getRegisteredCount() {
        return this.map.size();
    }

    /**
     * Get registerer map. {@link MapRegisterer#map}
     * @return registerer map
     */
    public Map<String, E> getMap() {
        return this.map;
    }
}
