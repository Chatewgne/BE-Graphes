package org.insa.algo.utils;

import org.junit.Test;

public class BinarySearchTreeTest extends PriorityQueueTest {

    @Override
    public PriorityQueue<MutableInteger> createQueue() {
        return new BinarySearchTree<>();
    }

    @Override
    public PriorityQueue<MutableInteger> createQueue(PriorityQueue<MutableInteger> queue) {
        return new BinarySearchTree<>((BinarySearchTree<MutableInteger>) queue);
    }

    @Test
    public void TestDijkstraAlgorithm(){

    }
}
