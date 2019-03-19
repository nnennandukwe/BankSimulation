package com.company;


public class AQueue<T> {

    private int head;
    private int tail;
    private int count;

    private static final int QUEUE_SIZE = 32;
    private T []queue;

    public AQueue() {
        queue = (T[]) new Object[QUEUE_SIZE];
    }

    public AQueue(int size) {
        queue = (T[]) new Object[size];
    }

    public T remove() {
        if (isEmpty()) {
            return null;
        } else {
            T rtn = queue[head++];

            // Handle wrap
            if (head == queue.length) {
                head = 0;
            }
            count--;
            return rtn;
        }

    }

    public boolean add(T data) {
        if (isFull()) {
            return false;
        } else {
            queue[tail++] = data;
            count++;
            if (tail == queue.length) {
                tail = 0;
            }
            return true;
        }

    }

    public int size() {
        return count;
    }

    public int maxSize() {
        return queue.length;
    }

    public boolean isEmpty() {
        return (count == 0);
    }

    public boolean isFull() {
        return (count == queue.length);
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        } else {
            return queue[head];
        }
    }

    public void ensureCapacity(int elements) {
        if (maxSize() >= elements) {
            return;
        } else {
            T []newQ = (T[]) new Object[elements];

            if (count > 0) {
                int tmp = head;
                for (int i = 0; i < count; i++) {
                    newQ[i] = queue[tmp++];
                    if (tmp == queue.length) {
                        tmp = 0;
                    }
                }
                head = 0;
                tail = count;
            } else {
                // Nothing in old queue
                count = head = tail = 0;
            }
            queue = newQ;
        }
    }

    public void queue_dec_ttl() {
        int tmp = head;

        for (int i = 0; i < count; i++) {
            ((Plane) queue[tmp++]).ttl--;
            if (tmp == queue.length) {
                tmp = 0;
            }
        }

    }


    public String toString() {
        String rtn = "";

        int tmp = head;
        rtn += "Head -> ";
        for (int i = 0; i < count; i++) {
            rtn += queue[tmp++] + "\n";
            if (tmp == queue.length) {
                tmp = 0;
            }
        }

        return rtn;
    }

}
