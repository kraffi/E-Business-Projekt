package e_business_projekt.e_business_projekt.Maps_Navigation;

import java.util.ArrayList;

public interface RoutingListener {
    void onRoutingFailure();

    void onRoutingStart();

    void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex);

    void onRoutingCancelled();
}
