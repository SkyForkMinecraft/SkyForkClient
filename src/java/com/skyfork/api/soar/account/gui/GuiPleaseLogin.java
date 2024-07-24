package com.skyfork.api.soar.account.gui;

import java.awt.Color;
import java.util.Random;

import com.skyfork.api.cedo.animations.Animation;
import com.skyfork.api.cedo.animations.Direction;
import com.skyfork.api.cedo.animations.impl.EaseBackIn;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.langya.utils.MouseUtil;
import com.skyfork.api.soar.ColorUtils;
import com.skyfork.api.soar.account.Account;
import com.skyfork.api.soar.account.AccountType;
import com.skyfork.api.yapeteam.util.TimerUtil;
import org.apache.commons.lang3.RandomStringUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import openauth.microsoft.MicrosoftAuthResult;
import openauth.microsoft.MicrosoftAuthenticationException;
import openauth.microsoft.MicrosoftAuthenticator;
import com.skyfork.client.Access;
import com.skyfork.api.soar.GlUtils;
import com.skyfork.api.soar.animation.SimpleAnimation;

public class GuiPleaseLogin extends GuiScreen{

	private GuiScreen prevGuiScreen;
	
	private Animation introAnimation;
    private GuiTransparentField usernameField;
    
	private SimpleAnimation clickAnimation = new SimpleAnimation(0.0F);
	private boolean click;
	private TimerUtil clickTimer = new TimerUtil();
    private SimpleAnimation selectAnimation = new SimpleAnimation(0.0F);
    private boolean close;
    
    public GuiPleaseLogin(GuiScreen prevGuiScreen) {
    	this.prevGuiScreen = prevGuiScreen;
    }
    
