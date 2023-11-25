package cc.dreamcode.ffa.handler;

import cc.dreamcode.command.handler.type.InvalidInputValueType;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.utilities.bukkit.ChatUtil;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_= @Inject)
public class InvalidInputValueHandler implements InvalidInputValueType {

    private final MessageConfig messageConfig;

    @Override
    public void handle(@NonNull DreamSender<?> sender, @NonNull Class<?> requiredClass, @NonNull String argument, int index) {
        sender.sendMessage("invalid input");
    }
}
