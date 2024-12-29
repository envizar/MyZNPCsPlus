package lol.pyr.znpcsplus.commands;

import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ToggleCommand implements CommandHandler {
    private final NpcRegistryImpl npcRegistry;

    public ToggleCommand(NpcRegistryImpl npcRegistry) {
        this.npcRegistry = npcRegistry;
    }

    @Override
    public void run(CommandContext context) throws CommandExecutionException {
        context.setUsage(context.getLabel() + " toggle <id> [<enable/disable>]");
        NpcImpl npc = context.parse(NpcEntryImpl.class).getNpc();
        boolean enabled;
        if (context.argSize() == 1) {
            enabled = context.popString().equals("enable");
        } else {
            enabled = !npc.isEnabled();
        }
        npc.setEnabled(enabled);
        context.send(Component.text("NPC has been " + (enabled ? "enabled" : "disabled"), NamedTextColor.GREEN));
    }

    @Override
    public List<String> suggest(CommandContext context) throws CommandExecutionException {
        if (context.argSize() == 1) return context.suggestCollection(npcRegistry.getModifiableIds());
        if (context.argSize() == 2) return context.suggestLiteral("enable", "disable");
        return Collections.emptyList();
    }
}
