package net.readybid.app.entities;

public interface PrimitivesHelper {

    static boolean unboxSafely(Boolean b){
        return b != null && b.equals(Boolean.TRUE);
    }

    static String formatInteger(int i) {
        return Integer.toString(i);
    }

}
