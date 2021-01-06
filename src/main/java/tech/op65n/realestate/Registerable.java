package tech.op65n.realestate;

public interface Registerable {

    void register(final RealEstatePlugin plugin);

    default void unregister(final RealEstatePlugin plugin) {}

}
