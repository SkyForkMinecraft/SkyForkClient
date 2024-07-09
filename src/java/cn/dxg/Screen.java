package cn.dxg;

import org.union4dev.base.Access;

public interface Screen extends Access.InstanceAccess
{
    default void onDrag(final int mouseX, final int mouseY) {
    }
    
    void initGui();
    
    void keyTyped(final char p0, final int p1);
    
    void drawScreen(final int p0, final int p1);
    
    void mouseClicked(final int p0, final int p1, final int p2);
    
    void mouseReleased(final int p0, final int p1, final int p2);
}
