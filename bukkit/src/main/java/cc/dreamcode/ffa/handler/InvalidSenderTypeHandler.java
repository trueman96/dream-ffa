package cc.dreamcode.ffa.handler;

import cc.dreamcode.command.handler.type.InvalidSenderType;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.command.sender.SenderType;
import cc.dreamcode.ffa.config.MessageConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_= @Inject)
public class InvalidSenderTypeHandler implements InvalidSenderType {

    private final MessageConfig messageConfig;

    @Override
    public void handle(@NonNull DreamSender<?> sender, @NonNull SenderType requiredSender) {

    }
}
