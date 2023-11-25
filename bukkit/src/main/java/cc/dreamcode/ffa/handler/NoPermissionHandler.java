package cc.dreamcode.ffa.handler;

import cc.dreamcode.command.handler.type.NoPermissionType;
import cc.dreamcode.command.sender.DreamSender;
import cc.dreamcode.ffa.config.MessageConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_= @Inject)
public class NoPermissionHandler implements NoPermissionType {

    private final MessageConfig messageConfig;

    @Override
    public void handle(@NonNull DreamSender<?> sender, @NonNull String permission) {

    }
}
