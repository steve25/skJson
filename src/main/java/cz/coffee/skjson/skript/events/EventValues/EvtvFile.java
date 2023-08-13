package cz.coffee.skjson.skript.events.EventValues;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.NoDoc;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ExpressionType;

import java.io.File;

@NoDoc
public class EvtvFile extends EventValueExpression<File> {

    static {
        Skript.registerExpression(EvtvFile.class, File.class, ExpressionType.SIMPLE, "[the] [event-](file|link)");
    }
    public EvtvFile() {
        super(File.class);
    }
}
