package com.settingseastereggs;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(CustomSettingsEasterEggsConfig.CONFIG_GROUP)
public interface CustomSettingsEasterEggsConfig extends Config
{
	String CONFIG_GROUP = "settingseastereggs";
	String EASTER_EGG_REPLACEMENTS_KEY_NAME = "easterEggReplacements";

	@ConfigItem(
		keyName = EASTER_EGG_REPLACEMENTS_KEY_NAME,
		name = "Easter Egg Replacements",
		description = "Format: 'SearchText = Egg', separate entries on new lines."
	)
	default String easterEggReplacements()
	{
		return "I am your father = Nooooooooooooooooooooooooo!" +
			"\nIll be back = Come with me if you want to live.";
	}
}
