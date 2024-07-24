package com.skyfork.api.starx.skinlayers3d;

public interface PlayerEntityModelAccessor
{
    boolean hasThinArms();

    HeadLayerFeatureRenderer getHeadLayer();

    BodyLayerFeatureRenderer getBodyLayer();
}
