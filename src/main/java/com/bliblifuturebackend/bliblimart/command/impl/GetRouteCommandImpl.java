package com.bliblifuturebackend.bliblimart.command.impl;

import com.bliblifuturebackend.bliblimart.command.GetRouteCommand;
import com.bliblifuturebackend.bliblimart.model.entity.Mark;
import com.bliblifuturebackend.bliblimart.model.entity.Product;
import com.bliblifuturebackend.bliblimart.model.entity.Route;
import com.bliblifuturebackend.bliblimart.model.request.RouteRequest;
import com.bliblifuturebackend.bliblimart.model.response.RouteResponse;
import com.bliblifuturebackend.bliblimart.model.response.responseUtil.RouteResponseUtil;
import com.bliblifuturebackend.bliblimart.model.util.Node;
import com.bliblifuturebackend.bliblimart.repository.MarkRepository;
import com.bliblifuturebackend.bliblimart.repository.ProductRepository;
import com.bliblifuturebackend.bliblimart.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class GetRouteCommandImpl implements GetRouteCommand {

    @Autowired
    private MarkRepository markRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RouteResponseUtil routeResponseUtil;

    @Override
    public Mono<RouteResponse> execute(RouteRequest request) {
        return productRepository.findById(request.getProductId())
                .flatMap(product -> routeRepository
                        .findByStartingmarkAndTargetMark(request.getStartingMark(), product.getMark1())
                        .switchIfEmpty(routeRepository.findByStartingmarkAndTargetMark(request.getStartingMark(), product.getMark2()))
                        .switchIfEmpty(calculateAndSaveRoute(request, product))
                )
                .map(route -> routeResponseUtil.getDirection(route, request.getStartingMark()));
    }

    private Mono<Route> calculateAndSaveRoute(RouteRequest request, Product product) {
        return markRepository.findAll()
                .collectList()
                .map(marks -> createRoute(request, marks, product))
                .flatMap(route -> routeRepository.save(route));
    }

    private Route createRoute(RouteRequest request, List<Mark> marks, Product product) {
        Route route = Route.builder().startingmark(request.getStartingMark()).build();

        route.setId(UUID.randomUUID().toString());

        Date date = new Date();
        route.setCreatedDate(date);
        route.setUpdatedDate(date);

        String[][] markArray = getMarkArray(marks);

        int[] startPos = getPosition(request.getStartingMark(), markArray);
        Node startNode = Node.builder().fCost(0).gCost(0).hCost(0)
                .position(startPos)
                .build();

        int[] endPos1 = getPosition(product.getMark1(), markArray);
        Node endNode1 = Node.builder().fCost(0).gCost(0).hCost(0)
                .position(endPos1)
                .build();

        int[] endPos2 = getPosition(product.getMark2(), markArray);
        Node endNode2 = Node.builder().fCost(0).gCost(0).hCost(0)
                .position(endPos2)
                .build();

        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();

        openList.add(startNode);

        while(openList.size() > 0){
            Node currentNode = openList.get(0);
            int currentIndex = 0;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).getFCost() < currentNode.getFCost()){
                    currentNode = openList.get(i);
                    currentIndex = i;
                }
            }

            openList.remove(currentIndex);
            closedList.add(currentNode);

            boolean arrivedAtTarget1 = Arrays.equals(currentNode.getPosition(), endNode1.getPosition());
            boolean arrivedAtTarget2 = Arrays.equals(currentNode.getPosition(), endNode2.getPosition());
            if ( arrivedAtTarget1 || arrivedAtTarget2 )
            {
                List<int[]> posRoute = new ArrayList<>();
                Node current = currentNode;
                while (current != null){
                    posRoute.add(current.getPosition());
                    current = current.getParent();
                }
                Collections.reverse(posRoute);
                List<String> theRoute = getStringRoute(posRoute, request.getStartingMark(), markArray);
                route.setRoute(theRoute);

                if (arrivedAtTarget1){
                    route.setTargetMark(product.getMark1());
                }
                else {
                    route.setTargetMark(product.getMark2());
                }
                return route;
            }

            List<Node> children = new ArrayList<>();
            for (int[] newPosition : new int[][] { {1,0}, {-1,0}, {0,1}, {0,-1} } ) {
                int[] nodePosition = new int[]{
                        currentNode.getPosition()[0]+newPosition[0], currentNode.getPosition()[1]+newPosition[1]
                };

                if (nodePosition[0] < 0 || nodePosition[1] < 0 || nodePosition[0] > markArray.length-1 ||  nodePosition[1] > markArray[0].length-1){
                    continue;
                }

                Node newNode = Node.builder().parent(currentNode).position(nodePosition).build();
                children.add(newNode);
            }

            for (Node child : children) {
                boolean skip = false;
                for (Node closedNode : closedList) {
                    if (closedNode.equals(child)){
                        skip = true;
                        break;
                    }
                }
                if (skip) continue;

                child.setGCost(currentNode.getGCost() + 1);
                child.setHCost(Math.min(findHCost(child, endNode1), findHCost(child, endNode2)));
                child.setFCost(child.getGCost() + child.getHCost());

                for (Node openNode : openList) {
                    if (openNode.equals(child) && child.getGCost() > openNode.getGCost()){
                        skip = true;
                        break;
                    }
                }

                if (skip) continue;

                openList.add(child);
            }
        }
        throw new NullPointerException("Route not found");
    }

    private int findHCost(Node child, Node endNode1) {
        return Math.abs(
                child.getPosition()[0] + endNode1.getPosition()[0]) + Math.abs(child.getPosition()[1] + endNode1.getPosition()[1]
        );
    }

    private List<String> getStringRoute(List<int[]> posRoute, String startingMark, String[][] markArray) {
        List<String> route = new ArrayList<>();
        route.add(startingMark);
        int[] currentPos = posRoute.get(0);

        for (int i = 1; i < posRoute.size(); i++) {
            int[] nextPos = posRoute.get(i);
            String nextMark = markArray[nextPos[0]][nextPos[1]];
            if (currentPos[0] == nextPos[0]){
                if (currentPos[1] < nextPos[1]){
                    nextMark += "n";
                }
                else {
                    nextMark += "s";
                }
            }
            else if (currentPos[1] == nextPos[1]){
                if (currentPos[0] < nextPos[0]){
                    nextMark += "e";
                }
                else {
                    nextMark += "w";
                }
            }
            route.add(nextMark);
            currentPos = nextPos;
        }

        return route;
    }

    private int[] getPosition(String mark, String[][] markArray) {
        for (int i = 0; i < markArray.length; i++) {
            for (int j = 0; j < markArray[0].length; j++) {
                if (markArray[i][j].substring(0,1).equals(mark.substring(0,1))){
                    return new int[]{i,j};
                }
            }
        }
        throw new IllegalArgumentException("Error get position");
    }

    private String[][] getMarkArray(List<Mark> marks) {
        int maxX = 0;
        int maxY = 0;
        for (Mark mark : marks) {
            if (mark.getName().charAt(1) == 'n'){
                int xCoordinate = mark.getX();
                if (maxX < xCoordinate){
                    maxX = xCoordinate;
                }
                int yCoordinate = mark.getY();
                if (maxY < yCoordinate){
                    maxY = yCoordinate;
                }
            }
        }

        String[][] array = new String[maxX+1][maxY+1];
        for (Mark mark : marks) {
            if (mark.getName().charAt(1) == 'n'){
                array[mark.getX()][mark.getY()] = mark.getName().substring(0,1);
            }
        }

        return array;
    }

}
