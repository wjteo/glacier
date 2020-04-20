package com.aws.wj;

import org.junit.Assert;
import org.junit.Test;

public class ProgressListenerImplTest {
    @Test
    public void getPercentageShouldRunCorrectly(){
        long percentage = ProgressListenerImpl.getPercentage(9999L,10000L);
        Assert.assertEquals(99,percentage);
    }
}
