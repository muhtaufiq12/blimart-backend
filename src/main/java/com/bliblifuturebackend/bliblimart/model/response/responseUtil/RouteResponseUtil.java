package com.bliblifuturebackend.bliblimart.model.response.responseUtil;

import com.bliblifuturebackend.bliblimart.model.entity.Route;
import com.bliblifuturebackend.bliblimart.model.response.RouteResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteResponseUtil {

    public RouteResponse getDirection(Route route, String currentMark) {
        RouteResponse response = new RouteResponse();
        response.setId(route.getId());

        List<String> routeString = route.getRoute();
        for (int i = 0; i < routeString.size(); i++) {
            if (routeString.get(i).equals(currentMark)){
                if (i == routeString.size()-1){
                    response.setNext("done");
                }
                else {
                    response.setNext(routeString.get(i+1));
                }
                break;
            }
        }

        char next = response.getNext().charAt(1);
        int direction = -1;
        switch (next){
            case 's':
                direction = 180;
                break;
            case 'e':
                direction = -90;
                break;
            case 'w':
                direction = 90;
                break;
            case 'n':
                direction = 0;
                break;
            default:
                throw new IllegalArgumentException("Error route");
        }
//        if (direction < 0){
//            throw new IllegalArgumentException("Error route");
//        }

        response.setRotation(direction);
        return response;
    }

}
