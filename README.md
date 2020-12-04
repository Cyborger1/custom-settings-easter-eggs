# Custom Settings Easter Eggs
Allows you to set up custom Easter eggs for the reworked settings interface.

![Example custom egg](egg_example.png)

## Usage
In the plugin's config, simply add your eggs in the following format:
```
Easter egg search term => Easter egg text
```

Separate your entries on new lines:
```
Text 1 => Egg 1
Text 2 => Egg 2
```

Define alternate search terms for the same Easter egg:
```
Text1 & Text2 & Text3 => Egg
```

## Current Limitations
* The plugin will only apply custom Easter eggs if the search result returns the default "Nothing Found" message.
* You cannot use the '=>' separator in the Easter egg text.