	@Override
	public void initGui() {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		int addX = 140;
		int addY = 85;
		int x = sr.getScaledWidth() / 2 - addX;
		int y =  sr.getScaledHeight() / 2 - addY;
		
		introAnimation = new EaseBackIn(450, 1, 1.5F);
        usernameField = new GuiTransparentField(1, mc.fontRendererObj, x + 38, y + 65, 220, 22, -1);
        click = false;
        close = false;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		int addX = 140;
		int addY = 85;
		int x = sr.getScaledWidth() / 2 - addX;
		int y =  sr.getScaledHeight() / 2 - addY;
		int width = addX * 2;
		int height = addY * 2;
		
        if(close) {
        	introAnimation.setDirection(Direction.BACKWARDS);
        	if(introAnimation.isDone()) {
        		close = false;
        		mc.displayGuiScreen(prevGuiScreen);
        	}
        }
        
		if(click) {
			if(clickTimer.delay(150)) {
				click = false;
			}
		}else {
			clickTimer.reset();
		}
		
		//�w�i�̕`��
		RoundedUtil.drawRound(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0, ColorUtils.getBackgroundColor(1));
		
		FontManager.sicon24.drawString("N", 10, 10, ColorUtils.getFontColor(1).getRGB());
		
		GlUtils.startScale(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2,  introAnimation.getOutput().floatValue());
		
		//�w�i�`��
		RoundedUtil.drawRound(x, y, width, height, 6, ColorUtils.getBackgroundColor(3));
		
		//��̃o�[�`��
		RoundedUtil.drawRound(x, y, width, 26, 6, ColorUtils.getBackgroundColor(4));
		RoundedUtil.drawRound(x, y + 10, width, 16, 0, ColorUtils.getBackgroundColor(4));
		
		FontManager.M22.drawString("Account Manager - Please Login", x + 10, y + 10, ColorUtils.getFontColor(1).getRGB());
		
		RoundedUtil.drawRound(x + 15, y + 40, width - 30, height - 50, 6, ColorUtils.getBackgroundColor(4));
		
		//Cracked���O�C��
		FontManager.MB22.drawString("Offline Login", x + 35, y + 47, ColorUtils.getFontColor(2).getRGB());
		RoundedUtil.drawRound(x + 35, y + 64, 210, 20, 4, ColorUtils.getBackgroundColor(2));
		
		selectAnimation.setAnimation(usernameField.isFocused() || (!usernameField.isFocused() && !usernameField.getText().isEmpty()) ? 0 : 255, 16);
		
		FontManager.M20.drawString("Username", x + 42, y + 71, ColorUtils.getFontColor(2, (int) selectAnimation.getValue()).getRGB());
		
		RoundedUtil.drawRound((x + width) - 135, y + 91, 100, 20, 4, ColorUtils.getBackgroundColor(3));
		FontManager.MB22.drawString("Login", x + 182, y + 96, ColorUtils.getFontColor(2).getRGB());
		
		RoundedUtil.drawRound(x + 35, y + 91, 100, 20, 4, ColorUtils.getBackgroundColor(3));
		FontManager.MB22.drawString("Gen Offline", x + 56, y + 96, ColorUtils.getFontColor(2).getRGB());
		
		clickAnimation.setAnimation(click ? 0.5F : 0, 100);
		
		//Microsoft���O�C��
		mc.getTextureManager().bindTexture(new ResourceLocation("com/skyfork/api/soar/minecraft-background.png"));
		
		GlStateManager.enableBlend();
		RoundedUtil.drawRoundTextured(x + 35 + clickAnimation.getValue(), y + 120 + clickAnimation.getValue(), 210 - (clickAnimation.getValue() * 2), 30 - (clickAnimation.getValue() * 2), 4, 1.0F);
		GlStateManager.disableBlend();
		
		RoundedUtil.drawRound(x + 42, (y + height) - 44, 8, 8, 1, new Color(247, 78, 30));
		RoundedUtil.drawRound(x + 42 + 11, (y + height) - 44, 8, 8, 1, new Color(127, 186, 0));
		RoundedUtil.drawRound(x + 42, (y + height) - 44 + 11, 8, 8, 1, new Color(0, 164, 239));
		RoundedUtil.drawRound(x + 42 + 11, (y + height) - 44 + 11, 8, 8, 1, new Color(255, 185, 0));
		
		FontManager.MB22.drawString("Microsoft Login", x + 70, (y + height) - 40, Color.WHITE.getRGB());
		
		usernameField.drawTextBox();
		
		GlUtils.stopScale();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		int addX = 140;
		int addY = 85;
		int x = sr.getScaledWidth() / 2 - addX;
		int y =  sr.getScaledHeight() / 2 - addY;
		int width = addX * 2;
		
		usernameField.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(MouseUtil.isInside(mouseX, mouseY, x + 35, y + 120, 210, 30)) {
			click = true;
			new Thread() {
				@Override
				public void run() {
					MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
					try {
						MicrosoftAuthResult acc = authenticator.loginWithWebview();
						Access.getInstance().getAccountManager().getAccounts().add(new Account(AccountType.MICROSOFT, acc.getProfile().getName(), acc.getProfile().getId(), acc.getRefreshToken()));
						mc.session = (new Session(acc.getProfile().getName(), acc.getProfile().getId(), acc.getAccessToken(), "legacy"));
						close = true;
						Access.getInstance().getAccountManager().isFirstLogin = false;
						Access.getInstance().getAccountManager().setCurrentAccount(Access.getInstance().getAccountManager().getAccountByUsername(acc.getProfile().getName()));
						Access.getInstance().getAccountManager().save();
					} catch (MicrosoftAuthenticationException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
		
		if(MouseUtil.isInside(mouseX, mouseY, x + 35, y + 91, 100, 20)) {
			Random random = new Random();
			int randomValue = random.nextInt(8) + 3;
			
			String username = RandomStringUtils.randomAlphabetic(randomValue);
			
			Access.getInstance().getAccountManager().getAccounts().add(new Account(AccountType.CRACKED, username, "0", "0"));
			mc.session = (new Session(username, "0", "0", "legacy"));
			close = true;
			Access.getInstance().getAccountManager().isFirstLogin = false;
			Access.getInstance().getAccountManager().setCurrentAccount(Access.getInstance().getAccountManager().getAccountByUsername(username));
			Access.getInstance().getAccountManager().save();
		}
		
		if(MouseUtil.isInside(mouseX, mouseY, (x + width) - 135, y + 91, 100, 20)) {
			if(!usernameField.getText().isEmpty()) {
				
				String username = usernameField.getText();
				
				Access.getInstance().getAccountManager().getAccounts().add(new Account(AccountType.CRACKED, username, "0", "0"));
				mc.session =(new Session(username, "0", "0", "legacy"));
				usernameField.setText("");
				close = true;
				Access.getInstance().getAccountManager().isFirstLogin = false;
				Access.getInstance().getAccountManager().setCurrentAccount(Access.getInstance().getAccountManager().getAccountByUsername(username));
				Access.getInstance().getAccountManager().save();
			}
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		usernameField.textboxKeyTyped(typedChar, keyCode);
	}
	
	@Override
	public void updateScreen() {
		usernameField.updateCursorCounter();
	}
}