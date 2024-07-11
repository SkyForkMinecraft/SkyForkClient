package soar;

import java.awt.*;
import java.util.List;

import cn.cedo.drag.Dragging;
import cn.cedo.shader.RoundedUtil;
import cn.langya.event.ShaderType;
import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;

public class PackDisplayMod implements Access.InstanceAccess {

	private IResourcePack pack;
	private ResourceLocation currentPack;
	private final ResourcePackRepository resourcePackRepository = mc.getResourcePackRepository();
	private List<ResourcePackRepository.Entry> packs = resourcePackRepository.getRepositoryEntries();
	private FontDrawer fr = FontManager.M22;
	private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(), "packdisplay", 150, 150);
	private static final NumberValue customAplha = new NumberValue("自定义不透明度",80,0,255,5);
	private static final NumberValue customRadius = new NumberValue("自定义圆角值", 2,0,10,1);
	private final BooleanValue blur = new BooleanValue("模糊背景",true);
	@Enable
	public void onEnable() {
		this.loadTexture();
	}

	@EventTarget
	public void onRender2D(Render2DEvent event) {
		
		GlStateManager.pushMatrix();
		
		if(pack == null) {
			pack = this.getCurrentPack();
		}
		
		RoundedUtil.drawRound(pos.getXPos(), pos.getYPos(), (float) (46 + (fr.getStringWidth(this.convertNormalText(pack.getPackName())))), 38,customRadius.getValue().intValue(),new Color(0,0,0,customAplha.getValue().intValue()));
		mc.getTextureManager().bindTexture(this.currentPack);
		RoundedUtil.drawRoundTextured(pos.getXPos() + 4.5F, pos.getYPos() + 4.5F, 29, 29, 4, 1.0F);
		
		fr.drawString(this.convertNormalText(pack.getPackName()), pos.getXPos() + 40, pos.getYPos() + (29 / 2), -1);
		
		GlStateManager.popMatrix();
		
		pos.setWidth(46 + fr.getStringWidth(this.convertNormalText(pack.getPackName())));
		pos.setHeight(38);
	}
	
	@EventTarget
	public void onShader(ShaderEvent event) {
		if (!blur.getValue() && event.getType() != ShaderType.Shadow) return;
		if(pack != null) {
			RoundedUtil.drawRound(pos.getXPos(), pos.getYPos(), (float) (46 + (fr.getStringWidth(this.convertNormalText(pack.getPackName())))), 38,customRadius.getValue().intValue(),new Color(0,0,0,customAplha.getValue().intValue()));
		}
	}
	
	@EventTarget
	public void onSwitchTexture(SwitchTextureEvent event) {
		packs = resourcePackRepository.getRepositoryEntries();
		pack = this.getCurrentPack();
		this.loadTexture();
	}
	
	private String convertNormalText(String text) {
		return text.replaceAll("\\u00a7" + "1", "").replaceAll("\\u00a7" + "2", "").replaceAll("\\u00a7" + "3", "")
				.replaceAll("\\u00a7" + "4", "").replaceAll("\\u00a7" + "5", "").replaceAll("\\u00a7" + "6", "")
				.replaceAll("\\u00a7" + "7", "").replaceAll("\\u00a7" + "8", "").replaceAll("\\u00a7" + "9", "")
				.replaceAll("\\u00a7" + "a", "").replaceAll("\\u00a7" + "b", "").replaceAll("\\u00a7" + "c", "")
				.replaceAll("\\u00a7" + "d", "").replaceAll("\\u00a7" + "e", "").replaceAll("\\u00a7" + "f", "")
				.replaceAll("\\u00a7" + "g", "").replaceAll("\\u00a7" + "k", "").replaceAll("\\u00a7" + "l", "")
				.replaceAll("\\u00a7" + "m", "").replaceAll("\\u00a7" + "n", "").replaceAll("\\u00a7" + "o", "")
				.replaceAll("\\u00a7" + "r", "").replace(".zip", "");
	}
	
	private void loadTexture() {
		DynamicTexture dynamicTexture;
		try {
			dynamicTexture = new DynamicTexture(getCurrentPack().getPackImage());
		} catch (Exception e) {
			dynamicTexture = TextureUtil.missingTexture;
		}
		this.currentPack = mc.getTextureManager().getDynamicTextureLocation("texturepackicon", dynamicTexture);
	}
	
	private IResourcePack getCurrentPack() {
		if (packs.size() > 0) {
			final IResourcePack last = packs.get(packs.size() - 1).getResourcePack();
			return last;
		}
		return mc.mcDefaultResourcePack;
	}
}