package com.skyfork.api.imflowow;

import com.skyfork.api.cedo.misc.ColorUtil;
import com.skyfork.client.Access;
import com.skyfork.client.value.impl.ComboValue;
import com.skyfork.client.value.impl.NumberValue;

import java.awt.*;

public class EnchantEffect  {

	private static final ComboValue colorMode = new ComboValue("颜色模式","客户端","客户端","自定义","彩虹");
	private static final NumberValue customColorRed = new NumberValue("自定义红色",255,0,255,5);
	private static final NumberValue customColorGreen = new NumberValue("自定义绿色",255,0,255,5);
	private static final NumberValue customColorBlue = new NumberValue("自定义蓝色",255,0,255,5);

	public static Color getColor() {
		Color color = null;
		switch (colorMode.getValue()) {
			case "自定义" : color = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue()); break;
			case "客户端": color = Access.CLIENT_COLOR; break;
			case "彩虹": color = new Color(ColorUtil.getColor(-(1 + 5 * 1.7f), 0.7f, 1));
		}
		return color;
	}
}
