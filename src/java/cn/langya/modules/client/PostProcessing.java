package cn.langya.modules.client;

import net.minecraft.client.shader.Framebuffer;
import org.union4dev.base.Access;
import org.union4dev.base.events.EventManager;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;
import skid.cedo.shader.blur.BloomUtil;
import skid.cedo.shader.blur.BlurUtil;
import skid.cedo.shader.blur.GaussianBlur;

public class PostProcessing implements Access.InstanceAccess {

    public final BooleanValue blur = new BooleanValue("Blur", false);
    private final NumberValue blurRadius = new NumberValue("Blur Radius", 8, 1, 25, 1);
    private final BooleanValue bloom = new BooleanValue("Bloom", true);
    private final NumberValue bloomRadius = new NumberValue("Bloom Radius", 10, 1, 25, 1);

    public void blurScreen() {

        if (blur.getValue()) {
            GaussianBlur.startBlur();
            EventManager.call(new ShaderEvent(true));
            GaussianBlur.endBlur(blurRadius.getValue().floatValue(), 2);
        }

    }


}
