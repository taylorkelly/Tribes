/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.tribes.core.util;

import com.first.tribes.core.Tribes;

/**
 *
 * @author taylor
 */
public class Timer implements Updatee {

    private Tribes game;
    private long initialDelay = -1;
    private long repeatedDelay = -1;
    private long endTime = -1;
    private TimerTask task;
    private boolean running;

    public Timer(Tribes game) {
        this.game = game;
    }

    public void cancel() {
        if (running) {
            running = false;
            game.unregisterUpdatee(this);
        }
    }

    public void schedule(TimerTask task, long initialDelay, long repeatedDelay) {
        if (!running) {
            this.initialDelay = initialDelay;
            this.repeatedDelay = repeatedDelay;
            this.task = task;
            this.running = true;
            this.endTime = System.currentTimeMillis() + initialDelay;
            game.registerUpdatee(this);
        }
    }

    public void update(float delta) {
        if (running) {
            if (System.currentTimeMillis() >= endTime) {
                task.run();
                if (repeatedDelay != -1) {
                    endTime = System.currentTimeMillis() + repeatedDelay;
                } else {
                    this.cancel();
                }
            }
        }
    }

    public interface TimerTask {

        public void run();
    }
}
