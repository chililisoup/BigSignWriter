package dev.chililisoup.bigsignwriter.font.supplier;

import com.google.common.collect.ImmutableSet;
import dev.chililisoup.bigsignwriter.font.FontFile;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractFontSupplier {
    public abstract FontFile get();

    public Map<Character, Set<PatchCharacter>> patches() {
        HashMap<Character, Set<PatchCharacter>> baseMap = new HashMap<>();
        this.appendRecolourfulContainersGuiFix(baseMap);
        return baseMap;
    }

    protected final void appendStringReplacement(HashMap<Character, Set<PatchCharacter>> baseMap, String from, String to) {
        this.get().characters.forEach((character, lines) -> {
            if (!String.join("", lines).contains(to)) return;

            baseMap.merge(
                    character,
                    Set.of(PatchCharacter.of(
                            Arrays.stream(lines)
                                    .map(line -> line.replace(to, from))
                                    .toArray(String[]::new)
                    )),
                    (baseSet, fixSet) -> {
                        ImmutableSet.Builder<@NotNull PatchCharacter> builder = ImmutableSet.builder();
                        builder.add(baseSet.toArray(PatchCharacter[]::new));
                        builder.add(fixSet.toArray(PatchCharacter[]::new));
                        return builder.build();
                    }
            );
        });
    }

    protected final void appendRecolourfulContainersGuiFix(HashMap<Character, Set<PatchCharacter>> baseMap) {
        this.appendStringReplacement(baseMap, " ", " ");
    }

    protected final void appendMediumVerticalBarAnnihilator(HashMap<Character, Set<PatchCharacter>> baseMap) {
        this.appendStringReplacement(baseMap, "❙", "𜷴");
    }

    public record PatchCharacter(String... lines) {
        static PatchCharacter of(String... lines) {
            return new PatchCharacter(lines);
        }

        @Override
        public boolean equals(Object other) {
            // doesn't work on java 17
            // noinspection DeconstructionCanBeUsed
            if (!(other instanceof PatchCharacter otherCharacter)) return false;
            if (otherCharacter.lines.length != this.lines.length) return false;

            for (int i = 0; i < lines.length; i++)
                if (!lines[i].equals(otherCharacter.lines[i])) return false;

            return true;
        }
    }
}
