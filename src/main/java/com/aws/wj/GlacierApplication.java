package com.aws.wj;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.transfer.ArchiveTransferManager;
import com.amazonaws.services.glacier.transfer.UploadResult;

import java.io.File;
import java.io.IOException;
import java.util.Date;


public class GlacierApplication{

    public final static String VAULT_NAME_ARG="--vault-name";
    public final static String INPUT_FILE_PATH_ARG="--input-file-path";
    public final static String ACCOUNT_ID="--account-id";
    public static AmazonGlacierClient client;


    public static void main(String[] args) throws IOException {

        ProfileCredentialsProvider credentials = new ProfileCredentialsProvider();
        client = new AmazonGlacierClient(credentials);
        client.setEndpoint("https://glacier.us-east-2.amazonaws.com/");

        try {
            ArchiveTransferManager atm = new ArchiveTransferManager(client, credentials);
            String vaultName = getArgument(args,VAULT_NAME_ARG);
            String archiveToUpload = getArgument(args,INPUT_FILE_PATH_ARG);
            String accountId = getArgument(args,ACCOUNT_ID);

            System.out.println("-----------------------------------------------------------------");
            System.out.println(String.format("Upload %s to %s",archiveToUpload,vaultName));
            System.out.println("-----------------------------------------------------------------");
//            UploadResult result = atm.upload(vaultName, "my archive " + (new Date()), new File(archiveToUpload));
            UploadResult result = atm.upload(accountId,vaultName,"my archive " + (new Date()),new File(archiveToUpload),new ProgressListenerImpl());
            System.out.println("Archive ID: " + result.getArchiveId());

        } catch (Exception e)
        {
            System.err.println(e);
        }
    }

    public static String getArgument(String[] args,String targetArg) throws Exception{
        for(int i=0;i<args.length;i++){
            if(!args[i].equals(targetArg)){
                continue;
            }
            if(!(i+1<args.length)){
                throw new ArgumentErrorException(targetArg);
            }
            if(args[i+1].isEmpty()){
                throw new ArgumentErrorException(targetArg);
            }
            return args[i+1];
        }
        throw new ArgumentErrorException(targetArg);
    }

    private static class ArgumentErrorException extends Exception{
        public ArgumentErrorException(String argument){
            super(String.format("Argument Error: %s is compulsory",argument));
        }
    }

}