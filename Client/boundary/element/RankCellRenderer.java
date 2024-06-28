package boundary.element;

import entity.Rank;

import javax.swing.*;
import java.awt.*;

public class RankCellRenderer extends JPanel implements ListCellRenderer<Object> {

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new RankPanel((Rank) value);
    }
}