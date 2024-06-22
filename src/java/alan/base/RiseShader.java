package alan.base;

import lombok.Getter;
import lombok.Setter;
import org.union4dev.base.Access;

import java.util.List;

@Getter
@Setter
public abstract class RiseShader implements Access.InstanceAccess {
    private boolean active;

    public abstract void run(ShaderRenderType type, float partialTicks, List<Runnable> runnable);

    public abstract void update();
}