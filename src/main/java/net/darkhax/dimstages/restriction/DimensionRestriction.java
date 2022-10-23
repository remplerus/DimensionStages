package net.darkhax.dimstages.restriction;

import javax.annotation.Nullable;

import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class DimensionRestriction implements IDimensionRestriction {
    
    /**
     * The default restriction message that is displayed to players when they get restricted.
     */
    public static final BaseComponent DEFAULT_MESSAGE = new TranslatableComponent("message.dimstages.noentry");
    
    /**
     * The actual restriction message to display when a player's access to a dimension is
     * restricted.
     */
    @Nullable
    private TextComponent restrictionMessage = (TextComponent) DEFAULT_MESSAGE;
    
    /**
     * Sets the restriction message to a new message.
     * 
     * @param newMessage The new message to display.
     * @return The previous restriction message.
     */
    @Nullable
    public TextComponent setRestrictionMessage (@Nullable TextComponent newMessage) {
        
        final TextComponent oldMessage = this.restrictionMessage;
        this.restrictionMessage = newMessage;
        return oldMessage;
    }
    
    @Override
    public TextComponent getRestrictedMessage (Player player, ResourceLocation dimension) {
        
        return this.restrictionMessage;
    }
}