package org.union4dev.base.module;

import lombok.Getter;

@Getter
public enum Category {
    Movement("玩家"),
    Render("视觉"),
    Misc("杂项"),
    Client("客户端");
    Category(String name) {
        this.name = name;
    }

    private final String name;
}
