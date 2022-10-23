package net.darkhax.dimstages;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import net.darkhax.dimstages.restriction.IDimensionRestriction;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class RestrictionManager implements PreparableReloadListener {
    
    private final Multimap<ResourceLocation, IDimensionRestriction> restrictions = ArrayListMultimap.create();
    
    public RestrictionManager() {
        
        MinecraftForge.EVENT_BUS.addListener(this::onEntityTravelToDimension);
        MinecraftForge.EVENT_BUS.addListener(this::addReloadListeners);
    }
    
    public <T extends IDimensionRestriction> T addRestriction (ResourceLocation dimensionId, T restriction) {
        
        this.restrictions.put(dimensionId, restriction);
        return restriction;
    }
    
    private void onEntityTravelToDimension (EntityTravelToDimensionEvent event) {
        
        if (event.getEntity() instanceof final ServerPlayer player) {

            final ResourceLocation dimensionId = event.getDimension().location();
            
            for (final IDimensionRestriction restriction : this.restrictions.get(dimensionId)) {
                
                if (restriction != null && restriction.shouldRestrict(player, event.getDimension().location())) {
                    
                    event.setCanceled(true);
                    DimensionStages.LOG.debug("Restricted {} from accessing dimension {}. Restriction={}", player.getDisplayName().getString(), dimensionId, restriction);
                    
                    final TextComponent message = restriction.getRestrictedMessage(player, dimensionId);
                    
                    if (message != null) {
                        
                        player.displayClientMessage(message, true);
                    }
                    
                    break;
                }
            }
        }
    }
    
    private void addReloadListeners (AddReloadListenerEvent event) {
        
        event.addListener(this);
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier p_10638_, ResourceManager p_10639_, ProfilerFiller p_10640_, ProfilerFiller p_10641_, Executor p_10642_, Executor p_10643_) {

        this.restrictions.clear();
        return null;
    }
}