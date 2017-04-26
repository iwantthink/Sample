package com.hmt.analytics;

import java.lang.instrument.Instrumentation;

/**
 * Created by renbo on 2017/4/24.
 */

public class TransformAgent {

    public static void agentmain(String args, Instrumentation inst) {
        premain(args, inst);
    }

    private static void premain(String args, Instrumentation inst) {


    }

}
