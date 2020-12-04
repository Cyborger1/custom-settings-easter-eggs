package com.settingseastereggs;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CustomSettingsEasterEggsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CustomSettingsEasterEggsPlugin.class);
		RuneLite.main(args);
	}
}