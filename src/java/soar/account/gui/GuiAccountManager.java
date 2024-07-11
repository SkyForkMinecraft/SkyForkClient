package soar.account.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.imageio.ImageIO;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.EaseBackIn;
import cn.cedo.render.StencilUtil;
import cn.cedo.shader.RoundedUtil;
import cn.langya.font.FontManager;
import cn.langya.utils.MouseUtil;
import cn.yapeteam.util.TimerUtil;
import openauth.microsoft.MicrosoftAuthResult;
import openauth.microsoft.MicrosoftAuthenticationException;
import openauth.microsoft.MicrosoftAuthenticator;
import org.apache.commons.lang3.RandomStringUtils;

import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.union4dev.base.Access;
import soar.ClickEffect;
import soar.ColorUtils;
import soar.GlUtils;
import soar.account.Account;
import soar.account.AccountType;
import soar.account.SkinUtils;
import soar.animation.SimpleAnimation;

public class GuiAccountManager extends GuiScreen{

	private GuiScreen prevGuiScreen;
	
	private Animation showAccountAnimation;
	private boolean closeAccountManager;
	private boolean showAddAccount;
	
	private SimpleAnimation clickAnimation = new SimpleAnimation(0.0F);
	private boolean click;
	private TimerUtil clickTimer = new TimerUtil();
	
    public ResourceLocation faceTexture;
    
    private SimpleAnimation showAddAccountAnimation = new SimpleAnimation(0.0F);
    
    private boolean delete;
    private Account deleteAccount;
    
    private double scrollY;
    private SimpleAnimation scrollAnimation = new SimpleAnimation(0.0F);
    
    private SimpleAnimation addOpacityAnimation = new SimpleAnimation(0.0F);
    
    private GuiTransparentField usernameField;
    private SimpleAnimation selectAnimation = new SimpleAnimation(0.0F);
    
    private List<ClickEffect> clickEffects = new ArrayList<>();
    
    public GuiAccountManager(GuiScreen prevGuiScreen) {
    	this.prevGuiScreen = prevGuiScreen;
    }
    
