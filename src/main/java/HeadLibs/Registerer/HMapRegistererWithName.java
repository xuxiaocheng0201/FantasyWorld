package HeadLibs.Registerer;

/**
 * Elements registerer with name.
 * @param <E> The type of elements
 * @author xuxiaocheng
 */
@SuppressWarnings("unused")
public class HMapRegistererWithName<E> extends HMapRegisterer<String, E> {
    /**
     * Register a new element.
     * @param name element's name
     * @param element the element
     * @throws HElementRegisteredException element or its name has been registered.
     */
    @Override
    public void register(String name, E element) throws HElementRegisteredException {
        if (this.map.containsKey(name))
            throw new HElementRegisteredException("Registered name.", name, element);
        if (this.map.containsValue(element))
            throw new HElementRegisteredException("Registered element.", name, element);
        this.map.put(name, element);
    }

    /**
     * Deregister element by name.
     * @param name element's name
     */
    public void deregisterByName(String name) {
        this.map.remove(name);
    }
}
