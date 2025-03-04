package com.solegendary.reignofnether.unit.units.piglins;

import com.solegendary.reignofnether.building.BuildingUtils;
import com.solegendary.reignofnether.hud.HudClientEvents;
import net.minecraft.client.resources.language.I18n;
import com.solegendary.reignofnether.ReignOfNether;
import com.solegendary.reignofnether.building.BuildingServerboundPacket;
import com.solegendary.reignofnether.building.ProductionBuilding;
import com.solegendary.reignofnether.building.ProductionItem;
import com.solegendary.reignofnether.hud.Button;
import com.solegendary.reignofnether.keybinds.Keybinding;
import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.resources.ResourceCost;
import com.solegendary.reignofnether.resources.ResourceCosts;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class GruntProd extends ProductionItem {

    public final static String itemName = "Grunt";
    public final static ResourceCost cost = ResourceCosts.GRUNT;

    public GruntProd(ProductionBuilding building) {
        super(building, cost.ticks);
        this.onComplete = (Level level) -> {
            if (!level.isClientSide())
                building.produceUnit((ServerLevel) level, EntityRegistrar.GRUNT_UNIT.get(), building.ownerName, true);
        };
        this.foodCost = cost.food;
        this.woodCost = cost.wood;
        this.oreCost = cost.ore;
        this.popCost = cost.population;
    }

    public String getItemName() {
        return GruntProd.itemName;
    }

    public static Button getStartButton(ProductionBuilding prodBuilding, Keybinding hotkey) {
        List<FormattedCharSequence> tooltipLines = new ArrayList<>(List.of(
            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.grunt"), Style.EMPTY.withBold(true)),
            ResourceCosts.getFormattedCost(cost),
            ResourceCosts.getFormattedPopAndTime(cost),
            FormattedCharSequence.forward("", Style.EMPTY),
            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.grunt.tooltip1"), Style.EMPTY),
            FormattedCharSequence.forward(I18n.get("units.piglins.reignofnether.grunt.tooltip2"), Style.EMPTY)
        ));

        return new Button(
            GruntProd.itemName,
            14,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/grunt.png"),
            hotkey,
            () -> false,
            () -> false,
            () -> true,
            () -> {
                if (!BuildingUtils.anyOtherCapitolProducingWorkers(true, prodBuilding))
                    BuildingServerboundPacket.startProduction(prodBuilding.originPos, itemName);
                else
                    HudClientEvents.showTemporaryMessage("Only one capitol may build workers at a time.");
            },
            null,
            tooltipLines
        );
    }

    public Button getCancelButton(ProductionBuilding prodBuilding, boolean first) {
        return new Button(
            GruntProd.itemName,
            14,
            new ResourceLocation(ReignOfNether.MOD_ID, "textures/mobheads/grunt.png"),
            (Keybinding) null,
            () -> false,
            () -> false,
            () -> true,
            () -> BuildingServerboundPacket.cancelProduction(prodBuilding.originPos, itemName, first),
            null,
            null
        );
    }
}
