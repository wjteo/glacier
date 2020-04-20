package com.aws.wj;


import org.junit.Assert;
import org.junit.Test;

public class GlacierApplicationTest {
    @Test
    public void getArgumentShouldRunCorrectly(){
        try{
            String arg = GlacierApplication.getArgument(new String[]{"--vault-name","vaultName"},"--vault-name");
            Assert.assertEquals(arg,"vaultName");
        }catch(Exception e){
            Assert.assertEquals(e,null);
        }

    }
}
