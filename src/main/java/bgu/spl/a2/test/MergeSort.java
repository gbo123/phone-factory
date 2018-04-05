/*
 * To change this license header, choose License Headers in Project Properties.

 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }


    @Override
    protected void start() {

        int size = array.length;
        if (size < 2) {
            complete(array);
            return;
        }

        List<Task<int[]>> tasks = new ArrayList<>();
        int mid = size / 2;
        int[] left = new int[mid];
        int[] right = new int[size - mid];
        for (int i = 0; i < mid; i++) {
            left[i] = array[i];
        }
        for (int i = mid; i < size; i++) {
            right[i - mid] = array[i];
        }

        MergeSort task1 = new MergeSort(left);
        MergeSort task2 = new MergeSort(right);
        spawn(task1);
        spawn(task2);
        tasks.add(task1);
        tasks.add(task2);

        whenResolved(tasks, () -> {

                    int[] result = new int[size];

                    result = merge(tasks.get(0).getResult().get(), tasks.get(1).getResult().get(), result);

                    complete(result);

                }

        );


    }


    private int[] merge(int[] left, int[] right, int[] result) {

   


        int sizeLeft = left.length;
        int sizeRight = right.length;
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < sizeLeft && j < sizeRight) {

            if (left[i] <= right[j]) {
                result[k] = left[i];
                i++;
            }

            else {
                result[k] = right[j];
                j++;

            }
            k++;


        }

        while (i < sizeLeft) {
            result[k] = left[i];
            i++;
            k++;

        }

        while (j < sizeRight) {
            result[k] = right[j];
            j++;
            k++;

        }

   
                return result;


    }


    public static void main(String[] args) throws InterruptedException {
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int n = 15000; //you may check on different number of elements if you like
        int[] array = new Random().ints(n).toArray();
        MergeSort task = new MergeSort(array);
        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        task.getResult().whenResolved(() -> {
        	//warning - a large print!! - you can remove this line if you wish
        	System.out.println(Arrays.toString(task.getResult().get()));
        	
        	l.countDown();
        });

        l.await();
        System.out.println("finished");  
        
        pool.shutdown();
    }


    
}



