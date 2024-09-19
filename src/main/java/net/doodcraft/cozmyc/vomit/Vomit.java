package net.doodcraft.cozmyc.vomit;

import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Vomit extends WaterAbility implements AddonAbility {

    private Location origin;
    private Vector direction;
    private double length;
    private final double particleSpacing;
    private final double maxCurveIntensity;

    public Vomit(Player player, double particleSpacing, double maxCurveIntensity) {
        super(player);

        this.player = player;
        this.direction = player.getLocation().getDirection();
        this.particleSpacing = particleSpacing;
        this.maxCurveIntensity = maxCurveIntensity;

        this.origin = player.getEyeLocation();
        this.length = calculateBeamLength();

        start();
    }

    @Override
    public void progress() {
        this.origin = player.getEyeLocation();
        this.direction = player.getLocation().getDirection();

        Location eyeLocation = player.getEyeLocation();
        double maxDistance = 64;
        double length = maxDistance;
        
        for (double distance = 0; distance <= maxDistance; distance += particleSpacing) {
            Location testLocation = eyeLocation.clone().add(direction.clone().multiply(distance));

            if (testLocation.getBlock().getType() != Material.AIR) {
                new TempBlock(testLocation.getBlock(), Material.SLIME_BLOCK.createBlockData(), 12000);
                length = distance;
                break;
            }
        }
        
        for (double i = 0; i <= length; i += particleSpacing) {
            double distanceFactor = i / length;
            double curveIntensity = maxCurveIntensity * (1 - distanceFactor);
            double offsetY = Math.sin(distanceFactor * Math.PI) * curveIntensity;

            Location particleLocation = origin.clone().add(direction.clone().multiply(i)).add(new Vector(0, offsetY, 0));

            ParticleEffect.SLIME.display(particleLocation, 2, 0, 0, 0, 0.01);
            ParticleEffect.COMPOSTER.display(particleLocation, 2, 0, 0, 0, 0.01);
        }
    
        if (System.currentTimeMillis() - getStartTime() > 5000) {
            remove();
        }
    }


    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public String getName() {
        return "Vomit";
    }

    @Override
    public Location getLocation() {
        return origin;
    }

    @Override
    public void load() {
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(new VomitListener(), ProjectKorra.plugin);
    }

    @Override
    public void stop() {}

    @Override
    public String getAuthor() {
        return "Cozmyc";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Creates a stream of high-velocity vomit in the direction you're looking.";
    }

    @Override
    public String getInstructions() {
        return "Press and release shift to empty your stomach upon the world!";
    }
}
