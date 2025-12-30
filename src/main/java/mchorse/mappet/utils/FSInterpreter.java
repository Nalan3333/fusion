package mchorse.mappet.utils;

import it.unimi.dsi.fastutil.Hash;

import java.util.*;

public class FSInterpreter {

    public static HashMap<String, Object> interpret(String input) {
        HashMap<String, Object> variables = new HashMap<>();

        String[] lines = input.split("\\r?\\n");

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split("=", 2);
            if (parts.length != 2) continue;

            String name = parts[0].trim();
            String valueStr = parts[1].trim();
            Object value = parseValue(valueStr);

            variables.put(name, value);
        }

        return variables;
    }

    private static Object parseValue(String valueStr) {
        if (valueStr.equalsIgnoreCase("true") || valueStr.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(valueStr);
        }

        try {
            return Integer.parseInt(valueStr);
        } catch (NumberFormatException ignored) {}

        try {
            return Double.parseDouble(valueStr);
        } catch (NumberFormatException ignored) {}

        if ((valueStr.startsWith("\"") && valueStr.endsWith("\"")) ||
                (valueStr.startsWith("'") && valueStr.endsWith("'"))) {
            return valueStr.substring(1, valueStr.length() - 1);
        }
        return valueStr;
    }
}