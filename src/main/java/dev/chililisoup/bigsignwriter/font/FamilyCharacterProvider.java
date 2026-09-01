package dev.chililisoup.bigsignwriter.font;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface FamilyCharacterProvider {
    Map<Character, String[]> characters();

    boolean parentIsImplicit();

    @Nullable FamilyCharacterProvider parentFont();
}
