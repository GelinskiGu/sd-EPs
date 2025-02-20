package com.gelinski.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.swing.*;

public class ResponseFormatter {
    private static final Gson gson = new Gson();

    public static void showFormattedMessage(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        StringBuilder formattedMessage = new StringBuilder();

        formattedMessage.append("Response:\n");
        jsonObject.entrySet().forEach(entry -> {
            if (entry.getValue().isJsonArray()) {
                formattedMessage.append(entry.getKey()).append(":\n");
                JsonArray jsonArray = entry.getValue().getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject arrayElement = jsonArray.get(i).getAsJsonObject();
                    formattedMessage.append("  Element ").append(i + 1).append(":\n");
                    arrayElement.entrySet().forEach(arrayEntry -> {
                        formattedMessage.append("    ").append(arrayEntry.getKey()).append(": ").append(arrayEntry.getValue()).append("\n");
                    });
                }
            } else {
                formattedMessage.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        });

        JOptionPane.showMessageDialog(null, formattedMessage.toString(), "Server Response", JOptionPane.INFORMATION_MESSAGE);
    }
}
