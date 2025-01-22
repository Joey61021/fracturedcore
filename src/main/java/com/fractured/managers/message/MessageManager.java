package com.fractured.managers.message;

import com.fractured.FracturedCore;
import com.fractured.utilities.Config;
import com.fractured.utilities.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.function.Function;

@RequiredArgsConstructor
public class MessageManager {

    public static void sendMessage(CommandSender receiver, Message message) {
        sendMessage(receiver, message, Function.identity());
    }

    public static void sendMessage(CommandSender receiver, Message message, Function<String, String> replacementFunction) {
        String path = message.getPath();
        Config messages = FracturedCore.getMessages;
        if (messages.isString(path))
            receiver.sendMessage(Utils.color(replacementFunction.apply(messages.getString(path))));
        else if (messages.isList(path)) {
            String joinedMessage = String.join("\n", messages.getStringList(path));
            receiver.sendMessage(Utils.color(replacementFunction.apply(joinedMessage)));
        }
        else
            throw new IllegalArgumentException("Path \"" + path + "\" is not a string or list of strings in the messages.yml");
    }
}
