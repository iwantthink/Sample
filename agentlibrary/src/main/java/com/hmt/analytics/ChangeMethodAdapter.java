package com.hmt.analytics;

import com.hmt.org.objectweb.asm.MethodVisitor;
import com.hmt.org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by renbo on 2017/4/24.
 */

public class ChangeMethodAdapter extends AdviceAdapter {

    protected ChangeMethodAdapter(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);

    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        System.out.println("name = " + name);
    }
}
