package com.tsoft.ui.layout;

import java.awt.LayoutManager;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;

public class MigLayoutHelper {
    private MigLayoutHelper() { }
    
    public static void createSeparator(JPanel panel, String text) {
        JLabel label = createLabel(text);
        panel.add(label, "gapbottom 1, span, split 2, aligny center");
        panel.add(new JSeparator(), "gapleft rel, growx");
    }

    public static JLabel createLabel(String text) {
        return createLabel(text, SwingConstants.LEADING);
    }

    public static JLabel createLabel(String text, int align) {
        final JLabel label = new JLabel(text, align);
        return label;
    }

    public static JPanel createTabPanel(LayoutManager lm) {
        JPanel panel = new JPanel(lm);
        panel.setOpaque(false);
        return panel;
    }

    public static JComponent createPanel() {
        return createPanel("");
    }

    public static JComponent createPanel(String s) {
        JLabel panel = new JLabel(s, SwingConstants.CENTER) {
            @Override
            public void addNotify() {
                super.addNotify();
                if (getText().length() == 0) {
                    String lText = (String) ((MigLayout) getParent().getLayout()).getComponentConstraints(this);
                    setText(lText != null && lText.length() > 0 ? lText : "<Empty>");
                }
            }
        };

        panel.setBorder(new EtchedBorder());
        panel.setOpaque(true);
        return panel;
    }
}
