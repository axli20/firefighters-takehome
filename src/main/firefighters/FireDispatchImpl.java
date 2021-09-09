package main.firefighters;

import java.util.ArrayList;
import java.util.List;

import main.api.*;
import main.api.exceptions.NoFireFoundException;

public class FireDispatchImpl implements FireDispatch {
  private City city;
  private List<Firefighter> stationFirefighters;
  private List<Firefighter> dispatchedFirefighters;

  public FireDispatchImpl(City city) {
    // TODO
    this.city = city;
    this.stationFirefighters = new ArrayList<>();
    this.dispatchedFirefighters = new ArrayList<>();
  }

  @Override
  public void setFirefighters(int numFirefighters) {
    // TODO
    for (int i = 0; i < numFirefighters; i++) {
      this.stationFirefighters.add(new FirefighterImpl(city.getFireStation().getLocation()));
    }
  }

  @Override
  public List<Firefighter> getFirefighters() {
    // TODO
    List<Firefighter> allFirefighters = new ArrayList<>(stationFirefighters);
    allFirefighters.addAll(dispatchedFirefighters);

    return allFirefighters;
  }

  @Override
  public void dispatchFirefighters(CityNode... burningBuildings) {
    // TODO
    int numBurningBuildings = burningBuildings.length;

    // Using a nearest neighbor type of approximation.
    while (numBurningBuildings > 0) {
      boolean dispatchedFromStation = false;

      Firefighter closestFirefighter = null;
      CityNode nextLocation = null;
      int minDistance = Integer.MAX_VALUE;

      // For all dispatched firefighters, find each firefighter's closest fire,
      // and save the closest fire out of all firefighters.
      for (Firefighter f : dispatchedFirefighters) {
        CityNode closestFire = findClosestFire(f, burningBuildings);
        int distance = getDistance(f.getLocation(), closestFire);

        if (distance < minDistance) {
          minDistance = distance;
          nextLocation = closestFire;
          closestFirefighter = f;
        }
      }

      // Check if there are any firefighters at the station.
      // If so, find the closest fire to the station and compare distance to saved closest fire.
      if (!stationFirefighters.isEmpty()) {
        Firefighter stationFirefighter = stationFirefighters.get(0);
        CityNode closestFireToStation = findClosestFire(stationFirefighter, burningBuildings);

        if (getDistance(city.getFireStation().getLocation(), closestFireToStation) < minDistance) {
          closestFirefighter = stationFirefighter;
          nextLocation = closestFireToStation;
          dispatchedFromStation = true;
        }
      }

      // Dispatch the closest firefighter to the next location.
      try {
        dispatchFirefighter(closestFirefighter, nextLocation);

        // Update the state of the firefighter (stationed or dispatched).
        if (dispatchedFromStation) {
          dispatchedFirefighters.add(closestFirefighter);
          stationFirefighters.remove(closestFirefighter);
        }
        numBurningBuildings--;
      } catch (NoFireFoundException e) {
        System.out.println("No fire found at location " + nextLocation.getX() + ", " + nextLocation.getY());
      }

    }
  }

  private void dispatchFirefighter(Firefighter firefighter, CityNode location) throws NoFireFoundException {
    firefighter.travelToLocation(location);

    Building burningBuilding = city.getBuilding(location);
    burningBuilding.extinguishFire();
  }

  private CityNode findClosestFire(Firefighter firefighter, CityNode... burningBuildings) {
    int minDistance = Integer.MAX_VALUE;
    CityNode closestFireLocation = null;

    for (int i = 0; i < burningBuildings.length; i++) {
      CityNode location = burningBuildings[i];
      Building b = city.getBuilding(location);
      if (b.isBurning()) {
        int distance = getDistance(firefighter.getLocation(), location);
        if (distance < minDistance) {
          minDistance = distance;
          closestFireLocation = location;
        }
      }
    }

    return closestFireLocation;
  }

  private int getDistance(CityNode start, CityNode target) {
    int deltaX = Math.abs(start.getX() - target.getX());
    int deltaY = Math.abs(start.getY() - target.getY());

    return deltaX + deltaY;
  }
}
