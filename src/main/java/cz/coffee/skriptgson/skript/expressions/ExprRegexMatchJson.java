package cz.coffee.skriptgson.skript.expressions;

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
import cz.coffee.skriptgson.SkriptGson;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Name("Json regex match")
@Description("Get a All matches of regex pattern from json")
@Examples({
        "on load:",
        "\tset {-e} to json {\"anything\": [1,2,\"false\"]",
        "\tsend {-e} regex matches \"anything\"",
})
@Since("1.2.0")

@SuppressWarnings({"unused","NullableProblems","unchecked"})


public class ExprRegexMatchJson extends SimpleExpression<String> {

    static {
        Skript.registerExpression(ExprRegexMatchJson.class, String.class, ExpressionType.COMBINED,
                "[json] %jsonelement% [regex] match[es] %string%"
        );
    }

    private Expression<String> inputRegex;
    private Expression<JsonElement> Json;

    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
        Json = (Expression<JsonElement>) exprs[0];
        inputRegex = (Expression<String>) exprs[1];
        return Json != null && inputRegex != null;
    }

    @Override
    protected @Nullable String[] get(Event e) {
        String regex = inputRegex.getSingle(e);
        String element = Json.getSingle(e).toString();
        assert regex != null;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(element);

        if (m.matches()) {
            if(m.groupCount() == 0) {
                SkriptGson.debug("&c MAIN 0:"+ m.group(0));
                return new String[]{m.group(0)};
            } else if (m.groupCount() >= 1) {
                List<String> output = new ArrayList<>();
                for(int i = 0; (m.groupCount() +1)> i; i++) {
                    output.add(i, m.group(i));
                }
                return new String[]{output.toString().replaceAll("]", "").replaceAll("]", "")};
            }
        }
        return null;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return null;
    }
}