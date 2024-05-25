package cn.imflowow;

import net.minecraft.util.AxisAlignedBB;
import org.union4dev.base.events.base.Cancellable;
import org.union4dev.base.events.base.Event;

public class BlockOverlayEvent implements Cancellable, Event {
	AxisAlignedBB axisalignedbb;
	boolean cancelled;

	public BlockOverlayEvent(AxisAlignedBB axisalignedbb) {
		this.axisalignedbb = axisalignedbb;
	}

	public AxisAlignedBB getBB() {
		return this.axisalignedbb;
	}

	public void setBB(AxisAlignedBB axisalignedbb) {
		this.axisalignedbb = axisalignedbb;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean state) {
		this.cancelled = state;
	}
}
