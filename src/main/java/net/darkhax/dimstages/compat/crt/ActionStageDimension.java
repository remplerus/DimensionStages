package net.darkhax.dimstages.compat.crt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.blamejared.crafttweaker.api.action.base.IRuntimeAction;

import net.darkhax.dimstages.DimensionStages;
import net.darkhax.dimstages.restriction.StagedDimensionRestriction;
import net.darkhax.gamestages.GameStageHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;

public class ActionStageDimension implements IRuntimeAction {
    
    private final String dimensionId;
    private final String[] stages;
    private final Component restrictionMessage;
    
    public ActionStageDimension(String dimensionId, @Nullable Component message, String... stages) {
        
        this.dimensionId = dimensionId;
        this.stages = stages;
        this.restrictionMessage = message;
    }
    
    @Override
    public boolean validate (Logger logger) {
        
        if (ResourceLocation.tryParse(this.dimensionId) == null) {
            
            logger.error("[Dimension Stages] Invalid dimension ID '" + this.dimensionId + "'.");
            return false;
        }
        
        final Set<String> validStages = new HashSet<>();
        
        for (final String stage : this.stages) {
            
            if (!GameStageHelper.isValidStageName(stage)) {
                
                logger.error("[Dimension Stages] Invalid stage name '" + stage + "' for dimension '" + this.dimensionId + "'. " + this.getDeclaredScriptPosition());
                continue;
            }
            
            if (!GameStageHelper.getKnownStages().isEmpty() && !GameStageHelper.isStageKnown(stage)) {
                
                logger.warn("[Dimension Stages] Unknown stage '" + stage + "' for dimension '" + this.dimensionId + "' " + this.getDeclaredScriptPosition());
            }
            
            validStages.add(stage);
        }
        
        if (validStages.isEmpty()) {
            
            logger.error("[Dimension Stages] No valid stages specified for dimension '" + this.dimensionId + "'. " + this.getDeclaredScriptPosition());
            return false;
        }
        
        return true;
    }
    
    @Override
    public void apply () {
        
        final StagedDimensionRestriction restriction = DimensionStages.MANAGER.addRestriction(new ResourceLocation(this.dimensionId), new StagedDimensionRestriction());
        
        for (final String stage : this.stages) {
            
            if (GameStageHelper.isValidStageName(stage)) {
                
                restriction.addStage(stage);
            }
        }
        
        restriction.setRestrictionMessage(this.restrictionMessage);
    }
    
    @Override
    public String describe () {
        
        return "[Dimension Stages] Staging dimension '" + this.dimensionId + "' to stage(s) '" + Arrays.toString(this.stages) + "'. " + this.getDeclaredScriptPosition();
    }
}
