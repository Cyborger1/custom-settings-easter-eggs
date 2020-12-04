package com.settingseastereggs;

import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Custom Settings Easter Eggs",
	tags = {"Custom", "Settings", "Easter Egg", "Secret"}
)
public class CustomSettingsEasterEggsPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CustomSettingsEasterEggsConfig config;

	@Provides
	CustomSettingsEasterEggsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CustomSettingsEasterEggsConfig.class);
	}

	private final Map<String, String> easterMap = new HashMap<>();

	private static final String NO_RESULT_STRING = "<br><br>Could not find any settings that match what you are looking for.<br><br>Try searching for something else.";

	@Override
	protected void startUp() throws Exception
	{
		loadEasterMap(config.easterEggReplacements());
	}

	@Override
	protected void shutDown() throws Exception
	{
		easterMap.clear();
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired event)
	{
		if (event.getScriptId() == 3876)
		{
			Widget search = client.getWidget(134, 10);
			Widget result = client.getWidget(134, 17);
			if (search != null && result != null)
			{
				Widget resultChild = result.getChild(0);
				if (resultChild != null && resultChild.getText().equals(NO_RESULT_STRING))
				{
					String text = Text.removeTags(search.getText());
					text = text.substring(0, text.length() - 1).toLowerCase();

					String egg = easterMap.get(text);
					if (egg != null)
					{
						resultChild.setText(egg);
						resultChild.setOriginalHeight(20);
						resultChild.setOriginalWidth(20);
						resultChild.setXPositionMode(1);
						resultChild.setYPositionMode(1);
						resultChild.revalidate();
					}
				}
			}
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals(CustomSettingsEasterEggsConfig.CONFIG_GROUP)
			&& event.getKey().equals(CustomSettingsEasterEggsConfig.EASTER_EGG_REPLACEMENTS_KEY_NAME))
		{
			String newVal = event.getNewValue();
			if (!newVal.equals(event.getOldValue()))
			{
				loadEasterMap(newVal);
			}
		}
	}

	private void loadEasterMap(String str)
	{
		easterMap.clear();
		String[] eggs = str.split("\n");
		for (String egg : eggs)
		{
			String[] searchReplace = egg.split("=");
			if (searchReplace.length >= 2)
			{
				easterMap.put(searchReplace[0].trim().toLowerCase(), searchReplace[1].trim());
			}
		}
	}
}
