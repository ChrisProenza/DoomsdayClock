package com.kcstudios.doomsdayclock;

public class LocationPair {
    Integer First;
    Integer Second;
    LocationPair(Integer x, Integer y) {
        First = x;
        Second = y;
    }
    LocationPair(Float x, Float y) {
        First = x.intValue();
        Second = y.intValue();
    }
}
