package boundary.element;

import entity.Answer;

import javax.swing.*;
import java.awt.*;

public class AnswerLogCellRenderer extends JPanel implements ListCellRenderer<Object> {

    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        return new AnswerLogPanel((Answer) value);
    }
}