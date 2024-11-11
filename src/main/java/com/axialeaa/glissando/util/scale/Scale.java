package com.axialeaa.glissando.util.scale;

import com.axialeaa.glissando.util.Note;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

public enum Scale {

    IONIAN          ("ionian",              2, 2, 1, 2, 2, 2, 1), // major
    DORIAN          ("dorian",              2, 1, 2, 2, 2, 1, 2),
    PHRYGIAN        ("phrygian",            1, 2, 2, 2, 1, 2, 2),
    LYDIAN          ("lydian",              2, 2, 2, 1, 2, 2, 1),
    MIXOLYDIAN      ("mixolydian",          2, 2, 1, 2, 2, 1, 2),
    AEOLIAN         ("aeolian",             2, 1, 2, 2, 1, 2, 2), // natural minor
    LOCRIAN         ("locrian",             1, 2, 2, 1, 2, 2, 2),
    HARMONIC_MAJ    ("harmonic_major",      2, 2, 1, 2, 1, 3, 1),
    HARMONIC_MIN    ("harmonic_minor",      2, 1, 2, 2, 1, 3, 1),
    MELODIC_MIN     ("melodic_minor",       2, 1, 2, 2, 2, 2, 1),
    PENTA_MAJ       ("pentatonic_major",    2, 2, 3, 2, 3),
    PENTA_MIN       ("pentatonic_minor",    3, 2, 2, 3, 1),
    BLUES           ("blues",               3, 2, 1, 1, 3, 2);

    private final String name;
    private final int[] steps;

    private final Object2ObjectArrayMap<Note, Note[]> notesForKeyMap = new Object2ObjectArrayMap<>();

    Scale(String name, int... steps) {
        this.name = name;
        this.steps = steps;
    }

    public ObjectArrayList<Note> getNotesInKey(Note key) {
        if (this.notesForKeyMap.containsKey(key))
            return new ObjectArrayList<>(this.notesForKeyMap.get(key));

        List<Note> notes = new ArrayList<>(List.of(key));
        Note note = key;

        for (int step : this.steps) {
            int ordinal = (note.ordinal() + step) % 12;
            note = Note.values()[ordinal];

            notes.add(note);
        }

        Note[] array = this.notesForKeyMap.put(key, notes.toArray(Note[]::new));

        return array == null ? new ObjectArrayList<>() : new ObjectArrayList<>(array);
    }

}
