package net.mehvahdjukaar.supplementaries;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.mehvahdjukaar.supplementaries.client.screens.widgets.ISlider;
import net.minecraft.network.chat.Component;

public class SuppClientPlatformStuff {

    @ExpectPlatform
    public static ISlider createSlider(int x, int y, int width, int height, Component prefix, Component suffix, double minValue, double maxValue,
                                       double currentValue, double stepSize, int precision, boolean drawString) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean hasFixedAO() {
        throw new AssertionError();
    }

}
