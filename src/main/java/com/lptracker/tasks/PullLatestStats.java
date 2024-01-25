package com.lptracker.tasks;

import java.util.TimerTask;

public class PullLatestStats extends TimerTask {
    @Override
    public void run() {
        System.out.println("I am running on an interval!");
    }
}
