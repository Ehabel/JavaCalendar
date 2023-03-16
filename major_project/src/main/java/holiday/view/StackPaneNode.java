package holiday.view;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import java.time.LocalDate;

public class StackPaneNode extends StackPane {
    private LocalDate date;

    /**
     * Creates a new Stackpane node that can store information such as the date.
     * @param children The set of children for this pane.
     */
    public StackPaneNode(Node... children) {
        super(children);
    }

    /**
     * Get the date stored in the stackpane.
     * @return The date associated with the stackpane
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date for the current stackpane.
     * @param date The date we want to set for the current stackpane.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

}