/**
 * Copyright (C) 2016 Newland Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newlandframework.mymq.core;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @filename:MessageCache.java
 * @description:MessageCache功能模块
 * 
 * 
 * 
 */
public class MessageCache<T> {

    private ConcurrentLinkedQueue<T> cache = new ConcurrentLinkedQueue<T>();

    private Semaphore semaphore = new Semaphore(0);

    public void appendMessage(T id) {
        cache.add(id);
        semaphore.release();
    }

    public void parallelDispatch(LinkedList<T> list) {

    }

    public void commit(ConcurrentLinkedQueue<T> tasks) {
        commitMessage(tasks);
    }

    public void commit() {

        commitMessage(cache);
    }

    private void commitMessage(ConcurrentLinkedQueue<T> messages) {

        LinkedList<T> list = new LinkedList<T>();

        list.addAll(messages);
        cache.clear();

        if (list != null && list.size() > 0) {
            parallelDispatch(list);
            list.clear();
        }
    }

    public boolean hold(long timeout) {
        try {
            return semaphore.tryAcquire(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            Logger.getLogger(MessageCache.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    protected Pair<Integer, Integer> calculateBlocks(int parallel, int sizeOfTasks) {
        int numberOfThreads = parallel > sizeOfTasks ? sizeOfTasks : parallel;
        Pair<Integer, Integer> pair = new MutablePair<Integer, Integer>(new Integer(sizeOfTasks / numberOfThreads), new Integer(numberOfThreads));
        return pair;
    }
}
