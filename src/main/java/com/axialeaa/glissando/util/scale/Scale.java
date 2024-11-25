package com.axialeaa.glissando.util.scale;

import com.axialeaa.glissando.util.Note;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

public enum Scale {

    IONIAN          (2, 2, 1, 2, 2, 2, 1), // major
    DORIAN          (2, 1, 2, 2, 2, 1, 2),
    PHRYGIAN        (1, 2, 2, 2, 1, 2, 2),
    LYDIAN          (2, 2, 2, 1, 2, 2, 1),
    MIXOLYDIAN      (2, 2, 1, 2, 2, 1, 2),
    AEOLIAN         (2, 1, 2, 2, 1, 2, 2), // natural minor
    LOCRIAN         (1, 2, 2, 1, 2, 2, 2),
    HARMONIC_MAJOR  (2, 2, 1, 2, 1, 3, 1),
    HARMONIC_MINOR  (2, 1, 2, 2, 1, 3, 1),
    MELODIC_MINOR   (2, 1, 2, 2, 2, 2, 1),
    PENTA_MAJOR     (2, 2, 3, 2, 3),
    PENTA_MINOR     (3, 2, 2, 3, 1),
    BLUES           (3, 2, 1, 1, 3, 2);

    private final int[] steps;

    private final Object2ObjectArrayMap<Note, Note[]> notesForKeyMap = new Object2ObjectArrayMap<>();

    Scale(int... steps) {
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
