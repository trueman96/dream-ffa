package cc.dreamcode.ffa.handler;

import cc.dreamcode.command.DreamCommandExecutor;
import cc.dreamcode.command.context.CommandInvokeContext;
import cc.dreamcode.command.context.CommandPathContext;
import cc.dreamcode.command.handler.type.InvalidUsageType;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.ffa.config.MessageConfig;
import cc.dreamcode.utilities.bukkit.ChatUtil;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(onConstructor_= @Inject)
public class InvalidUsageHandler implements InvalidUsageType {

    private final MessageConfig messageConfig;

    @Override
    public void handle(@NonNull DreamSender<?> sender, @NonNull DreamCommandExecutor executor, @NonNull List<CommandPathContext> commandPathContextList, @NonNull CommandInvokeContext commandInvokeContext) {
        sender.sendMessage("invalid usage");
    }
}
