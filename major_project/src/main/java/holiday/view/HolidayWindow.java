package holiday.view;

import javafx.scene.Node;
import org.controlsfx.control.WorldMapView;

import java.util.ArrayList;
import java.util.List;


public class HolidayWindow {
    private WorldMapView wmv;
    private List<Node> allNodes = new ArrayList<>();

    /**
     * Create a new window that displays the world map.
     */
    public HolidayWindow(){
        this.wmv = new WorldMapView();
        this.wmv.setCountrySelectionMode(WorldMapView.SelectionMode.SINGLE);
    }

    /**
     * Get the world map view.
     * @return The world map view.
     */
    public WorldMapView getWmv(){
        return this.wmv;
    }

    /**
     * Add the world map view to the list of nodes we want to display.
     */
    public void setUpNodes(){
        this.allNodes.add(this.wmv);
    }

    /**
     * Get a list of nodes currently stored in the Holiday window.
     * @return A list of nodes.
     */
    public List<Node> getNodes(){
        return this.allNodes;
    }

}