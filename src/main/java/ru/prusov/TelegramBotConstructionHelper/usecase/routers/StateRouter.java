package ru.prusov.TelegramBotConstructionHelper.usecase.routers;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.State;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class StateRouter {
    private final Map<UserState, State> handleMap = new EnumMap<>(UserState.class);

    public StateRouter (List<State> handlers){
        handlers.forEach(handler -> {
            handleMap.put(handler.state(), handler);
        });
    }

    public Optional<State> getHandler(UserState userState){
        return Optional.ofNullable(handleMap.get(userState));
    }
}
