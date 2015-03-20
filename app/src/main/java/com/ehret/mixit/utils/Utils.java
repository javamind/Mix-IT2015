package com.ehret.mixit.utils;

import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ehret_g on 18/03/15.
 */
public class Utils {
    public static <C> List<C> asList(LongSparseArray<C> sparseArray) {
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }
}
