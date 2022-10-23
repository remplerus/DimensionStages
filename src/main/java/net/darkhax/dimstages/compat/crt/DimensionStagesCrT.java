package net.darkhax.dimstages.compat.crt;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import net.minecraft.network.chat.TextComponent;
import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;

import net.darkhax.dimstages.restriction.DimensionRestriction;

@ZenRegister
@ZenCodeType.Name("mods.dimstages.DimensionStages")
public class DimensionStagesCrT {
    
    @ZenCodeType.Method
    public static void stageDimension (String dimensionId, String... stages) {
        
        CraftTweakerAPI.apply(new ActionStageDimension(dimensionId, new TextComponent(DimensionRestriction.DEFAULT_MESSAGE.getContents()), stages));
    }
    
    @ZenCodeType.Method
    public static void stageDimensionWithMessage (String dimensionId, TextComponent message, String... stages) {
        
        CraftTweakerAPI.apply(new ActionStageDimension(dimensionId, message, stages));
    }
}