package com.bliblifuturebackend.bliblimart.model.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    private Node parent;

    private int[] position;

    //Jarak antara starting node dgn current node
    private int gCost;

    //jarak antara current node dgn target node
    private int hCost;

    // Jarak antara starting node dgn target node
    private int fCost;

}
