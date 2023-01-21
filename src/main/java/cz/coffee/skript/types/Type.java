/**
 *   This file is part of skJson.
 * <p>
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * <p>
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * <p>
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Copyright coffeeRequired nd contributors
 */
package cz.coffee.skript.types;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.util.SimpleLiteral;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.Converters;
import ch.njol.yggdrasil.Fields;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.jetbrains.annotations.NotNull;

import java.io.StreamCorruptedException;

import static cz.coffee.utils.json.JsonUtils.fromString2JsonElement;


@SuppressWarnings("unused")
public class Type {
    private static final String KEY_PARSED_TAG = ";";

    static {
        Converters.registerConverter(JsonElement.class, String.class, JsonElement::toString);

        Classes.registerClass(
                new ClassInfo<>(JsonElement.class, "json")
                        .user("json")
                        .name("json")
                        .description("Represents a json element and its class `JsonElement.class`.",
                                "You can convert string to json or json to string",
                                "You can remove or add elements to json"
                        )
                        .examples(
                                "on load:",
                                "\nset {_e} to \"{'Test object': true}\" parsed as json",
                                "\nsend \"%{_e}%\"",
                                "add \"something;false\" to {_e}",
                                "remove \"something\" from {_e}",
                                "\nset {_e} to \"[1,2,3]\" parsed as json",
                                "\nsend \"%{_e}%\"",
                                "add \"4\" to {_e}",
                                "remove \"1\" from {_e}",
                                ""
                        )
                        .since("2.5.0")
                        .parser(
                                new Parser<>() {
                                    @Override
                                    public @NotNull String toString(JsonElement element, int i) {
                                        return element.toString();
                                    }

                                    @Override
                                    public @NotNull String toVariableNameString(JsonElement element) {
                                        return toString(element, 0);
                                    }

                                    @Override
                                    public boolean canParse(@NotNull ParseContext context) {
                                        return false;
                                    }

                                }
                        )
                        .defaultExpression(new SimpleLiteral<>(JsonNull.INSTANCE, true))
                        .serializer(
                                new Serializer<>() {
                                    @Override
                                    public @NotNull Fields serialize(JsonElement element) {
                                        Fields fields = new Fields();
                                        fields.putObject("json", element.toString());
                                        return fields;
                                    }

                                    @Override
                                    public void deserialize(JsonElement element, @NotNull Fields fields) {
                                        assert false;
                                    }

                                    @Override
                                    public JsonElement deserialize(@NotNull Fields fields) throws StreamCorruptedException {
                                        Object fieldContent = fields.getObject("json");
                                        if (fieldContent == null) return JsonNull.INSTANCE;
                                        fields.removeField("json");
                                        return fromString2JsonElement(fieldContent);
                                    }

                                    @Override
                                    public boolean mustSyncDeserialization() {
                                        return false;
                                    }

                                    @Override
                                    protected boolean canBeInstantiated() {return false;}
                                }
                        )
        );
    }
}