	@Override
	public void initGui() {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		int addX = 140;
		int addY = 85;
		int x = sr.getScaledWidth() / 2 - addX;
		int y =  sr.getScaledHeight() / 2 - addY;
		
        showAddAccount = false;
        closeAccountManager = false;
        showAccountAnimation = new EaseBackIn(450, 1, 1.5F);
        usernameField = new GuiTransparentField(1, mc.fontRendererObj, x + 38, y + 65, 220, 22, getFont2Color(255).getRGB());
        click = false;
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
		int offsetY = 36;
        int index = 1;
        
		Color bg1Color = ColorUtils.getBackgroundColor(1);
		Color bg2Color = ColorUtils.getBackgroundColor(2);
		Color bg3Color = ColorUtils.getBackgroundColor(3);
		Color bg4Color = ColorUtils.getBackgroundColor(4);
		Color font1Color = ColorUtils.getFontColor(1);
		Color font2Color = ColorUtils.getFontColor(2);
		
		
        if(closeAccountManager) {
        	showAccountAnimation.setDirection(Direction.BACKWARDS);
        	if(showAccountAnimation.isDone()) {
        		closeAccountManager = false;
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
		
		RoundedUtil.drawRound(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0, bg1Color);
		
		FontManager.sicon24.drawString("N", 10, 10, font1Color.getRGB());
		FontManager.sicon24.drawString("Q", sr.getScaledWidth() - 20, 10, font1Color.getRGB());
		
		GlUtils.startScale(sr.getScaledWidth() / 2F, sr.getScaledHeight() / 2F, showAccountAnimation.getOutput().floatValue());
		
		RoundedUtil.drawRound(x, y, width, height, 6, bg3Color);
		
		RoundedUtil.drawRound(x, y, width, 26, 6, bg4Color);
		RoundedUtil.drawRound(x, y + 10, width, 16, 0, bg4Color);
		
		FontManager.M22.drawString("Account Manager", x + 10, y + 10, font1Color.getRGB());
		addOpacityAnimation.setAnimation(showAddAccount ? 0 : 255, 16);
		FontManager.M22.drawGradientStringWithShadow("+Add", (x + width) - 35, y + 10);
		
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x, y + 28, width, height - 28.5F, 6, Color.WHITE);
        StencilUtil.readStencilBuffer(1);
		
		showAddAccountAnimation.setAnimation(showAddAccount ? 0 : 140, 16);
		
		if(showAddAccount) {
			
			GlUtils.startTranslate(0, showAddAccountAnimation.getValue());
			
			RoundedUtil.drawRound(x + 15, y + 40, width - 30, height - 50, 6, bg4Color);
			
			FontManager.MB22.drawString("Offline Login", x + 35, y + 47, font2Color.getRGB());
			RoundedUtil.drawRound(x + 35, y + 64, 210, 20, 4, bg2Color);
			
			selectAnimation.setAnimation(usernameField.isFocused() || (!usernameField.isFocused() && !usernameField.getText().isEmpty()) ? 0 : 255, 16);
			
			FontManager.M20.drawString("Username", x + 42, y + 71, getFont2Color((int) selectAnimation.getValue()).getRGB());
			
			RoundedUtil.drawRound((x + width) - 135, y + 91, 100, 20, 4, bg3Color);
			FontManager.MB22.drawString("Login", x + 182, y + 96, font2Color.getRGB());
			
			RoundedUtil.drawRound(x + 35, y + 91, 100, 20, 4, bg3Color);
			FontManager.MB22.drawString("Gen Offline", x + 56, y + 96, font2Color.getRGB());
			
			clickAnimation.setAnimation(click ? 0.5F : 0, 100);
			
			mc.getTextureManager().bindTexture(new ResourceLocation("client/minecraft-background.png"));
			
			GlStateManager.enableBlend();
			RoundedUtil.drawRoundTextured(x + 35 + clickAnimation.getValue(), y + 120 + clickAnimation.getValue(), 210 - (clickAnimation.getValue() * 2), 30 - (clickAnimation.getValue() * 2), 4, 1.0F);
			GlStateManager.disableBlend();
			
			RoundedUtil.drawRound(x + 42, (y + height) - 44, 8, 8, 1, new Color(247, 78, 30));
			RoundedUtil.drawRound(x + 42 + 11, (y + height) - 44, 8, 8, 1, new Color(127, 186, 0));
			RoundedUtil.drawRound(x + 42, (y + height) - 44 + 11, 8, 8, 1, new Color(0, 164, 239));
			RoundedUtil.drawRound(x + 42 + 11, (y + height) - 44 + 11, 8, 8, 1, new Color(255, 185, 0));
			
			FontManager.MB22.drawString("Microsoft Login", x + 70, (y + height) - 40, Color.WHITE.getRGB());
			
			usernameField.drawTextBox();
			
			GlUtils.stopTranslate();
		}
		
		if(!showAddAccount) {
			
			GlUtils.startTranslate(0, (140 - showAddAccountAnimation.getValue()));
			
			if(Access.getInstance().getAccountManager().getAccounts().isEmpty()) {
				FontManager.MB26.drawString("Empty...", sr.getScaledWidth() / 2 - (FontManager.MB26.getStringWidth("Empty...") / 2), (sr.getScaledHeight() / 2) - (FontManager.MB26.getHeight() / 2), ColorUtils.getFontColor(2).getRGB());
			}
			
			for(Account a : Access.getInstance().getAccountManager().getAccounts()) {
				
				RoundedUtil.drawRound(x + 10, y + offsetY + scrollAnimation.getValue(), width - 20, 35, 4, bg4Color);
				
				if(a.getAccountType().equals(AccountType.MICROSOFT)) {
					mc.getTextureManager().bindTexture(face(a.getUsername(), UUIDTypeAdapter.fromString(a.getUuid())));
				}else {
					mc.getTextureManager().bindTexture(new ResourceLocation("client/head.png"));
				}

				GlStateManager.enableBlend();
				RoundedUtil.drawRoundTextured(x + 17, y + offsetY + 6 + scrollAnimation.getValue(), 24, 24, 4, 1.0F);
				GlStateManager.disableBlend();
				
				FontManager.M20.drawString(a.getUsername(), x + 50, y + offsetY + 15 + scrollAnimation.getValue(), font1Color.getRGB());
				
				a.opacityAnimation.setAnimation(a.isDone ? 0 : 255, 16);
				
				FontManager.M20.drawCenteredString(a.getInfo(), x + width - 54, y + 14.5F + offsetY + scrollAnimation.getValue(), ColorUtils.getFontColor(1, (int) a.opacityAnimation.getValue()).getRGB());
				
				FontManager.sicon20.drawString("M", x + width - 30, y + offsetY + 15 + scrollAnimation.getValue(),  new Color(255, 20, 20).getRGB());
				
				if(a.getInfo().equals(EnumChatFormatting.GREEN + "Success!") || a.getInfo().equals(EnumChatFormatting.RED + "Error :(")) {
					if(a.getTimer().delay(3500)) {
						a.isDone = true;
						a.getTimer().reset();
					}
				}else {
					a.getTimer().reset();
				}
				
				offsetY+=45;
				index++;
			}
			
			GlUtils.stopTranslate();
		}
		
		StencilUtil.uninitStencilBuffer();
		
		GlUtils.stopScale();
		
        final MouseUtil.Scroll scroll = MouseUtil.scroll();

        if(scroll != null) {
        	switch (scroll) {
        	case DOWN:
        		if(index > 4){
            		if(scrollY > -((index - 3.5) * 45)) {
                		scrollY -=20;
            		}
            		
            		if(index > 4) {
                		if(scrollY < -((index - 3.8) * 45)) {
                			scrollY = -((index - 3.9) * 45);
                		}
            		}
        		}else {
        			scrollY = 0;
        		}
        		break;
            case UP:
        		if(scrollY > 0) {
        			scrollY = -18;
        		}
        		
        		if(scrollY < -0) {
            		scrollY +=20;
        		}else {
            		if(index > 5) {
            			scrollY = 10;
            		}
        		}
        		break;
        	}
        }
        
        scrollAnimation.setAnimation((float) scrollY, 16);
        
        if(delete) {
        	Access.getInstance().getAccountManager().getAccounts().remove(deleteAccount);
			scrollY = 0;
        	delete = false;
        }
        
        //Copyright
        FontManager.M20.drawString("Copyright Mojang AB. Do not distribute!", sr.getScaledWidth() - FontManager.MB20.getStringWidth("Copyright Mojang AB. Do not distribute!") + 4, sr.getScaledHeight() - FontManager.MB20.getHeight() - 3, font2Color.getRGB());
        
        if(clickEffects.size() > 0) {
            Iterator<ClickEffect> clickEffectIterator= clickEffects.iterator();
            while(clickEffectIterator.hasNext()){
                ClickEffect clickEffect = clickEffectIterator.next();
                clickEffect.draw();
                if (clickEffect.canRemove()) clickEffectIterator.remove();
            }
        }
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		int addX = 140;
		int addY = 85;
		int x = sr.getScaledWidth() / 2 - addX;
		int y =  sr.getScaledHeight() / 2 - addY;
		int width = addX * 2;
		int offsetY = 36;
		
        ClickEffect clickEffect = new ClickEffect(mouseX, mouseY);
        clickEffects.add(clickEffect);
        
		if(mouseButton == 0) {
			
			if(MouseUtil.isInside(mouseX, mouseY, (x + width) - 50, y, 50, 26)) {
				showAddAccount = true;
			}
			
			if(showAddAccount) {
				
				usernameField.mouseClicked(mouseX, mouseY, mouseButton);
				
				if(MouseUtil.isInside(mouseX, mouseY, x + 35, y + 120, 210, 30)) {
					click = true;
					new Thread(() -> {
                        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                        try {
                            MicrosoftAuthResult acc = authenticator.loginWithWebview();
                            Access.getInstance().getAccountManager().getAccounts().add(new Account(AccountType.MICROSOFT, acc.getProfile().getName(), acc.getProfile().getId(), acc.getRefreshToken()));
							mc.session = (new Session(acc.getProfile().getName(), acc.getProfile().getId(), acc.getAccessToken(), "legacy"));
                            showAddAccount = false;
                        } catch (MicrosoftAuthenticationException e) {
                            e.printStackTrace();
                        }
                    }).start();
				}
				
				if(MouseUtil.isInside(mouseX, mouseY, x + 35, y + 91, 100, 20)) {
					Random random = new Random();
					int randomValue = random.nextInt(8) + 3;
					
					String username = RandomStringUtils.randomAlphabetic(randomValue);
					
					Access.getInstance().getAccountManager().getAccounts().add(new Account(AccountType.CRACKED, username, "0", "0"));
					mc.session = (new Session(username, "0", "0", "legacy"));
					showAddAccount = false;
				}
				
				if(MouseUtil.isInside(mouseX, mouseY, (x + width) - 135, y + 91, 100, 20)) {
					if(!usernameField.getText().isEmpty()) {
						Access.getInstance().getAccountManager().getAccounts().add(new Account(AccountType.CRACKED, usernameField.getText(), "0", "0"));
						mc.session = (new Session(usernameField.getText(), "0", "0", "legacy"));
						showAddAccount = false;
						usernameField.setText("");
					}
				}
			}
			
			for(Account a : Access.getInstance().getAccountManager().getAccounts()) {
				
				if(MouseUtil.isInside(mouseX, mouseY, x + width - 36, y + offsetY + 7 + scrollAnimation.getValue(), 20, 20)) {
					deleteAccount = a;
					delete = true;
				}
				
				if(!showAddAccount) {
					if(MouseUtil.isInside(mouseX, mouseY, x + 10, y + offsetY + scrollAnimation.getValue(), width - 50, 35)) {
						a.isDone = false;
						
						if(a.getAccountType().equals(AccountType.MICROSOFT)) {
							
							new Thread(() -> {
                                MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
                                a.setInfo("Loading...");
                                try {
                                    MicrosoftAuthResult acc = authenticator.loginWithRefreshToken(a.getToken());
                                    mc.session = (new Session(acc.getProfile().getName(), acc.getProfile().getId(), acc.getAccessToken(), "legacy"));
                                    Access.getInstance().getAccountManager().setCurrentAccount(Access.getInstance().getAccountManager().getAccountByUsername(acc.getProfile().getName()));
                                    a.setInfo(EnumChatFormatting.GREEN + "Success!");
                                } catch (MicrosoftAuthenticationException e) {
                                    e.printStackTrace();
                                    a.setInfo(EnumChatFormatting.RED + "Error :(");
                                }
                            }).start();
						}
						
						if(a.getAccountType().equals(AccountType.CRACKED)) {
							a.setInfo(EnumChatFormatting.GREEN + "Success!");
							mc.session = (new Session(a.getUsername(), "0", "0", "legacy"));
							Access.getInstance().getAccountManager().setCurrentAccount(Access.getInstance().getAccountManager().getAccountByUsername(a.getUsername()));
						}
					}
				}
				
				offsetY+=45;
			}
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		
		if(showAddAccount) {
			usernameField.textboxKeyTyped(typedChar, keyCode);
		}
		
		if(keyCode == 1) {
			if(showAddAccount) {
				showAddAccount = false;
			}else {
				Access.getInstance().getAccountManager().save();
				closeAccountManager = true;
			}
		}
	}
	
	@Override
	public void updateScreen() {
		if(showAddAccount) {
			usernameField.updateCursorCounter();
		}
	}
	
    private ResourceLocation face(String username, UUID uuid) {
    	
        File model = new File(new File(mc.mcDataDir, "skyfork/cachedImages/models"), username + ".png");
        File face = new File(new File(mc.mcDataDir, "skyfork/cachedImages/faces"), username + ".png");

        SkinUtils.loadSkin(mc, username, uuid, model, face);

        try {
            BufferedImage t = ImageIO.read(face);
            DynamicTexture nibt = new DynamicTexture(t);

            this.faceTexture = mc.getTextureManager().getDynamicTextureLocation("iasface_" + username.hashCode(), nibt);
        } catch (Throwable throwable) {
            this.faceTexture = new ResourceLocation("iaserror", "skin");
        }

        return this.faceTexture;
    }
    
    private Color getFont2Color(int opacity) {
    	
    	return ColorUtils.getFontColor(2, opacity);
    }
}