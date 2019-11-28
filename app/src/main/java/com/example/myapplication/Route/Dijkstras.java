package com.example.myapplication.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstras {

    private final Map<String, List<Vertex>> vertices;

    public Map<String, List<Vertex>> getVertices() {
        return vertices;
    }

    public Dijkstras() {
        this.vertices = new HashMap<>();
    }

    public void addVertex(String character, List<Vertex> vertex) {
        this.vertices.put(character, vertex);
    }

    //역간 가중치를 더하여 반환
    public double getWeight(String start, List<String> route){
        double weight=0;    //가중치가 들어갈 weight 변수
        for(int i=0;i<route.size()-1;i++){
            weight+= getDistance(route.get(i+1),getVertices().get(route.get(i)));
        }
        return  weight;
    }

    //현재역에서 찾으려는 역 사이에 가중치를 반환
    public double getDistance(String station, List<Vertex> vertex){
        for(int i=0;i<vertex.size();i++){
            if(vertex.get(i).getId().equals(station)){  //찾으려는 역과 일치하는지 확인
                return vertex.get(i).getDistance();
            }
        }
        return 0.0;
    }

    //다익스트라 구하기
    public List<String> getDijkstras(String start, String end) {
        final Map<String, Double> distances = new HashMap<>();
        final Map<String, Vertex> previous = new HashMap<>();

        //오름차순으로 정렬되는 Queue
        PriorityQueue<Vertex> nodes = new PriorityQueue<>();

        // 출발역의 값은 0으로, 나머지 역의 값은 최대값
        for(String vertex : vertices.keySet()) {
            if (vertex.equals(start)) {
                distances.put(vertex, (double) 0);
                nodes.add(new Vertex(vertex, (double) 0));
            } else {
                distances.put(vertex, Double.MAX_VALUE);
                nodes.add(new Vertex(vertex, Double.MAX_VALUE));
            }
            previous.put(vertex, null);
        }

        while (!nodes.isEmpty()) {
            Vertex smallest = nodes.poll();
            //
            if (smallest.getId().equals(end)) {
                final List<String> path = new ArrayList<>();
                while (previous.get(smallest.getId()) != null) {
                    path.add(smallest.getId());
                    smallest = previous.get(smallest.getId());
                }
                return path;
            }
            if (distances.get(smallest.getId()) == Double.MAX_VALUE) {
                break;
            }

            for (Vertex neighbor : vertices.get(smallest.getId())) {
                Double alt = distances.get(smallest.getId()) + neighbor.getDistance();
                if (alt < distances.get(neighbor.getId())) {
                    distances.put(neighbor.getId(), alt);
                    previous.put(neighbor.getId(), smallest);
                    forloop:
                    for(Vertex n : nodes) {
                        if (n.getId().equals(neighbor.getId())) {
                            nodes.remove(n);
                            n.setDistance(alt);
                            nodes.add(n);
                            break forloop;
                        }
                    }
                }
            }
        }
        return new ArrayList<>(distances.keySet());
    }

}