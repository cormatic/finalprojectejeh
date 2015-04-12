package com.example.ericjohannsen.finalprojectejeh;

import java.util.ArrayList;

/**
 * Created by Evan on 4/9/2015.
 */
public class Levels {

    ArrayList<Integer> targetArrayList = new ArrayList<>();

    public void addTarget(ArrayList<Integer> list)
    {
        this.targetArrayList = list;
    }

    public ArrayList<Integer> getTargetArrayList() {
        return targetArrayList;
    }
}
