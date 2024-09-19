package net.doodcraft.cozmyc.vomit;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class VomitListener implements Listener {

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);

        if (bPlayer == null || !bPlayer.canBendIgnoreCooldowns(CoreAbility.getAbility(Vomit.class))) {
            return;
        }

        if (player.isSneaking() && bPlayer.getBoundAbilityName().equalsIgnoreCase("Vomit")) {
            if (!CoreAbility.hasAbility(player, Vomit.class)) {
                new Vomit(player, 0.2, 1.1);
            }
        }
    }
}
