package org.insa.graph;

import java.util.List;

/**
 * Interface representing an arc in the graph - Arc is an interface and not a
 * class to allow us to represent two-ways roads in a memory efficient manner
 * (without having to duplicate attributes).
 * 
 * Arc should never be created manually but always using the
 * {@++Node#linkNodes(Node, Node, float, RoadInformation, java.util.ArrayList)}
 * method to ensure proper instantiation of the {@link ArcForward} and
 * {@link ArcBackward} classes.
 * 
 * @see ArcForward
 * @see ArcBackward
 *
 */
public abstract class Arc {

    /**
     * @return Origin node of this arc.
     */
    public abstract Node getOrigin();

    /**
     * @return Destination node of this arc.
     */
    public abstract Node getDestination();

    /**
     * @return Length of this arc, in meters.
     */
    public abstract float getLength();

    /**
     * Compute the time required to travel this arc if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this arc at the given speed (in
     *         kilometers-per-hour).
     */
    public double getTravelTime(double speed) {
        return getLength() * 3600.0 / (speed * 1000.0);
    }

    /**
     * Compute and return the minimum time required to travel this arc, or the time
     * required to travel this arc at the maximum speed allowed.
     * 
     * @return Minimum time required to travel this arc, in seconds.
     * 
     * @see Arc#getTravelTime(double)
     */
    public double getMinimumTravelTime() {
        return getTravelTime(getRoadInformation().getMaximumSpeed());
    }

    /**
     * @return Road information for this arc.
     */
    public abstract RoadInformation getRoadInformation();

    /**
     * @return Points representing segments of this arc.
     */
    public abstract List<Point> getPoints();
}
