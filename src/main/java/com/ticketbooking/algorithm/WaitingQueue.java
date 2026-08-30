package com.ticketbooking.algorithm;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WaitingQueue {
    private final Map<Long, Queue<WaitingRequest>> waitingLists = new HashMap<>();

    public void addToQueue(Long movieId, WaitingRequest request) {
        Queue<WaitingRequest> queue =
                waitingLists.computeIfAbsent(
                        movieId,
                        k -> new LinkedList<>()
                );

        queue.offer(request);

        System.out.println("=================================");
        System.out.println("USER ADDED TO WAITING QUEUE");
        System.out.println("Movie ID: " + movieId);
        System.out.println("User ID: " + request.getUserId());
        System.out.println("Seats: " + request.getSeats());
        System.out.println("Queue size: " + queue.size());
        System.out.println("=================================");
    }

    public WaitingRequest getNext(Long movieId) {
        Queue<WaitingRequest> queue = waitingLists.get(movieId);
        if (queue == null || queue.isEmpty()) return null;
        return queue.peek();
    }

    public WaitingRequest removeNext(Long movieId) {

        Queue<WaitingRequest> queue = waitingLists.get(movieId);

        if (queue == null || queue.isEmpty()) {
            return null;
        }

        return queue.poll();
    }

    public Queue<WaitingRequest> getWaitingList(Long movieId) {
        return waitingLists.getOrDefault(
                movieId,
                new LinkedList<>()
        );
    }
}