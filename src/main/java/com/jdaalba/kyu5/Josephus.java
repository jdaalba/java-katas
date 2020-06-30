package com.jdaalba.kyu5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Josephus {

    public static <T> List<T> josephusPermutation(final List<T> items, final int k) {
        final Circle<T> circle = new Circle<>(items);
        var counter = 1;
        final var elements = new ArrayList<T>();
        while (circle.hasNext()) {
            final var element = circle.next();
            if (counter == k) {
                circle.remove();
                elements.add(element);
                counter = 1;
            } else {
                counter++;
            }
        }
        return elements;
    }

    private static class Circle<T> implements Iterator<T> {

        private final List<Node<T>> elements;

        private Node<T> currentElement;

        private Node<T> deletedElement;

        public Circle(List<T> list) {
            final var elements = list.stream()
                    .map(Node::new)
                    .collect(Collectors.toList());
            final var lastIndex = elements.size() - 1;
            for (var i = 0; i < list.size(); i++) {
                final var currentNode = elements.get(i);
                if (i == 0) {
                    currentNode.prev = elements.get(lastIndex);
                } else {
                    currentNode.prev = elements.get(i - 1);
                }
                if (i == lastIndex) {
                    currentNode.next = elements.get(0);
                } else {
                    currentNode.next = elements.get(i + 1);
                }
            }
            this.elements = elements;
        }

        @Override
        public boolean hasNext() {
            return !elements.isEmpty();
        }

        @Override
        public T next() {
            if (Objects.isNull(currentElement)) {
                if (Objects.isNull(deletedElement)) {
                    currentElement = elements.get(0);
                } else {
                    currentElement = deletedElement.next;
                    deletedElement = null;
                }
            } else {
                currentElement = currentElement.next;
            }
            return currentElement.item;
        }

        @Override
        public void remove() {
            if (Objects.isNull(currentElement)) {
                currentElement = elements.get(0);
            }
            deletedElement = currentElement;
            final var prev = currentElement.prev;
            final var next = currentElement.next;
            prev.next = next;
            next.prev = prev;
            elements.remove(deletedElement);
        }

        private static class Node<E> {
            E item;
            Circle.Node<E> next;
            Circle.Node<E> prev;

            Node(E item) {
                this.item = item;
            }
        }
    }
}