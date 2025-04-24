package dev.jsinco.brewery.bukkit.command;

import dev.jsinco.brewery.brew.Brew;
import dev.jsinco.brewery.bukkit.TheBrewingProject;
import dev.jsinco.brewery.bukkit.brew.BrewAdapter;
import dev.jsinco.brewery.bukkit.recipe.RecipeEffectsImpl;
import dev.jsinco.brewery.bukkit.util.MessageUtil;
import dev.jsinco.brewery.configuration.locale.TranslationsConfig;
import dev.jsinco.brewery.recipes.BrewScoreImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class InfoCommand {
    public static boolean onCommand(Player target, CommandSender sender, String[] args) {
        ItemStack item = args.length > 1 ? target.getInventory().getItem(Integer.parseInt(args[1])) : target.getInventory().getItemInMainHand();
        if (item == null) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(TranslationsConfig.COMMAND_INFO_NOT_A_BREW));
            return true;
        }
        Optional<Brew> brewOptional = BrewAdapter.fromItem(item);
        brewOptional
                .ifPresent(brew -> sender.sendMessage(
                        MiniMessage.miniMessage().deserialize(TranslationsConfig.COMMAND_INFO_BREW_MESSAGE,
                                MessageUtil.getScoreTagResolver(brew.closestRecipe(TheBrewingProject.getInstance().getRecipeRegistry())
                                        .map(brew::score)
                                        .orElse(BrewScoreImpl.NONE)),
                                Placeholder.component("brewing_step_info", MessageUtil.compileBrewInfo(brew, true)
                                        .collect(Component.toComponent(Component.text("\n")))
                                )
                        )
                ));
        Optional<RecipeEffectsImpl> recipeEffectsOptional = RecipeEffectsImpl.fromItem(item);
        recipeEffectsOptional.ifPresent(effects -> {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(TranslationsConfig.COMMAND_INFO_EFFECT_MESSAGE, MessageUtil.recipeEffectResolver(effects)));
        });
        if (brewOptional.isEmpty() && recipeEffectsOptional.isEmpty()) {
            sender.sendMessage(MiniMessage.miniMessage().deserialize(TranslationsConfig.COMMAND_INFO_NOT_A_BREW));
        }
        return true;
    }
}
