package io.github.moulberry.repo.data;

import com.google.gson.annotations.SerializedName;
import io.github.moulberry.repo.util.NEUId;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NEUItem {
    @SerializedName("itemid")
    String minecraftItemId;
    @SerializedName("displayname")
    String displayName;
    String nbttag;
    int damage;
    List<String> lore;
    @SerializedName("internalname")
    @NEUId String skyblockItemId;
    String crafttext;
    String clickcommand;
    String modver;
    String infoType;
    List<String> info;
    boolean vanilla = false;

    int x;
    int y;
    int z;
    String island;

    @SerializedName("recipe")
    @Getter(value = AccessLevel.PRIVATE)
    NEURecipe singletonRecipe = null;
    @SerializedName("recipes")
    @Getter(value = AccessLevel.PRIVATE)
    List<NEURecipe> recipeList = new ArrayList<>();


    transient volatile List<NEURecipe> recipes;

    /**
     * Returns directly associated recipes. This does not necessarily contain all recipes that result in this item, nor
     * does this list have to contain any recipe that results in this item. To obtain such a list fulfilling those
     * criteria, you will need to collect all recipes associated with all items, and filter those manually.
     */
    public List<NEURecipe> getRecipes() {
        if (recipes == null) {
            synchronized (this) {
                if (recipes == null) {
                    List<NEURecipe> newRecipes = new ArrayList<>(recipeList.size() + (singletonRecipe == null ? 0 : 1));
                    newRecipes.addAll(recipeList);
                    if (singletonRecipe != null)
                        newRecipes.add(singletonRecipe);
                    recipes = newRecipes;
                    for (NEURecipe recipe : recipes) {
                        recipe.fillItemInfo(this);
                    }
                }
            }
        }
        return recipes;
    }


}
