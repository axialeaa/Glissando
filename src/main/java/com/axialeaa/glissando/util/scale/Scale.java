package com.axialeaa.glissando.util.scale;

import com.axialeaa.glissando.util.Note;
import com.google.common.collect.Maps;

import java.util.*;

import static com.axialeaa.glissando.util.GlissandoConstants.*;

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
    private final EnumMap<Note, Note[]> notesForKeyMap = Maps.newEnumMap(Note.class);

    Scale(int... steps) {
        this.steps = steps;

        for (Note key : Note.values())
            this.putNotesForKey(key);
    }

    public Note[] getNotesInKey(Note key) {
        return this.notesForKeyMap.get(key);
    }

    private void putNotesForKey(Note key) {
        List<Note> notes = new ArrayList<>(List.of(key));
        Note note = key;

        for (int step : this.steps) {
            int ordinal = (note.ordinal() + step) % NOTES_IN_OCTAVE;
            note = Note.values()[ordinal];

            notes.add(note);
        }

        Note[] notesArray = notes.toArray(Note[]::new);
        this.notesForKeyMap.put(key, notesArray);
    }

}
