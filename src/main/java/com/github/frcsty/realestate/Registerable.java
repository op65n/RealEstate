package com.github.frcsty.realestate;

public interface Registerable {

    void register(final RealEstatePlugin plugin);

    default void unregister(final RealEstatePlugin plugin) {}

}
