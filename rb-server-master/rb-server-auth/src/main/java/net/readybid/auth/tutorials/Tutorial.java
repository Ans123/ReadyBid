package net.readybid.auth.tutorials;

import java.util.ArrayList;
import java.util.List;

public enum Tutorial {
    FillResponseOnlyOnce;

    public static List<String> asList() {
        final List<String> tutorials = new ArrayList<>();
        for(Tutorial t : Tutorial.values()){
            tutorials.add(String.valueOf(t));
        }
        return tutorials;
    }
}
