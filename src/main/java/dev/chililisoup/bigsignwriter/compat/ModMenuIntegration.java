//? if fabric {
package dev.chililisoup.bigsignwriter.compat;

import com.terraformersmc.modmenu.api.ModMenuApi;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import dev.chililisoup.bigsignwriter.gui.config.BigSignWriterConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return BigSignWriterConfigScreen::new;
    }
}
//?}