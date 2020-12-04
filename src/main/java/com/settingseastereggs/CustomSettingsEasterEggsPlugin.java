package com.settingseastereggs;

import com.google.common.base.Splitter;
import com.google.inject.Provides;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.swing.JOptionPane;
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

	private static final Splitter.MapSplitter EGG_MAP_SPLITTER = Splitter.on('\n')
		.trimResults()
		.omitEmptyStrings()
		.withKeyValueSeparator("=>");

	private static final Splitter SUB_EGG_SPLITTER = Splitter.on('&')
		.trimResults()
		.omitEmptyStrings();

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
			String search = client.getVarcStrValue(417);
			Widget result = client.getWidget(134, 17);
			if (search != null && result != null)
			{
				Widget resultChild = result.getChild(0);
				if (resultChild != null && resultChild.getText().equals(NO_RESULT_STRING))
				{
					String egg = easterMap.get(Text.removeTags(search).toLowerCase());
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
		try
		{
			EGG_MAP_SPLITTER.split(str).forEach((k, v) ->
			{
				for (String subegg : SUB_EGG_SPLITTER.split(k))
				{
					easterMap.put(subegg.toLowerCase(), v);
				}
			});
		}
		catch (IllegalArgumentException iae)
		{
			if (config.showErrorMessage())
			{
				String error =
					"Invalid entry in custom settings easter eggs config." +
						"\n" + iae.getMessage();

				JOptionPane.showOptionDialog(null, error,
					"Custom Settings Easter Eggs", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
					null, new String[]{"Ok"}, "Ok");
			}
		}
	}
}
