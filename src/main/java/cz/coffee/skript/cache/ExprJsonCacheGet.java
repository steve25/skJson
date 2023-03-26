package cz.coffee.skript.cache;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.google.gson.JsonElement;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Map;

import static cz.coffee.SkJson.JSON_STORAGE;

/**
 * This file is part of skJson.
 * <p>
 * Skript is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Skript is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Skript.  If not, see <<a href="http://www.gnu.org/licenses/">...</a>>.
 * <p>
 * Copyright coffeeRequired nd contributors
 * <p>
 * Created: úterý (14.03.2023)
 */

@Name("Get cached json")
@Description({"You can get json from cached internal storage by with a key defined by you"})
@Examples({"on script load:",
        "\tset {_json} to cached json \"your\"",
        "\tsend {_json} with pretty print"
})
@Since("2.8.0 - performance & clean")

public class ExprJsonCacheGet extends SimpleExpression<JsonElement> {

    static {
        Skript.registerExpression(ExprJsonCacheGet.class, JsonElement.class, ExpressionType.SIMPLE,
                "cached json %string%"
        );
    }
    private Expression<String> storedKeyExpr;

    @Override
    protected @Nullable JsonElement @NotNull [] get(@NotNull Event e) {
        String storedKey = storedKeyExpr.getSingle(e);
        if (storedKey != null) {
            for (Map.Entry<String, Map<JsonElement, File>> entry : JSON_STORAGE.entrySet()) {
                if (entry.getKey().equals(storedKey)) {
                    for (Map.Entry<JsonElement, File> fileEntry : entry.getValue().entrySet()) {
                        return new JsonElement[]{fileEntry.getKey()};
                    }
                }
            }
        }
        return new JsonElement[0];
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<JsonElement> getReturnType() {
        return JsonElement.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        return "get cached json " + storedKeyExpr.toString(event, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, @NotNull SkriptParser.ParseResult parseResult) {
        storedKeyExpr = (Expression<String>) exprs[0];
        return true;
    }
}