package dev.latvian.kubejs.item;

import dev.latvian.kubejs.docs.ID;
import dev.latvian.kubejs.text.Text;
import dev.latvian.kubejs.util.BuilderBase;
import dev.latvian.kubejs.util.UtilsJS;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class ItemBuilder extends BuilderBase {
	public int maxStackSize;
	public int maxDamage;
	public String containerItem;
	// TODO reevaluate whether to automatically add to tool tags
	public Map<Identifier, Integer> tools;
	public float miningSpeed;
	public Float attackDamage;
	public Float attackSpeed;
	public Rarity rarity;
	public boolean glow;
	public final List<Text> tooltip;
	public ItemGroup group;
	public Int2IntOpenHashMap color;
	public String texture;
	public String parentModel;
	public FoodBuilder foodBuilder;
	
	public ItemJS item;
	
	public ItemBuilder(String i) {
		super(i);
		maxStackSize = 64;
		maxDamage = 0;
		containerItem = "minecraft:air";
		tools = new HashMap<>();
		miningSpeed = 1.0F;
		rarity = Rarity.COMMON;
		glow = false;
		tooltip = new ArrayList<>();
		group = ItemGroup.MISC;
		color = new Int2IntOpenHashMap();
		color.defaultReturnValue(0xFFFFFFFF);
		texture = id.getNamespace() + ":item/" + id.getPath();
		parentModel = "item/generated";
		foodBuilder = null;
		displayName = "";
	}
	
	@Override
	public String getType() {
		return "item";
	}
	
	public ItemBuilder maxStackSize(int v) {
		maxStackSize = v;
		return this;
	}
	
	public ItemBuilder unstackable() {
		return maxStackSize(1);
	}
	
	public ItemBuilder maxDamage(int v) {
		maxDamage = v;
		return this;
	}
	
	public ItemBuilder containerItem(@ID String id) {
		containerItem = UtilsJS.getID(id);
		return this;
	}
	
	public ItemBuilder tool(String type, int level) {
		tools.put(UtilsJS.getMCID(type), level);
		return this;
	}
	
	public ItemBuilder miningSpeed(float miningSpeed) {
		this.miningSpeed = miningSpeed;
		return this;
	}
	
	public ItemBuilder attackDamage(float attackDamage) {
		this.attackDamage = attackDamage;
		return this;
	}
	
	public ItemBuilder attackSpeed(float attackSpeed) {
		this.attackSpeed = attackSpeed;
		return this;
	}
	
	public ItemBuilder rarity(Rarity v) {
		rarity = v;
		return this;
	}
	
	public ItemBuilder glow(boolean v) {
		glow = v;
		return this;
	}
	
	public ItemBuilder tooltip(Object text) {
		tooltip.add(Text.of(text));
		return this;
	}
	
	public ItemBuilder group(String g) {
		for (ItemGroup ig : ItemGroup.GROUPS) {
			if (ig.getName().equals(g)) {
				group = ig;
				return this;
			}
		}
		
		return this;
	}
	
	public ItemBuilder color(int index, int c) {
		color.put(index, 0xFF000000 | c);
		return this;
	}
	
	public ItemBuilder texture(String tex) {
		texture = tex;
		return this;
	}
	
	public ItemBuilder parentModel(String m) {
		parentModel = m;
		return this;
	}
	
	public ItemBuilder food(Consumer<FoodBuilder> b) {
		foodBuilder = new FoodBuilder();
		b.accept(foodBuilder);
		return this;
	}
	
	public Map<Identifier, Integer> getToolsMap() {
		return tools;
	}
	
	public float getMiningSpeed() {
		return miningSpeed;
	}
	
	public Float getAttackDamage() {
		return attackDamage;
	}
	
	public Float getAttackSpeed() {
		return attackSpeed;
	}
	
	public Item.Settings createItemProperties() {
		Item.Settings properties = new Item.Settings();
		
		properties.group(group);
		properties.maxDamage(maxDamage);
		properties.maxCount(maxStackSize);
		properties.rarity(rarity);
		
		Item item = Registry.ITEM.get(UtilsJS.getMCID(containerItem));
		
		if (item != null && item != Items.AIR) {
			properties.recipeRemainder(item);
		}
		
		if (foodBuilder != null) {
			properties.food(foodBuilder.build());
		}
		
		return properties;
	}
}