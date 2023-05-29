package lol.pyr.znpcsplus.interaction.consolecommand;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.interaction.InteractionAction;
import lol.pyr.znpcsplus.interaction.InteractionActionType;
import lol.pyr.znpcsplus.interaction.InteractionCommandHandler;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class ConsoleCommandActionType implements InteractionActionType<ConsoleCommandAction>, InteractionCommandHandler {
    private final TaskScheduler scheduler;

    public ConsoleCommandActionType(TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public String serialize(ConsoleCommandAction obj) {
        return Base64.getEncoder().encodeToString(obj.getCommand().getBytes(StandardCharsets.UTF_8)) + ";" + obj.getCooldown() + ";" + obj.getInteractionType().name();
    }

    @Override
    public ConsoleCommandAction deserialize(String str) {
        String[] split = str.split(";");
        InteractionType type = split.length > 2 ? InteractionType.valueOf(split[2]) : InteractionType.ANY_CLICK;
        return new ConsoleCommandAction(scheduler, new String(Base64.getDecoder().decode(split[0]), StandardCharsets.UTF_8), type, Long.parseLong(split[1]));
    }

    @Override
    public Class<ConsoleCommandAction> getActionClass() {
        return ConsoleCommandAction.class;
    }

    @Override
    public String getSubcommandName() {
        return "consolecommand";
    }

    @Override
    public InteractionAction parse(CommandContext context, NpcImpl npc) throws CommandExecutionException {
        context.setUsage(context.getUsage() + getSubcommandName() + " <type> <cooldown seconds> <command>");
        InteractionType type = context.parse(InteractionType.class);
        long cooldown = (long) (context.parse(Double.class) * 1000D);
        String command = context.dumpAllArgs();
        ConsoleCommandAction action = new ConsoleCommandAction(scheduler, command, type, cooldown);
        if (npc != null) {
            npc.addAction(action);
            context.send(Component.text("Added a console command action to the npc with the command " + action.getCommand(), NamedTextColor.GREEN));
        }
        return action;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {

    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestEnum(InteractionType.values());
        if (context.argSize() == 2) return context.suggestLiteral("1");
        return Collections.emptyList();
    }
}
