package com.skyfork.api.starx;

import com.skyfork.client.value.impl.NumberValue;

// @ModuleInfo(name = "SkinLayers3D", description = "Extend your skin with extra layers", chineseDescription = "3D渲染你的皮肤层", category = Category.ADDONS)
public class SkinLayers3D {

    public static final NumberValue baseVoxelSize = new NumberValue("Voxel Size", 1.15, 1.01, 1.4, 0.01);
    public static final NumberValue bodyVoxelSize = new NumberValue("Torso Voxel Width", 1.05, 1.01, 1.4, 0.01);
    public static final NumberValue headVoxelSize = new NumberValue("Head Voxel Size", 1.18, 1.01, 1.25, 0.01);
    public static final NumberValue renderDistance = new NumberValue("Level Of Detail Distance",14, 5, 40, 1);
}
