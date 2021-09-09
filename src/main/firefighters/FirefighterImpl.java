package main.firefighters;

import main.api.CityNode;
import main.api.Firefighter;

public class FirefighterImpl implements Firefighter {
  private int x;
  private int y;
  private int totalDistanceTravelled;

  public FirefighterImpl(CityNode fireStationLocation) {
    this.totalDistanceTravelled = 0;

    this.x = fireStationLocation.getX();
    this.y = fireStationLocation.getY();
  }

  @Override
  public CityNode getLocation() {
    // TODO
    return new CityNode(x, y);
  }

  @Override
  public int distanceTraveled() {
    // TODO
    return this.totalDistanceTravelled;
  }

  @Override
  public void travelToLocation(CityNode location) {
    int locationX = location.getX();
    int locationY = location.getY();

    // Calculate distance travelled
    int deltaX = Math.abs(locationX - this.x);
    int deltaY = Math.abs(locationY - this.y);

    // Update firefighter's location and total distance travelled
    this.x = locationX;
    this.y = locationY;
    this.totalDistanceTravelled += deltaX + deltaY;
  }
}
