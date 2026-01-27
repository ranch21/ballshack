package org.ranch.ballshack.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.text.Text;

public class CharacterArgumentType implements ArgumentType<Character> {

	public static final SimpleCommandExceptionType TOO_MANY_CHARACTERS_EXCEPTION = new SimpleCommandExceptionType(Text.literal("Too many characters"));

	@Override
	public Character parse(StringReader reader) throws CommandSyntaxException {
		if (reader.getRemainingLength() > 1)
			throw TOO_MANY_CHARACTERS_EXCEPTION.createWithContext(reader);
		return reader.read();
	}

	public static Character getCharacter(final CommandContext<?> context, final String name) {
		return context.getArgument(name, Character.class);
	}
}
