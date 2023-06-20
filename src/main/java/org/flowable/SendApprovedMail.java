package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendApprovedMail implements JavaDelegate {

    /**
     * 触发发送邮件的操作
     * @param delegateExecution
     */
    @Override
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("**********************");
        System.out.println("该方案做的不错，再接再厉！");
        System.out.println("**********************");
    }
}
