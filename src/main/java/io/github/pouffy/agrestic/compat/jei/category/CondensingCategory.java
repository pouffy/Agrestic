package io.github.pouffy.agrestic.compat.jei.category;

import com.mojang.serialization.Codec;
import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.client.ui.CondenserMenu;
import io.github.pouffy.agrestic.common.recipe.BasicCondensingRecipe;
import io.github.pouffy.agrestic.compat.jei.AgresticJEIConstants;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticMenuTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.common.gui.elements.DrawableAnimated;
import mezz.jei.common.gui.elements.DrawableResource;
import mezz.jei.common.util.ErrorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import slimeknights.mantle.plugin.jei.fluid.FluidTooltipCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static io.github.pouffy.agrestic.compat.jei.AgresticJEIConstants.formatTime;

public class CondensingCategory extends AbstractRecipeCategory<RecipeHolder<BasicCondensingRecipe>> {
    private final List<ItemStack> furnaceFuels;

    public CondensingCategory(IGuiHelper guiHelper, List<ItemStack> furnaceFuels) {
        super(AgresticJEIConstants.CONDENSING, Component.translatable("emi.category.agrestic.alchemy"), guiHelper.createDrawableItemLike(AgresticBlocks.BASIC_CONDENSER), 130, 80);
        this.furnaceFuels = List.copyOf(furnaceFuels);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BasicCondensingRecipe> recipeHolder, IFocusGroup focuses) {
        BasicCondensingRecipe recipe = recipeHolder.value();

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = ErrorUtil.checkNotNull(minecraft.level, "level");
        RegistryAccess registryAccess = level.registryAccess();

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 46, 59)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addItemStacks(furnaceFuels);

        builder.addInputSlot(6, 19)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addInputSlot(6, 43)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                .addIngredients(recipe.getIngredients().get(1));

        if (recipe.getContainer().isPresent()) {
            builder.addInputSlot(82, 5)
                    .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.SLOT), -1, -1)
                    .addIngredients(recipe.getContainer().get());
        }

        builder.addInputSlot(109, 23)
                .addRichTooltipCallback(FluidTooltipCallback.UNITS)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.TANK), -1, -1)
                .setFluidRenderer(Math.min(recipe.getBase().amount() * 4, 8000), false, 16, 32)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(recipe.getBase().getFluids()));

        builder.addOutputSlot(82, 31)
                .setBackground(AgresticJEIConstants.asDrawable(AgresticSprites.BIG_SLOT), -5, -5)
                .addItemStack(recipe.getResultItem(registryAccess));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<BasicCondensingRecipe> recipe, IFocusGroup focuses) {
        builder.addDrawableWidget(AgresticJEIConstants.asDrawable(AgresticSprites.ALCHEMY_ARROW_BASE)).setPosition(23, 25);

        IDrawableStatic arrow = new DrawableResource(AgresticSprites.ALCHEMY_ARROW.location, 0, 0, AgresticSprites.ALCHEMY_ARROW.width, AgresticSprites.ALCHEMY_ARROW.height, 0, 0, 0, 0, AgresticSprites.ALCHEMY_ARROW.width, AgresticSprites.ALCHEMY_ARROW.height);
        IDrawableAnimated animatedFill = new DrawableAnimated(arrow, recipe.value().getTime(), IDrawableAnimated.StartDirection.LEFT, false);
        builder.addDrawableWidget(animatedFill).setPosition(23, 24).setTooltip(Component.translatable("recipe.agrestic.time", formatTime(recipe.value().getTime())).withStyle(ChatFormatting.GOLD));

        builder.addAnimatedRecipeFlameWidget(recipe.value().getTime())
                .setPosition(47, 42);
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<BasicCondensingRecipe> recipe) {
        return recipe.id();
    }

    @Override
    public Codec<RecipeHolder<BasicCondensingRecipe>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

    public static class TransferInfo implements IRecipeTransferInfo<CondenserMenu, RecipeHolder<BasicCondensingRecipe>> {

        @Override
        public Class<? extends CondenserMenu> getContainerClass() {
            return CondenserMenu.class;
        }

        @Override
        public Optional<MenuType<CondenserMenu>> getMenuType() {
            return Optional.of(AgresticMenuTypes.BASIC_CONDENSER.get());
        }

        @Override
        public RecipeType<RecipeHolder<BasicCondensingRecipe>> getRecipeType() {
            return AgresticJEIConstants.CONDENSING;
        }

        @Override
        public boolean canHandle(CondenserMenu container, RecipeHolder<BasicCondensingRecipe> recipe) {
            return true;
        }

        @Override
        public List<Slot> getRecipeSlots(CondenserMenu container, RecipeHolder<BasicCondensingRecipe> recipe) {
            List<Slot> slots = new ArrayList<>();
            slots.add(container.getSlot(0));
            slots.add(container.getSlot(1));
            if (recipe.value().getContainer().isPresent()) {
                slots.add(container.getSlot(3));
            }
            return slots;
        }

        @Override
        public List<Slot> getInventorySlots(CondenserMenu container, RecipeHolder<BasicCondensingRecipe> recipe) {
            return container.slots.subList(5, 41);
        }
    }
}
