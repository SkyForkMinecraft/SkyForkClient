package cn.langya.modules.misc;

import org.union4dev.base.annotations.module.Enable;

import javax.swing.*;

/**
 * @author LangYa466
 * @date 2024/4/25 20:51
 */

public class About {

    @Enable
    void onEnable() {
        JOptionPane.showMessageDialog(null,"This client base is github.com/cubk1/clientbase , client by langya");
    }

}
